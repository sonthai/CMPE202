package model;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

/**
 * Created by sonthai on 2/20/17.
 */
public class ParamPojo {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public boolean isPrimitiveType() {
        return isPrimitiveType;
    }

    public void setPrimitiveType(boolean primitiveType) {
        isPrimitiveType = primitiveType;
    }

    private String type;
    private String param;
    private boolean isPrimitiveType;

    public ParamPojo(Type type, String param) {
        if (type instanceof ClassOrInterfaceType) {
            isPrimitiveType = false;
        } else {
            isPrimitiveType = true;
        }
        this.type  = type.toString();
        this.param = param;

    }

}
