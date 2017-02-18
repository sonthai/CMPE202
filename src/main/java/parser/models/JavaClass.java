package parser.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by sonthai on 2/17/17.
 */
public class JavaClass {
    private String className;
    private final Map<String, Method> methods = new HashMap<String, Method>();
    private final Set<String> classVariables = new HashSet<String>();
    private final Set<String> labels = new HashSet<String>();

    public final String className() {
        return className;
    }

    public final JavaClass className(final String className) {
        this.className = className;
        return this;
    }

    public Set<String> classVariables() {
        return classVariables;
    }

    public JavaClass classVariable(final String classVariable) {
        if (classVariable.trim().length() > 0) {
            String [] list =  classVariable.split(" ");
            if (list.length == 3  &&!this.methods.containsKey(list[2])) {
                this.methods.put(list[2], new Method(list[1], list[0], list[2]));
            } else if (list.length < 3) {
                System.out.println(list.toString());
            }
            this.classVariables.add(classVariable);
        }
        return this;
    }

    public Set<String> labels() {
        return labels;
    }

    public JavaClass label(final String label) {
        labels.add(label);
        return this;
    }

    public Map<String, Method> methods() {
        return methods;
    }

    public JavaClass method(final Method method) {
        //methods.add(method);
        return this;
    }
}
