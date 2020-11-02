%%
%public
%class Laxer
%unicode
%standalone

%{
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
    "void"               {out.append("void\n");}
    "int"                {out.append("int\n");}  
    "double"             {out.append("double\n");}
    "bool"               {out.append("bool\n");}
    "string"             {out.append("string\n");}
    "class"              {out.append("class\n");}
    "interface"          {out.append("interface\n");}
    "null"               {out.append("null\n");}
    "this"               {out.append("this\n");}
    "extends"            {out.append("extends\n");}
    "implements"         {out.append("implements\n");}
    "for"                {out.append("for\n");}
    "while"              {out.append("while\n");}
    "if"                 {out.append("if\n");}
    "else"               {out.append("else\n");}
    "return"             {out.append("return\n");}
    "break"              {out.append("break\n");}
    "new"                {out.append("new\n");}
    "NewArray"           {out.append("NewArray\n");}
    "Print"              {out.append("Print\n");}
    "ReadInteger"        {out.append("ReadInteger\n");}
    "ReadLine"           {out.append("ReadLine\n");}
    "‫‪dtoi‬‬"               {out.append("‫‪dtoi‬‬\n");}
    "‫‪itod‬‬"               {out.append("‫‪itod‬‬\n");}
    "‫‪btoi‬‬"               {out.append("‫‪btoi‬‬\n");}
    "‫‪itob‬‬"               {out.append("‫‪itob‬‬\n");}
    "‫‪private‬‬"            {out.append("‫‪private\n");}
    "‫‪protected‬‬"          {out.append("‫‪protected‬‬\n");}
    "‫‪public‬‬"             {out.append("‫‪public‬‬\n");}

    "false"              {out.append("T_BOOLEANLITERAL false\n");}
    "true"               {out.append("T_BOOLEANLITERAL true\n");}

    "=="				 {out.append("==\n");}
    "!="				 {out.append("!=\n");}
    "<="				 {out.append("<=\n");}
    "<"					 {out.append("<\n");}
    ">"					 {out.append(">\n");}
    ">="				 {out.append(">=\n");}
	"="					 {out.append("=\n");}
	"&&"				 {out.append("&&\n");}
	"!"				     {out.append("!\n");}
	";"		    		 {out.append(";\n");}
	"/"					 {out.append("/\n");}
	"*"					 {out.append("*\n");}
	"+"					 {out.append("+\n");}
	"-"			    	 {out.append("-\n");}
	"%"					 {out.append("%\n");}
	","				     {out.append(",\n");}
	"["				     {out.append("[\n");}
	"."				     {out.append(".\n");}
	"]"				     {out.append("]\n");}
	"("				     {out.append("(\n");}
	")"					 {out.append(")\n");}
	")"					 {out.append(")\n");}
	"{"					 {out.append("{\n");}
	"}"					 {out.append("}\n");}
	"||"				 {out.append("||\n");}
	
    {Identifier}         { out.append("T_ID " + yytext() +"\n"); }
    {WhiteSpace}         {/*ignore*/}
    {DecInteger}         { out.append("T_INTLITERAL " + yytext() +"\n"); }
    {HexNumber}          { out.append("T_INTLITERAL " + yytext() +"\n"); }
    {FloatNumber}        { out.append("T_DOUBLELITERAL "+ yytext() +"\n");}
    {string}             { out.append("T_STRINGLITERAL " + yytext() +"\n");}
    {Comment}            {/*ignore*/} 
}