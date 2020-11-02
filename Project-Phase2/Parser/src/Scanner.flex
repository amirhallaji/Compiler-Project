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
    "string"             {return token(sym.string);}
    "class"              {return token(sym.class);}
    "interface"          {return token(sym.interface);}
    "null"               {return token(sym.null);}
    "this"               {return token(sym.this);}
    "extends"            {return token(sym.extends);}
    "implements"         {return token(sym.implements);}
    "for"                {return token(sym.for);}
    "while"              {return token(sym.while);}
    "if"                 {return token(sym.if);}
    "else"               {return token(sym.else);}
    "return"             {return token(sym.return);}
    "break"              {return token(sym.break);}
    "new"                {return token(sym.new);}
    "NewArray"           {return token(sym.NewArray);}
    "Print"              {return token(sym.Print);}
    "ReadInteger"        {return token(sym.ReadInteger);}
    "ReadLine"           {return token(sym.ReadLine);}
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
