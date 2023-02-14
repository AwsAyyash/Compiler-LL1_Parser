package com.example.compilerprojfx.controler;

import com.example.compilerprojfx.model.Lexeme;
import com.example.compilerprojfx.model.SyntaxErrorThrower;
import com.example.compilerprojfx.model.Token;

import java.util.*;

public class LexicalAnalyzer {

    public Map<String, Token> keywordsAndOperatorsMap;

    public List<Lexeme> lexemes;

    public static void initKeywords() {

    }

    public LexicalAnalyzer() {
        initKeywords();

        keywordsAndOperatorsMap = new HashMap<>();

        keywordsAndOperatorsMap.put("project", Token.KEYWORD);
        keywordsAndOperatorsMap.put("const", Token.KEYWORD);
        keywordsAndOperatorsMap.put("var", Token.KEYWORD);
        keywordsAndOperatorsMap.put("int", Token.KEYWORD);
        keywordsAndOperatorsMap.put("subroutine", Token.KEYWORD);
        keywordsAndOperatorsMap.put("begin", Token.KEYWORD);
        keywordsAndOperatorsMap.put("end", Token.KEYWORD);
        keywordsAndOperatorsMap.put("scan", Token.KEYWORD);
        keywordsAndOperatorsMap.put("print", Token.KEYWORD);
        keywordsAndOperatorsMap.put("if", Token.KEYWORD);
        keywordsAndOperatorsMap.put("then", Token.KEYWORD);
        keywordsAndOperatorsMap.put("endif", Token.KEYWORD);
        keywordsAndOperatorsMap.put("else", Token.KEYWORD);
        keywordsAndOperatorsMap.put("while", Token.KEYWORD);
        keywordsAndOperatorsMap.put("do", Token.KEYWORD);


        keywordsAndOperatorsMap.put(".", Token.DOT);
        keywordsAndOperatorsMap.put(";", Token.SEMICOLON);
        keywordsAndOperatorsMap.put(":", Token.COLON);
        keywordsAndOperatorsMap.put(",", Token.COMMA);
        keywordsAndOperatorsMap.put(":=", Token.ASSIGNMENT_OPERATOR);

        keywordsAndOperatorsMap.put("+", Token.PLUS);
        keywordsAndOperatorsMap.put("-", Token.MINUS);
        keywordsAndOperatorsMap.put("*", Token.TIMES);
        keywordsAndOperatorsMap.put("/", Token.DIVIDE);
        keywordsAndOperatorsMap.put("%", Token.MOD);

        keywordsAndOperatorsMap.put("(", Token.LEFT_PARENTHESIS);
        keywordsAndOperatorsMap.put(")", Token.RIGHT_PARENTHESIS);

        keywordsAndOperatorsMap.put("=", Token.EQUAL);
        keywordsAndOperatorsMap.put("|=", Token.NOT_EQUALS);
        keywordsAndOperatorsMap.put("<", Token.LOWER_THAN);
        keywordsAndOperatorsMap.put("=<", Token.LOWER_OR_EQUALS);
        keywordsAndOperatorsMap.put(">", Token.GREATER_THAN);
        keywordsAndOperatorsMap.put("=>", Token.GREATER_OR_EQUALS);
    }

    // key=line number, value= the line itself
    public List<Lexeme> analyzeCode(String[] linesArray) throws SyntaxErrorThrower {

        lexemes = new LinkedList<>();
        for (int i = 0; i < linesArray.length; i++) {
            analyzeLine(linesArray[i].trim(), i + 1);
        }


        return lexemes;
    }

    private void analyzeLine(String line, int lineNumber) throws SyntaxErrorThrower {

        line = cleanLineString(line);

        FiniteStateAutomata finiteStateAutomata = new FiniteStateAutomata();

        String[] word = line.split(" ");
        for (String str : word) {
            str = str.trim();
            if (str.equals(""))
                continue;

            if (keywordsAndOperatorsMap.containsKey(str))
                lexemes.add(new Lexeme(keywordsAndOperatorsMap.get(str), lineNumber, str));
            else {
                Token token = finiteStateAutomata.evaluate(str);
                if (token == Token.INVALID)
                    throw new SyntaxErrorThrower("SyntaxError: Token='" + str + "', at Line#='" + lineNumber + "'");
                else
                    lexemes.add(new Lexeme(token, lineNumber, str));

            }
        }

    }

