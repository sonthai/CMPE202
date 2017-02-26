package model;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.*;

/**
 * Created by sonthai on 2/20/17.
 */
public class FieldPojo {
    public FieldPojo(boolean isPublic, NodeList<VariableDeclarator> var) {
        this.isPublic = isPublic;
        Type varType = var.get(0).getType();
        if (varType instanceof PrimitiveType) {
            this.type = varType.toString();
            isPrimitiveType = true;
        } else if (varType instanceof  ArrayType) {
            if (((ArrayType) varType).getComponentType() instanceof  PrimitiveType) {
                isPrimitiveType = true;
            } else {
                isPrimitiveType = false;
            }
            this.type = ((ArrayType) varType).getComponentType().toString();
            multiplicity = "*";
        } else if (varType instanceof ClassOrInterfaceType) {
            isPrimitiveType = false;
            if (varType.getChildNodes().size() >  0) {
                multiplicity = "*";
                this.type = varType.getChildNodes().get(0).toString();
            } else {
                this.type = ((ClassOrInterfaceType) varType).getNameAsString();
            }
        }

        this.fieldName = var.get(0).getNameAsString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }

    public boolean isPrimitiveType() {
        return isPrimitiveType;
    }

    public void setPrimitiveType(boolean primitiveType) {
        isPrimitiveType = primitiveType;
    }


    private boolean isPublic;
    private String type;
    private String fieldName;
    private String multiplicity = "1";
    private boolean isPrimitiveType;

}
