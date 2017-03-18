package model;

/**
 * Created by sonthai on 2/28/17.
 */
public class PlantUMLPojo {
    private StringBuilder umlBodyString;
    private StringBuilder plantUmlString;
    private StringBuilder startTag;
    private StringBuilder endTag;
    private StringBuilder extraUmlSource;

    public PlantUMLPojo() {
        umlBodyString = new StringBuilder();
        startTag = new StringBuilder();
        endTag = new StringBuilder();
        extraUmlSource = new StringBuilder();
        plantUmlString = new StringBuilder();
    }

    public StringBuilder getumlBodyString() {
        return umlBodyString;
    }

    public void setumlBodyString(StringBuilder umlBodyString) {
        this.umlBodyString = umlBodyString;
    }

    public void createStartTag() {
        startTag.append("@startuml\n");
    }

    public void createEndTag() {
        endTag.append("\n@enduml");
    }
    
    public void appendNewLine() {
    	umlBodyString.append("\n");
    }
    
    public void appendInterface(String interfaceName) {
    	umlBodyString.append("\n").append("interface ").append(interfaceName);
    }
    
    public void appendClass(String className) {
    	umlBodyString.append("\n").append("class ").append(className);
    }
    
    public void appendBracket(String bracket) {
    	umlBodyString.append(bracket);
    }

    public void buildReferenceRelationship(String referredClass, String referingClass, String notation, String option) {
        extraUmlSource.append(referredClass).append(notation).append(referingClass);
        if (option != null) {
        	extraUmlSource.append(option);
        }
        extraUmlSource.append("\n");
    }
    
    public void buildMultiplicity(String class1, String mult1, String class2, String mult2) {
    	extraUmlSource.append(class1).append(addQuote(mult1))
    					.append(" -- ")
    					.append(addQuote(mult2)).append(class2)
    					.append("\n");
    }
    
    public void buildField(String modifier, String fieldName, String fieldType) {
    	umlBodyString.append("\n\t").append(modifier)
    				.append(fieldName).append(" : ").append(fieldType);
    }
    
    public void buildConstructor(String constructorName, String paramList) {
    	umlBodyString.append("\n\t+").append(constructorName).append("(").append(paramList).append(")");
    }
    
    public void buildMethod(String methodName, String paramList, String methodType) {
    	umlBodyString.append("\n\t+").append(methodName).append("(").append(paramList).append("): ").append(methodType);
    }
    
    private String addQuote(String mult) {
    	if (mult.trim().length() == 0 ) {
    		return mult;
    	} else {
    		StringBuilder sb = new StringBuilder();
    		sb.append(" \"").append(mult).append("\" ");
    		return sb.toString();
    	}
    }

    public String buildumlBodyString() {
        return plantUmlString.append(startTag).append(umlBodyString).append("\n")
        		.append(extraUmlSource)
        		.append(endTag)
        		.toString();
    }


}
