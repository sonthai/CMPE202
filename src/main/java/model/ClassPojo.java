package model;

import java.util.List;

/**
 * Created by sonthai on 2/20/17.
 */
public class ClassPojo {
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<FieldPojo> getFields() {
        return fields;
    }

    public void setFields(List<FieldPojo> fields) {
        this.fields = fields;
    }

    public List<MethodPojo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodPojo> methods) {
        this.methods = methods;
    }

    public List<String> getExtendedTypes() {
        return extendedTypes;
    }

    public void setExtendedTypes(List<String> extendedTypes) {
        this.extendedTypes = extendedTypes;
    }

    public List<String> getImplementedTypes() {
        return implementedTypes;
    }

    public void setImplementedTypes(List<String> implementedTypes) {
        this.implementedTypes = implementedTypes;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }


    private String className;
    private List<FieldPojo> fields;
    private List<MethodPojo> methods;
    private List<String> extendedTypes;
    private List<String> implementedTypes;
    private boolean isInterface;

}
