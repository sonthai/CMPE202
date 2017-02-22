package model;

import java.util.List;

/**
 * Created by sonthai on 2/20/17.
 */
public class MethodPojo {
    public MethodPojo(String type, String name) {
        this.methodType = type;
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
