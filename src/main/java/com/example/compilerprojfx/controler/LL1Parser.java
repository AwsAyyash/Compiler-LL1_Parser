package com.example.compilerprojfx.controler;

import com.example.compilerprojfx.controler.LexicalAnalyzer;
import com.example.compilerprojfx.model.Lexeme;
import com.example.compilerprojfx.model.SyntaxErrorThrower;
import com.example.compilerprojfx.model.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class LL1Parser {



    /*

    *********************************The production rules in BNF****************************************************

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
	compound-stmt  begin       stmt-list       end -20
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

    *************************************************************************************

     */


    public Map<Integer, String> productionRules = new HashMap<>();

    public Map<String, Map<String, Integer>> LL1Table = new HashMap<>();

    public LL1Parser() {

        productionRules.put(1, "project-def ."); //1,
        productionRules.put(2, "project-heading declarations compound-stmt"); //2,

        productionRules.put(3, "project name ;");//3,
        productionRules.put(4, "const-decl var-decl subroutine-decl");// 4,
        productionRules.put(5, "const const-list");// 5,
        productionRules.put(6, ""); // lambda

        productionRules.put(7, "name = integer-value ; const-list");

        productionRules.put(8, ""); // lambda
        productionRules.put(9, "var var-list"); // lambda
        productionRules.put(10, ""); // lambda


        productionRules.put(11, "var-item ; var-list"); // lambda
        productionRules.put(12, ""); // lambda
        productionRules.put(13, "name-list : int");
        productionRules.put(14, "name more-names");
        productionRules.put(15, ", name-list");

        productionRules.put(16, ""); // lambda
        productionRules.put(17, "subroutine-heading declarations compound-stmt ;");
        productionRules.put(18, ""); // lambda

        productionRules.put(19, "subroutine name ;");
        productionRules.put(20, "begin stmt-list end");

        productionRules.put(21, "statement ; stmt-list");
        productionRules.put(22, "");

        productionRules.put(23, "ass-stmt");
        productionRules.put(24, "inout-stmt");
        productionRules.put(25, "if-stmt");
        productionRules.put(26, "while-stmt");
        productionRules.put(27, "compound-stmt");

        productionRules.put(28, "");


        productionRules.put(29, "name := arith-exp");
        productionRules.put(30, "term arith-exp-prime");
        productionRules.put(31, "add-sign term arith-exp-prime");
        productionRules.put(32, "");

        productionRules.put(33, "factor term-prime");
        productionRules.put(34, "mul-sign factor term-prime");

        productionRules.put(35, "");


        productionRules.put(36, "( arith-exp )");
        productionRules.put(37, "name-value");
        productionRules.put(38, "name");
        productionRules.put(39, "integer-value");

        productionRules.put(40, "+");
        productionRules.put(41, "-");
        productionRules.put(42, "*");
        productionRules.put(43, "/");
        productionRules.put(44, "%");

        productionRules.put(45, "scan ( name )");
        productionRules.put(46, "print ( name-value )");
        productionRules.put(47, "if bool-exp then statement else-part endif");
        productionRules.put(48, "else statement");

        productionRules.put(49, "");

        productionRules.put(50, "while bool-exp do statement");
        productionRules.put(51, "name-value relational-oper name-value");
        productionRules.put(52, "=");
        productionRules.put(53, "|=");
        productionRules.put(54, "<");
        productionRules.put(55, "=<");
        productionRules.put(56, ">");
        productionRules.put(57, "=>");


        initTable();
    }

    private void initTable() {


        LL1Table.put("project-declaration", Map.of("project", 1));
        LL1Table.put("project-def", Map.of("project", 2));
        LL1Table.put("project-heading", Map.of("project", 3));


        LL1Table.put("declarations", Map.of("const", 4, "var", 4, "subroutine", 4, "begin", 4));

        LL1Table.put("const-decl", Map.of("const", 5, "var", 6, "subroutine", 6, "begin", 6));

        LL1Table.put("const-list", Map.of("name", 7, "var", 8, "subroutine", 8, "begin", 8));

        LL1Table.put("var-decl", Map.of("var", 9, "subroutine", 10, "begin", 10));

        LL1Table.put("var-list", Map.of("name", 11, "subroutine", 12, "begin", 12));

        LL1Table.put("var-item", Map.of("name", 13));

        LL1Table.put("name-list", Map.of("name", 14));
        LL1Table.put("more-names", Map.of(":", 16, ",", 15));
        LL1Table.put("subroutine-decl", Map.of("subroutine", 17, "begin", 18));
        LL1Table.put("subroutine-heading", Map.of("subroutine", 19));

        LL1Table.put("compound-stmt", Map.of("begin", 20));

        LL1Table.put("stmt-list", Map.of("name", 21, ";", 21, "begin", 21,
                "end", 22, "scan", 21, "print", 21, "if", 21, "endif", 21,
                "else", 21, "while", 21));

        LL1Table.put("statement", Map.of("name", 23, ";", 28, "begin", 27,
                "scan", 24, "print", 24, "if", 25, "endif", 28,
                "else", 28, "while", 26));

        LL1Table.put("ass-stmt", Map.of("name", 29));

        LL1Table.put("arith-exp", Map.of("name", 30, "integer-value", 30, "(", 30));


        LL1Table.put("arith-exp-prime", Map.of(";", 32, ")", 32, "+", 31, "-", 31
                , "endif", 32, "else", 32));
        LL1Table.put("term", Map.of("name", 33, "integer-value", 33, "(", 33));
        LL1Table.put("term-prime", Map.of(";", 35, "+", 35, "-", 35, ")", 35, "*", 34, "/", 34, "%", 34
                , "endif", 35, "else", 35));

        LL1Table.put("factor", Map.of("name", 37, "integer-value", 37, "(", 36));
        LL1Table.put("name-value", Map.of("name", 38, "integer-value", 39));
        LL1Table.put("add-sign", Map.of("+", 40, "-", 41));
        LL1Table.put("mul-sign", Map.of("*", 42, "/", 43, "%", 44));
        LL1Table.put("inout-stmt", Map.of("scan", 45, "print", 46));
        LL1Table.put("if-stmt", Map.of("if", 47));
        LL1Table.put("else-part", Map.of("endif", 49, "else", 48));
        LL1Table.put("while-stmt", Map.of("while", 50));

        LL1Table.put("bool-exp", Map.of("name", 51, "integer-value", 51));


        LL1Table.put("relational-oper", Map.of("=", 52, "|=", 53, "<", 54, "=<", 55, ">", 56
                , "=>", 57));

    }

    private Token getTokenType(String variable) {

        if (variable.equals("name"))
            return Token.NAME;

        else if (variable.equals("integer-value"))
            return Token.INTEGER_VALUE;
        else
            return new LexicalAnalyzer().keywordsAndOperatorsMap.get(variable);


    }

    public String parse(List<Lexeme> lexemes) throws SyntaxErrorThrower {

        Stack<String> stack = new Stack<>();
        stack.push("project-declaration");


        int iterator = 0;
        while (!stack.empty() && iterator < lexemes.size()) {

            String stackTop = stack.peek().trim();
            Lexeme currentToken = lexemes.get(iterator);

            // non terminal
            if (LL1Table.containsKey(stackTop)) {
                stack.pop();
                String productionRule;
                if (currentToken.getToken().equals(Token.NAME))

                    productionRule = productionRules.get(LL1Table.get(stackTop).get("name"));
                else if (currentToken.getToken().equals(Token.INTEGER_VALUE))
                    productionRule = productionRules.get(LL1Table.get(stackTop).get("integer-value"));

                else
                    productionRule = productionRules.get(LL1Table.get(stackTop).get(currentToken.getValue()));


                // ERROR Entry in the parsing table! invalid token
                if (productionRule == null)
                    throw new SyntaxErrorThrower("SyntaxError: Token='" + currentToken.getValue() +
                            "', at Line#='" + currentToken.getLine() + "'");

                if (productionRule.equals("")) // lambda rule.
                    continue;
                String[] nonTerminals = productionRule.split(" ");
                for (int i = nonTerminals.length - 1; i >= 0; i--)
                    stack.push(nonTerminals[i]);


            } else { // terminal


                if (getTokenType(stackTop).equals(currentToken.getToken())) {
                    stack.pop();
                    iterator++;

                } else {
                    throw new SyntaxErrorThrower("SyntaxError: Token='" + currentToken.getValue() +
                            "', at Line#='" + currentToken.getLine() + "'");

                }

            }



        }
        if (iterator < lexemes.size()) {


            throw new SyntaxErrorThrower("SyntaxError: Token='" + lexemes.get(iterator).getValue() +
                    "', at Line#='" + lexemes.get(iterator).getLine() + "'");

        } else if (!stack.empty()) {

            throw new SyntaxErrorThrower("SyntaxError: LastValidInputToken='" + lexemes.get(iterator - 1).getValue() +
                    "',\nCurrentStackRuleToken='" + stack.peek() + "'" +
                    ", \nAt Line#='" + lexemes.get(iterator - 1).getLine() + "'");



        } else
            return "Parsed successfully!";

    }
}
