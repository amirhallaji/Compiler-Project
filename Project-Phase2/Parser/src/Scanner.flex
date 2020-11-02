import java.io.*;
import java_cup.runtime.*;

%%
%public
%class Laxer
%unicode
%standalone
%cup
%char
%type Symbol

%{
	public Symbol token (int tokenType) {
	    System.out.println(yytext());
	    return new Symbol(tokenType,yytext());
	}
    	public Symbol token (int tokenType , Object value) {
	    System.out.println(yytext());
	    return new Symbol(tokenType,yytext(),value);
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

DecInteger = [0-9]*

HexNumber = "0"[xX][0-9a-fA-F]*

FloatNumber = \d+\.\d*([eE]([+-])?\d+)?

string = \"[^(\\n|\\r)]~\"






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
    "‫‪dtoi‬‬"               {return token(sym.‫‪DTOI‬‬);}
    "‫‪itod‬‬"               {return token(sym.‫‪ITOD‬‬);}
    "‫‪btoi‬‬"               {return token(sym.‫‪BTOI‬‬);}
    "‫‪itob‬‬"               {return token(sym.‫‪ITOB‬‬);}
    "‫‪private‬‬"            {return token(sym.‫‪PRIVATE);}
    "‫‪protected‬‬"          {return token(sym.‫‪PROTECTED‬‬);}
    "‫‪public‬‬"             {return token(sym.‫‪PUBLIC‬‬);}

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
	
    {Identifier}         { return token(sym.ID , new String(yytext())); }
    {WhiteSpace}         {/*ignore*/}
    {DecInteger}         { return token(sym.INT,new Integer(yytext())); }
    {HexNumber}          { return token(sym.HEX , new String(yytext()));}
    {FloatNumber}        { return token(sym.FLOAT , new Float(yytext()));}
    {string}             { return token(sym.STRING, new String(yytext()));}
    {Comment}            {/*ignore*/} 
}
