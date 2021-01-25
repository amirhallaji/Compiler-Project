package codegen;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple symbol table implementation.
 */

class SymbolTable implements Symbol {
    private ArrayList<Scope> scopes = new ArrayList<>();
    private Scope currentScope;

    void enterScope(String id) {
        Scope newScope = new Scope(id);
        scopes.add(newScope);
        currentScope = newScope;

    }

    void leaveCurrentScope() {
        if (currentScope != null)
            scopes.remove(currentScope);
    }

    void leaveScopeType(String id) {
        for (Scope scope : scopes) {
            if (scope.getName().equals(id)) {
                scopes.remove(currentScope);
                if (scopes.size() > 1)
                    currentScope = scopes.get(scopes.size() - 1);
                break;
            }
        }
    }

    void put(String id, SymbolInfo si) throws Exception {
        if (currentScope.getVariables().containsKey(id)) {
            throw new Exception("current scope already contains an entry for " + id);
        }

        currentScope.getVariables().put(id, si);
    }

    Symbol get(String id) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).getVariables().containsKey(id))
                return scopes.get(i).getVariables().get(id);
        }
        return currentScope.getVariables().get(id);
    }

    String getCurrentScopeName() {
        return currentScope.getName();
    }

    boolean contains(String id) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).getVariables().containsKey(id))
                return true;
        }
        return false;
    }
}