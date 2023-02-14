package com.example.compilerprojfx.model;

public class Lexeme {

    private Token token;
    private int line;
    private String value;

    public Lexeme(Token token, int line, String value) {
        this.token = token;
        this.line = line;
        this.value = value;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Lexeme{" +
                "token=" + token +
                ", line=" + line +
                ", value='" + value + '\'' +
                '}';
    }
}
