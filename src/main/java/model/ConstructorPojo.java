package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sonthai on 2/25/17.
 */
public class ConstructorPojo {

    public ConstructorPojo() {
        paramList = new ArrayList<>();
    }

    public List<ParamPojo> getParamList() {
        return paramList;
    }

    public void setParamList(List<ParamPojo> paramList) {
        this.paramList = paramList;
    }

    public void addParams(ParamPojo p) {
        paramList.add(p);
    }

    public String getConstructor() {
        return constructor;
    }

    public void setConstructor(String constructor) {
        this.constructor = constructor;
    }


    private String constructor;

    private List<ParamPojo> paramList;
}
