package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import model.*;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import util.Utils;

import java.io.*;
import java.util.*;

/**
 * Created by sonthai on 2/20/17.
 */
public class MyJavaParser {
    public static String rootDir = "src/main/resources/";
    public static Map<String, ClassPojo> rules = new HashMap<String, ClassPojo>();
    public static Map<String, Integer> accessAttrMap = new HashMap<String, Integer>();
    public static Map<String, String>  classMapping = new HashMap<>();
    public static Map<String, List<String>> varsInMain = null;
    static int i = 0;
    public static void parse(String sourceDir, String destDir) {
        CompilationUnit compilationUnit = null;
        List<ClassOrInterfaceDeclaration> classesOrInterafces = null;
        List<FieldPojo> fields = null;
        List<MethodPojo> methods = null;
        List<ConstructorPojo> constructors = null;
        List<ClassPojo> classPojos = new ArrayList<ClassPojo>();
        List<String> extendedTypes = null;
        List<String> implementedTypes = null;

        Utils utils = new Utils();
        List<String> files = utils.getFilePaths(sourceDir);
        try {
            for (String f : files) {
                File sourceFile = new File(f);
                System.out.println("Source file " + sourceFile);
                compilationUnit = JavaParser.parse(sourceFile);
                classesOrInterafces = compilationUnit.getNodesByType(ClassOrInterfaceDeclaration.class);
                for (ClassOrInterfaceDeclaration declaration : classesOrInterafces) {
                    ClassPojo pj = new ClassPojo();
                    pj.setInterface(declaration.isInterface());
                    pj.setClassName(declaration.getNameAsString());

                    implementedTypes = new ArrayList<String>();

                    for (ClassOrInterfaceType type: declaration.getImplementedTypes()) {
                        implementedTypes.add(type.getNameAsString());
                    }

                    pj.setImplementedTypes(implementedTypes);

                    extendedTypes = new ArrayList<String>();
                    for(ClassOrInterfaceType type: declaration.getExtendedTypes()) {
                        extendedTypes.add(type.getNameAsString());
                    }
                    pj.setExtendedTypes(extendedTypes);

                    fields = new ArrayList<FieldPojo>();
                    for (FieldDeclaration field: declaration.getFields()) {
                        if (field.isPrivate() || field.isPublic()) {
                            FieldPojo fieldPj = new FieldPojo(field.isPublic(), field.getVariables().get(0));
                            fields.add(fieldPj);
                        }
                    }
                    pj.setFields(fields);

                    constructors = new ArrayList<>();
                    for (ConstructorDeclaration constructor: declaration.getNodesByType(ConstructorDeclaration.class)) {
                        if (constructor.isPublic()) {
                            ConstructorPojo constructorPojo = new ConstructorPojo();
                            constructorPojo.setConstructor(constructor.getName().toString());
                            constructor.getParameters().forEach((Parameter param) ->
                                    constructorPojo.addParams(new ParamPojo(param.getType(), param.getName().toString()))
                            );
                            constructors.add(constructorPojo);
                        }
                    }

                    pj.setConstructors(constructors);

                    methods = new ArrayList<MethodPojo>();
                    for (MethodDeclaration method: declaration.getMethods()) {
                        if (method.isPublic()) {
                            if (!pj.isInterface()) {
                                String fieldName = checkIfGetterSetterMethods(method);
                                if (!fieldName.equals("")) {
                                    if (accessAttrMap.containsKey(fieldName)) {
                                        int count = accessAttrMap.get(fieldName);
                                        accessAttrMap.put(fieldName, count++);
                                    } else {
                                        accessAttrMap.put(fieldName, 1);
                                    }
                                }

                                if (!fieldName.equals("")) {
                                    continue;
                                }
                            }

                            // Analyze main method
                            if (method.getName().getIdentifier().equals("main")) {
                                varsInMain = new HashMap<>();
                                List<String> vars = new ArrayList<>(analyzeBodyMethod(method));
                                varsInMain.put(pj.getClassName(), vars);
                            }

                            List<ParamPojo> params = new ArrayList<ParamPojo>();
                            MethodPojo methodPj = new MethodPojo(method.getType(), method.getNameAsString());
                            for (Parameter p: method.getParameters()) {
                                params.add(new ParamPojo(p.getType(), p.getNameAsString()));
                            }
                            methodPj.setParamPojo(params);
                            methods.add(methodPj);
                        }
                    }
                    pj.setMethods(methods);

                    classPojos.add(pj);
                    rules.put(pj.getClassName(), pj);
                }
            }
            umlConfigGen(classPojos, destDir);
            //utils.prettyJson(classPojos);
            rules.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void umlConfigGen(List<ClassPojo> classPojos, String destDir) {
        String fileName = "file" + i + ".png";
        StringBuilder plantUmlSource = new StringBuilder();
        StringBuilder extraUmlSource = new StringBuilder();
        Set<String> duplicates = new HashSet<String>();
        plantUmlSource.append("@startuml");
        for (ClassPojo pj : classPojos) {
            if (pj.isInterface()) {
                plantUmlSource.append("\ninterface ").append(pj.getClassName()).append(" {");
            } else {
                plantUmlSource.append("\nclass ").append(pj.getClassName()).append(" {");
            }

            // Extends and implements
            for (String parentClassOrInterface:  pj.getImplementedTypes()) {
                extraUmlSource.append(parentClassOrInterface).append(" <|.. ").append(pj.getClassName()).append("\n");
            }

            for (String parentClassOrInterface:  pj.getExtendedTypes()) {
                extraUmlSource.append(parentClassOrInterface).append(" <|-- ").append(pj.getClassName()).append("\n");
            }
            for (FieldPojo field : pj.getFields()) {
                // Association
                if (rules.containsKey(field.getType()) && !duplicates.contains(field.getType()) && !field.getType().equals("String")) {
                    ClassPojo cpj = rules.get(field.getType());
                    String multiplicity = "";
                    for (FieldPojo f : cpj.getFields()) {
                        if (f.getType().equals(pj.getClassName())) {
                            multiplicity = f.getMultiplicity();
                            break;
                        }
                    }

                    extraUmlSource.append(pj.getClassName());
                    if (multiplicity.length() > 0) {
                        extraUmlSource.append(" \"").append(multiplicity).append("\"");
                    } else {
                        extraUmlSource.append(" ");
                    }
                    extraUmlSource.append(" -- ");
                    extraUmlSource.append("\"").append(field.getMultiplicity());
                    extraUmlSource.append("\" ").append(cpj.getClassName());
                    extraUmlSource.append("\n");

                    duplicates.add(pj.getClassName());
                }

                if (!field.isPrimitiveType() && !field.getType().equals("String")) {
                    continue;
                }

                plantUmlSource.append("\n\t");
                if (field.isPublic()) {
                    plantUmlSource.append("+");
                } else {
                    if (accessAttrMap.get(field.getFieldName()) != null && accessAttrMap.get(field.getFieldName()) > 0) {
                        plantUmlSource.append("+");
                    } else {
                        plantUmlSource.append("-");
                    }
                }
                plantUmlSource.append(field.getFieldName()).append(" : ").append(field.getType());
            }

            for (ConstructorPojo constructorPojo: pj.getConstructors()) {
                plantUmlSource.append("\n\t+").append(constructorPojo.getConstructor()).append("(");
                StringBuilder sb = new StringBuilder();
                List<ParamPojo> paramList = constructorPojo.getParamList();
                for (ParamPojo p: paramList) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(p.getParam()).append(" : ").append(p.getType());

                    if (!p.isPrimitiveType() && !p.getParam().equals("String")) {
                        extraUmlSource.append(p.getType()).append(" <.. ").append(pj.getClassName()).append("\n");
                    }
                }

                plantUmlSource.append(sb).append(")");
            }


            for (MethodPojo method : pj.getMethods()) {
                plantUmlSource.append("\n\t+").append(method.getMethodName()).append("(");
                StringBuilder sb = new StringBuilder();

                for(ParamPojo p: method.getParamPojo()) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(p.getParam()).append(" : ").append(p.getType());

                    if (!p.isPrimitiveType() && !p.getType().equals("String")) {
                        String childClass = p.getType();
                        String parentClass = pj.getClassName();
                        if (classMapping.get(childClass) == null || classMapping.get(parentClass) == null) {
                            classMapping.put(childClass, parentClass);
                            classMapping.put(parentClass, childClass);
                            extraUmlSource.append(childClass).append(" <.. ").append(parentClass).append(": uses\n");
                        }
                    }
                }
                plantUmlSource.append(sb).append(") : ").append(method.getMethodType());
            }

            plantUmlSource.append("\n}");

            if (varsInMain != null && varsInMain.containsKey(pj.getClassName())) {
                List<String> vars = varsInMain.get(pj.getClassName());
                vars.forEach((String var) -> {
                    extraUmlSource.append(var).append("<..").append(pj.getClassName()).append("\n");
                });
            }


        }
        plantUmlSource.append("\n");

        plantUmlSource.append(extraUmlSource);
        plantUmlSource.append("\n@enduml");
        System.out.println("************");
        System.out.println(plantUmlSource.toString());
        try {
            SourceStringReader reader = new SourceStringReader(plantUmlSource.toString());
            FileOutputStream output = new FileOutputStream(new File(destDir + fileName));
            reader.generateImage(output, new FileFormatOption(FileFormat.PNG, false));
            i++;
        } catch (Exception e) {}
    }

