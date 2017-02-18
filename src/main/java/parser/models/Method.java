package parser.models;

/**
 * Created by sonthai on 2/17/17.
 */
public class Method {
    public Method(String modifier, String type, String varName) {
        this.modifier = modifier;
        this.type = type;
        this.varName = varName;
    }

    public Method() {}

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    private String modifier;
    private String type;
    private String varName;
}
