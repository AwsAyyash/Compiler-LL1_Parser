package com.example.compilerprojfx.model;

public class SyntaxErrorThrower extends Exception{

    public SyntaxErrorThrower(String message) {
        super(message);
    }
}
