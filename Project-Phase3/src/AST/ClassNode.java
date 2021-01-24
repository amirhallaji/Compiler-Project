package AST;
//TODO

import java.util.HashMap;

public class ClassNode extends BaseASTNode {
    //name of method -> signature
    private HashMap<String, String> methodMap = new HashMap<>();

    public ClassNode() {
        super(NodeType.Class_DECLARATION);
    }

    public void putMethodSig(String name, String sig) {
        methodMap.put(name, sig);
    }

    public String getMethodSig(String name) {
        return methodMap.get(name);
    }
}