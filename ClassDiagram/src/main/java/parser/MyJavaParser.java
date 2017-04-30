package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AssignExpr;
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
    public static Map<String, ClassPojo> rules = new HashMap<String, ClassPojo>();
    public static Map<String, Integer> accessAttrMap = new HashMap<String, Integer>();
    public static Map<String, String>  classMapping = new HashMap<>();
    public static Map<String, List<String>> varsInMain = null;
    public static String outputFileName;
    
    public static void parse(String sourceDir, String destDir) {
        CompilationUnit compilationUnit = null;
        List<ClassOrInterfaceDeclaration> classesOrInterafces = null;
        List<FieldPojo> fields = null;
        List<MethodPojo> methods = null;
        List<ConstructorPojo> constructors = null;
        List<ClassPojo> classPojos = new ArrayList<ClassPojo>();
        List<String> extendedTypes = null;
        List<String> implementedTypes = null;
        outputFileName = sourceDir.split("/")[sourceDir.split("/").length -1];

        Utils utils = new Utils();
        List<String> files = utils.getFilePaths(sourceDir);
        try {
            for (String f : files) {
                File sourceFile = new File(f);
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
    	PlantUMLPojo plantUMLObj = new PlantUMLPojo();
        String fileName = outputFileName + ".png";
        Set<String> duplicates = new HashSet<String>();
        
        plantUMLObj.createStartTag();
        
        for (ClassPojo pj : classPojos) {
            if (pj.isInterface()) {
            	plantUMLObj.appendInterface(pj.getClassName());	
            } else {
            	plantUMLObj.appendClass(pj.getClassName());
            }
            plantUMLObj.appendBracket("{");

            // Extends and implements
            for (String parentClassOrInterface:  pj.getImplementedTypes()) {
            	plantUMLObj.buildReferenceRelationship(parentClassOrInterface, pj.getClassName(), " <|.. ", null);
            }

            for (String parentClassOrInterface:  pj.getExtendedTypes()) {
            	plantUMLObj.buildReferenceRelationship(parentClassOrInterface, pj.getClassName(), " <|-- ", null);
            }
            
            for (FieldPojo field : pj.getFields()) {
                // Association
                if (rules.containsKey(field.getType()) && !duplicates.contains(field.getType()) && !field.getType().equals("String")) {
                    ClassPojo cpj = rules.get(field.getType());
                    String multiplicity = " ";
                    for (FieldPojo f : cpj.getFields()) {
                        if (f.getType().equals(pj.getClassName())) {
                            multiplicity = f.getMultiplicity();
                            break;
                        }
                    }
                    
                    plantUMLObj.buildMultiplicity(pj.getClassName(), multiplicity, cpj.getClassName(), field.getMultiplicity());

                    duplicates.add(pj.getClassName());
                }

                if (!field.isPrimitiveType() && !field.getType().equals("String")) {
                    continue;
                }

                String fieldModifier = "";
                if (field.isPublic()) {
                	fieldModifier = "+";
                } else {
                    if (accessAttrMap.get(field.getFieldName()) != null && accessAttrMap.get(field.getFieldName()) > 0) {
                        fieldModifier = "+";
                    } else {
                    	fieldModifier = "-";
                    }
                }
                plantUMLObj.buildField(fieldModifier, field.getFieldName(), field.getType());
            }

            for (ConstructorPojo constructorPojo: pj.getConstructors()) {
                StringBuilder sb = new StringBuilder();
                List<ParamPojo> paramList = constructorPojo.getParamList();
                for (ParamPojo p: paramList) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(p.getParam()).append(" : ").append(p.getType());

                    if (!p.isPrimitiveType() && !p.getParam().equals("String")) {
                    	plantUMLObj.buildReferenceRelationship(p.getType(), pj.getClassName(), " <.. ", null);
                    }
                }
                
                plantUMLObj.buildConstructor(constructorPojo.getConstructor(), sb.toString());
            }


            for (MethodPojo method : pj.getMethods()) {
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
                            plantUMLObj.buildReferenceRelationship(childClass, parentClass, " <.. ", ": uses");
                        }
                    }
                }
                plantUMLObj.buildMethod(method.getMethodName(), sb.toString(), method.getMethodType());
            }

            plantUMLObj.appendNewLine();
            plantUMLObj.appendBracket("}");

            if (varsInMain != null && varsInMain.containsKey(pj.getClassName())) {
                List<String> vars = varsInMain.get(pj.getClassName());
                vars.forEach((String var) -> {
                	plantUMLObj.buildReferenceRelationship(var, pj.getClassName(), "<.. ", null);
                });
            }


        }
        
        plantUMLObj.createEndTag();
        try {
            SourceStringReader reader = new SourceStringReader(plantUMLObj.buildumlBodyString());
            String outputFile = destDir;
            if (!destDir.endsWith(".png")) {
            	outputFile = destDir + fileName;
            }
            FileOutputStream output = new FileOutputStream(new File(outputFile));
            reader.generateImage(output, new FileFormatOption(FileFormat.PNG, false));
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
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
