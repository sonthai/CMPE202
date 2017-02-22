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
        if (varType instanceof PrimitiveType || varType instanceof ArrayType) {
            this.type = varType.toString();
        } else if (varType instanceof ClassOrInterfaceType) {

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

    private boolean isPublic;
    private String type;
    private String fieldName;

}
