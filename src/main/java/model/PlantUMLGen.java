package model;

/**
 * Created by sonthai on 2/28/17.
 */
public class PlantUMLGen {
    private StringBuilder umlString;
    private StringBuilder startTag;
    private StringBuilder endTag;
    private StringBuilder extraUmlSource;

    public PlantUMLGen() {
        umlString = new StringBuilder();
    }

    public StringBuilder getUmlString() {
        return umlString;
    }

    public void setUmlString(StringBuilder umlString) {
        this.umlString = umlString;
    }

    public void createStartTag() {
        startTag.append("@startuml");
    }

    public void createEndTag() {
        endTag.append("\n@enduml");
    }

    public void appendExtraUmlSource(String parentClass, String childClass, String notation) {
        extraUmlSource.append(parentClass).append(notation).append(childClass);
    }

    public String buildUmlString() {
        return umlString.append(startTag).append(extraUmlSource).append(endTag).toString();
    }

    public void setModiferNotation(String modifier) {
        umlString.append(modifier);
    }
}
