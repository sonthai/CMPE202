package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import model.ClassPojo;
import model.FieldPojo;
import model.MethodPojo;
import model.ParamPojo;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import util.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonthai on 2/20/17.
 */
public class MyJavaParser {
    public static String rootDir = "src/main/resources/";
    static int i = 0;
    public static void parse(String sourceDir) {
        CompilationUnit compilationUnit = null;
        List<ClassOrInterfaceDeclaration> classesOrInterafces = null;
        List<FieldPojo> fields = null;
        List<MethodPojo> methods = null;
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
                        FieldPojo fieldPj =  new FieldPojo(field.isPublic(), field.getVariables());
                        fields.add(fieldPj);
                    }
                    pj.setFields(fields);
                    methods = new ArrayList<MethodPojo>();
                    for (MethodDeclaration method: declaration.getMethods()) {
                        if (method.isPublic()) {
                            List<ParamPojo> params = new ArrayList<ParamPojo>();
                            MethodPojo methodPj = new MethodPojo(method.getType().toString(), method.getNameAsString());
                            for (Parameter p: method.getParameters()) {
                                params.add(new ParamPojo(p.getType().toString(), p.getNameAsString()));
                            }
                            methodPj.setParamPojo(params);
                            methods.add(methodPj);
                        }
                    }
                    pj.setMethods(methods);

                    classPojos.add(pj);
                }
            }
            umlConfigGen(classPojos);
            utils.prettyJson(classPojos);
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
        plantUmlSource.append("@startuml");
        for (ClassPojo pj : classPojos) {
            plantUmlSource.append("\nclass ").append(pj.getClassName()).append(" {");
            for (FieldPojo field : pj.getFields()) {
                if (field.getType() == null) continue;
                plantUmlSource.append("\n\t");
                if (field.isPublic()) {
                    plantUmlSource.append("+");
                } else {
                    plantUmlSource.append("-");
                }
                plantUmlSource.append(field.getFieldName()).append(" : ").append(field.getType());
            }

            for (MethodPojo method : pj.getMethods()) {
                plantUmlSource.append("\n\t+").append(method.getMethodName()).append("(");
                StringBuilder sb = new StringBuilder();

                for(ParamPojo p: method.getParamPojo()) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(p.getType()).append(" ").append(p.getParam());
                }
                plantUmlSource.append(sb).append(") : ").append(method.getMethodType());
            }

            plantUmlSource.append("\n}");
        }
        plantUmlSource.append("\n@enduml");
        System.out.println("************");
        System.out.println(plantUmlSource.toString());
        try {
            SourceStringReader reader = new SourceStringReader(plantUmlSource.toString());
            FileOutputStream output = new FileOutputStream(new File(fileName));
            reader.generateImage(output, new FileFormatOption(FileFormat.PNG, false));
            i++;
        } catch (Exception e) {
        }
    }
}
