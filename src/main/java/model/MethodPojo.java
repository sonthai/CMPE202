package model;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

import java.util.List;

/**
 * Created by sonthai on 2/20/17.
 */
public class MethodPojo {
    public MethodPojo(Type type, String name) {
        this.methodType = type.toString();
        this.methodName = name;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<ParamPojo> getParamPojo() {
        return paramPojo;
    }

    public void setParamPojo(List<ParamPojo> paramPojo) {
        this.paramPojo = paramPojo;
    }

    private String methodType;
    private String methodName;
    private List<ParamPojo> paramPojo;




}
