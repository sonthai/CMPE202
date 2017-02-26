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
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
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
    static int i = 0;
    public static void parse(String sourceDir) {
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
                File sourceFile = new File(rootDir + f);
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
                            FieldPojo fieldPj = new FieldPojo(field.isPublic(), field.getVariables());
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
                                    //constructorPojo.setParams(param.getType().toString(), param.getName().toString())
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
            umlConfigGen(classPojos);
            utils.prettyJson(classPojos);
            rules.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void umlConfigGen(List<ClassPojo> classPojos) {
        String fileName = "file " + i + ".png";
        /*Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), "utf-8"));
            writer.write("@startuml\n");
            for (ClassPojo pj: classPojos) {
                writer.write("\nclass " + pj.getClassName() + " {");
                for (FieldPojo field: pj.getFields()) {
                    writer.write("\n\t");
                    if (field.isPublic()) {
                        writer.write("+");
                    } else {
                        writer.write("-");
                    }
                    writer.write(field.getFieldName() + " : " + field.getType());
                }

                for (MethodPojo method: pj.getMethods()) {
                    writer.write("\n\t+"  + method.getMethodName() + " : " + method.getMethodType());
                }

                writer.write("\n}\n");
            }
            writer.write("\n@enduml");
        } catch (IOException ex) {

        } finally {
            try {
                writer.close();
                i++;
            } catch (Exception ex) {
            }
        }*/
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
                            /*extraUmlSource.append(pj.getClassName()).append(" \"").append(f.getMultiplicity()).append("\"").append(" -- ");
                            extraUmlSource.append("\"").append(field.getMultiplicity());
                            extraUmlSource.append("\" ").append(cpj.getClassName());
                            extraUmlSource.append("\n");*/
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
        }
        plantUmlSource.append("\n");
        plantUmlSource.append(extraUmlSource);
        /*Set<String> duplicates = new HashSet<String>();
        for (Map.Entry<String, ClassPojo> rule: rules.entrySet()) {
            for (FieldPojo fieldPj: rule.getValue().getFields()) {
                if (rules.containsKey(fieldPj.getType()) && !duplicates.contains(fieldPj.getType())) {
                    ClassPojo cpj = rules.get(fieldPj.getType());
                    for (FieldPojo f : cpj.getFields()) {
                        if (f.getType().equals(rule.getKey())) {
                            plantUmlSource.append(rule.getKey()).append(" \"").append(f.getMultiplicity()).append("\"").append(" -- ");
                            plantUmlSource.append("\"").append(fieldPj.getMultiplicity());
                            plantUmlSource.append("\" ").append(cpj.getClassName()); //).append("\n(").append(rule.getKey()).append(", ").append(cpj.getClassName()).append(")");
                            plantUmlSource.append("\n");
                            break;
                        }
                    }
                }
            }
            duplicates.add(rule.getKey());
        }*/

        plantUmlSource.append("\n@enduml");
        System.out.println("************");
        System.out.println(plantUmlSource.toString());
        try {
            SourceStringReader reader = new SourceStringReader(plantUmlSource.toString());
            FileOutputStream output = new FileOutputStream(new File(fileName));
            reader.generateImage(output, new FileFormatOption(FileFormat.PNG, false));
            i++;
        } catch (Exception e) {}
    }

    public static String checkIfGetterSetterMethods(MethodDeclaration method) {
        Optional<BlockStmt> body = method.getBody();
        //System.out.println(method.getName() + " " + method.getParentNode());
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
}
