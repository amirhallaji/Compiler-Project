package Vtable;

import codegen.SymbolInfo;

public class Field {
    String name;
    SymbolInfo symbolInfo;
    AccessMode accessMode = AccessMode.Public;
    ClassDecaf classDecaf = null;
}
