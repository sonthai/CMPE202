package model;

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

    private String type;
    private String param;
    public ParamPojo(String type, String param) {
        this.type  = type;
        this.param = param;
    }

}
