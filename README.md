# Compiler-LL1_Parser

Implementing a full working LL1 parser for these production rules in BNF.

	
project-declaration  project-def     "." -1
project-def     project-heading        declarations          compound-stmt -2
	project-heading    project      "name"       ";" -3
declarations    const-decl       var-decl       subroutine-decl -4
const-decl   const    const-list    -5  |        -6
const-list       “const-name”      =    “integer-value”   ;   const-list    -7   |          -8
	var-decl   var    var-list    -9 |       -10
var-list     var-item     ;      var-list   -11   |      -12
	var-item     name-list       ":"       int -13
name-list     “var-name”     more-names -14
           	more-names       ,     name-list     -15  |         -16
	subroutine-decl  subroutine-heading      declarations      compound-stmt    “;”  -17 |      -18
	subroutine-heading    subroutine      "name"       ";" -19
	compund-stmt  begin       stmt-list       end -20
stmt-list      statement     ;     stmt-list     -21    |          -22
	statement  ass-stmt -23 | inout-stmt -24 |  if-stmt  -25 | while-stmt  -26 | compound-stmt -27 |   -28
	ass-stmt ” name”     ":="      arith-exp -29
arith-exp  term      arith-exp-prime -30
	arith-exp-prime  add-sign     term     arith-exp-prime   -31    |        -32
	term  factor        term-prime  -33
term-prime    mul-sign       factor       term-prime -34  |        -35
	factor   "("   arith-exp  ")"   -36 |     name-value -37
	name-value   "name"     -38  |        "integer-value" -39
	add-sign   "+"  -40  |     "-" -41
	mul-sign  "*"   -42 |      "/"   -43  |        “%” -44
	inout-stmt  scan    "("    "name"     ")"  -45  |    print   "("   name-value   ")" -46
	if-stmt  if     bool-exp     then     statement     else-part       endif -47
	else-part   else     statement  -48 |    -49
	while-stmt  while      bool-exp     do      statement -50
	bool-exp  name-value       relational-oper        name-value  -51
	relational-oper       "="   -52   |     "|="   -53  |     "<"    -54 |     "=<"  -55 |   ">"  -56 |   "=>" -57
  
 - All “names” and “integer-value” are user defined names and values in the source code.
 - The tokens in bold letters are reserved words.
 - The words between  ""  are terminals (terminal tokens).
 
 1- First I implemented the lexical analayzer to scan the tokens. Also the Finite state automota to validate the integer values and the user defined names.
 2- adding the LL1 parsing table entries.

This can detect any syntax error in this given language.

