import java.io.*;
import java_cup.runtime.*;

%%
%public
%class Lexer
%unicode
%cup
%char
%type Symbol
%standalone
%{
	public Symbol token (int tokenType) {
	 //   System.out.println(yytext());
	    return new Symbol(tokenType,yytext());
	}
    	public Symbol token (int tokenType , Object value) {
	    System.out.println(yytext());
	    return new Symbol(tokenType , value);
	}
    StringBuffer out = new StringBuffer();
%}

/* comments */
InputCharacter = [^\r\n]
Comment = {TraditionalComment} | {EndOfLineComment} 
TraditionalComment= "/*"~"*/" 
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

Identifier = [:jletter:][:jletterdigit:]*

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]

DecInteger = [0-9][0-9]*

HexNumber = "0"[xX][0-9a-fA-F]*

FloatNumber = \d+\.\d*([eE]([+-])?\d+)?

string = \"[^(\\n|\\r)]~\"

Brackets = "\[" {WhiteSpace}*"\]"






%%
<YYINITIAL>{
    "void"               {return token(sym.VOID);}
    "int"                {return token(sym.INT);}  
    "double"             {return token(sym.DOUBLE);}
    "bool"               {return token(sym.BOOL);}
    "string"             {return token(sym.STRING);}
    "class"              {return token(sym.CLASS);}
    "interface"          {return token(sym.INTERFACE);}
    "null"               {return token(sym.NULL);}
    "this"               {return token(sym.THIS);}
    "extends"            {return token(sym.EXTENDS);}
    "implements"         {return token(sym.IMPLEMENTS);}
    "for"                {return token(sym.FOR);}
    "while"              {return token(sym.WHILE);}
    "if"                 {return token(sym.IF);}
    "else"               {return token(sym.ELSE);}
    "return"             {return token(sym.RETURN);}
    "break"              {return token(sym.BREAK);}
    "new"                {return token(sym.NEW);}
    "NewArray"           {return token(sym.NEWARRAY);}
    "Print"              {return token(sym.PRINT);}
    "ReadInteger"        {return token(sym.READINTEGER);}
    "ReadLine"           {return token(sym.READLINE);}
    "continue"           {return token(sym.CONTINUE);}

    "dtoi"               {return token(sym.DTOI);}
    "itod"               {return token(sym.ITOD);}
    "btoi"               {return token(sym.BTOI);}
    "itob"               {return token(sym.ITOB);}
    "private"            {return token(sym.PRIVATE);}
    "protected"          {return token(sym.PROTECTED);}
    "public"             {return token(sym.PUBLIC);}

    "false"              {return token(sym.FALSE);}
    "true"               {return token(sym.TRUE);}

    "=="				 {return token(sym.EQUAL);}
    "!="				 {return token(sym.NOTEQUAL);}
    "<="				 {return token(sym.LESSEQUAL);}
    "<"					 {return token(sym.LESS);}
    ">"					 {return token(sym.GREATER);}
    ">="				 {return token(sym.GREATEREQUAL);}
	"="					 {return token(sym.ASSIGN);}
	"&&"				 {return token(sym.AND);}
	"!"				     {return token(sym.NOT);}
	";"		    		 {return token(sym.COLON);}
	"/"					 {return token(sym.DIV);}
	"*"					 {return token(sym.PROD);}
	"+"					 {return token(sym.ADD);}
	"-"			    	 {return token(sym.MINUS);}
	"%"					 {return token(sym.MOD);}
	","				     {return token(sym.COMMA);}
	"["				     {return token(sym.LBRACK);}
	"."				     {return token(sym.DOT);}
	"]"				     {return token(sym.RBRACK);}
	"("				     {return token(sym.LBRACES);}
	")"					 {return token(sym.RBRACES);}
	"{"					 {return token(sym.LCURLY);}
	"}"					 {return token(sym.RCURLY);}
	"||"				 {return token(sym.OR);}
//	"[]"				 {return token(sym.LRBRACK);}


    {Identifier}         { return token(sym.ID , new String(yytext())); }
    {WhiteSpace}         {/*ignore*/}
    {DecInteger}         { return token(sym.INTCONST, new Integer(yytext())); }
    {HexNumber}          { return token(sym.HEXCONST , new String(yytext()));}
    {FloatNumber}        { return token(sym.DOUBLECONST , new Float(yytext()));}
    {string}             { return token(sym.STRINGCONST, new String(yytext()));}
    {Comment}            {/*ignore*/}
    {Brackets}           {return token(sym.LRBRACK);}
}


/* error fallback */
[^] { throw new Error("Illegal character <" + yytext() + ">"); }