    public static String checkIfGetterSetterMethods(MethodDeclaration method) {
        Optional<BlockStmt> body = method.getBody();
        NodeList<Statement> block = body.get().getStatements();
        String fieldAccess = "";

        if (block.size() == 1) {
            if (block.get(0) instanceof ExpressionStmt) {
                ExpressionStmt expressionStmt = (ExpressionStmt) block.get(0);
                if (expressionStmt.getExpression() instanceof  AssignExpr) {
                    AssignExpr assignExpr = (AssignExpr) expressionStmt.getExpression();
                    if (assignExpr.getTarget() instanceof FieldAccessExpr) {
                        fieldAccess = ((FieldAccessExpr) assignExpr.getTarget()).getName().toString();
                    }
                }
                System.out.println(expressionStmt.toString());
            } else if (block.get(0) instanceof ReturnStmt) {
                ReturnStmt returnStmt = (ReturnStmt) block.get(0);
                if (returnStmt.getExpression().get() instanceof FieldAccessExpr) {
                    fieldAccess = ((FieldAccessExpr) returnStmt.getExpression().get()).getName().toString();
                }
            }


        }


        return fieldAccess;
    }

    public static Set<String> analyzeBodyMethod(MethodDeclaration method) {
        Set<String> variables = new HashSet<>();
        NodeList<Statement> statements = method.getBody().get().getStatements();

        for (Statement stmt: statements) {
            for (VariableDeclarator varDecl: stmt.getNodesByType(VariableDeclarator.class)) {
                FieldPojo fieldPojo = new FieldPojo(false, varDecl);
                if (!fieldPojo.isPrimitiveType() && !fieldPojo.getType().equals("String")) {
                    variables.add(fieldPojo.getType());
                }
            }
        }

        return variables;

    }
}