    private String cleanLineString(String line) {

        line = line.replace(";", " ; ").trim();
        line = line.replace(",", " , ").trim();
        line = line.replace(".", " . ").trim();

        line = line.replace("(", " ( ").trim();
        line = line.replace(")", " ) ").trim();
        line = line.replace(":=", " := ").trim();


        //line = line.replace("+", " + ").trim();
        //line = line.replace("-", " - ").trim();
        line = line.replace("*", " * ").trim();
        line = line.replace("/", " / ").trim();
        line = line.replace("%", " % ").trim();


        line = line.replace("|=", " |= ").trim();
        line = line.replace("=<", " =< ").trim();
        line = line.replace("=>", " => ").trim();


        HashSet<Character> mapBeforeEqualSymbols = new HashSet<>();
        mapBeforeEqualSymbols.add('|');
        mapBeforeEqualSymbols.add(':');

        HashSet<Character> mapAfterEqualSymbols = new HashSet<>();
        mapAfterEqualSymbols.add('>');
        mapAfterEqualSymbols.add('<');


        HashSet<Character> mapAfterColonSymbols = new HashSet<>();
        mapAfterColonSymbols.add('=');


        HashSet<Character> mapBeforeGreaterAndLessThanSymbols = new HashSet<>();
        mapBeforeGreaterAndLessThanSymbols.add('=');


        for (int i = 0; i < line.length(); i++) {
            char currChar = line.charAt(i);
            char prevChar = line.charAt(Math.max(0, i - 1));
            char nextChar = line.charAt(Math.min(line.length() - 1, i + 1));

            if (currChar == '=') {


                if (!mapBeforeEqualSymbols.contains(prevChar)) {
                    if (!mapAfterEqualSymbols.contains(nextChar)) {

                        line = line.substring(0, i) + " = " + line.substring(i + 1);
                        i += 2;
                    }
                }


            }

            if (currChar == ':') {


                if (!mapAfterColonSymbols.contains(nextChar)) {


                    line = line.substring(0, i) + " : " + line.substring(i + 1);
                    i += 2;
                }


            }

            if (currChar == '>') {


                if (!mapBeforeGreaterAndLessThanSymbols.contains(prevChar)) {


                    line = line.substring(0, i) + " > " + line.substring(i + 1);
                    i += 2;
                }


            }

            if (currChar == '<') {


                if (!mapBeforeGreaterAndLessThanSymbols.contains(prevChar)) {


                    line = line.substring(0, i) + " < " + line.substring(i + 1);
                    i += 2;
                }


            }


            ////////////////


            HashSet<Character> mapBeforePositiveIntOrNegativeIntSymbols = new HashSet<>();
            mapBeforePositiveIntOrNegativeIntSymbols.add('=');
            mapBeforePositiveIntOrNegativeIntSymbols.add('<');
            mapBeforePositiveIntOrNegativeIntSymbols.add('>');
            mapBeforePositiveIntOrNegativeIntSymbols.add('(');

            mapBeforePositiveIntOrNegativeIntSymbols.add('*');
            mapBeforePositiveIntOrNegativeIntSymbols.add('/');
            mapBeforePositiveIntOrNegativeIntSymbols.add('%');
            mapBeforePositiveIntOrNegativeIntSymbols.add('+');
            mapBeforePositiveIntOrNegativeIntSymbols.add('-');


            int counter = 0;
            if (currChar == '+') {

                // counter = 0;
                if (prevChar == ' ') {
                    counter = 1;
                    while (prevChar == ' ') {

                        prevChar = line.charAt(Math.max(0, i - 1 - counter));
                        counter++;
                    }
                }

                if (!mapBeforePositiveIntOrNegativeIntSymbols.contains(prevChar)) {


                    line = line.substring(0, i) + " + " + line.substring(i + 1);
                    i = i + 2 + counter;
                }


            }

            if (currChar == '-') {

                if (prevChar == ' ') {
                    counter = 1;
                    while (prevChar == ' ') {

                        prevChar = line.charAt(Math.max(0, i - 1 - counter));
                        counter++;
                    }
                }


                if (!mapBeforePositiveIntOrNegativeIntSymbols.contains(prevChar)) {


                    line = line.substring(0, i) + " - " + line.substring(i + 1);
                    i = i + 2 + counter;
                }


            }

            /////////////
        }

        while (line.contains("  "))
            line = line.replace("  ", " ").trim();


        return line;
    }


}
