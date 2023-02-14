package com.example.compilerprojfx.controler;

import com.example.compilerprojfx.model.State;
import com.example.compilerprojfx.model.Token;

import java.util.HashMap;
import java.util.Map;

public class FiniteStateAutomata {

    private Map<State, Token> finalStates;

    public FiniteStateAutomata() {
        finalStates = new HashMap<>();
        // If it ends in this state, then it is a valid identifier name, and it is a user defined name.
        finalStates.put(State.Q1, Token.NAME);
        // If it ends in this state, then it is a valid number, and it is an integer_value.
        finalStates.put(State.Q2, Token.INTEGER_VALUE);
    }

    private State executeTransition(State currentState, char entry) {

        switch (currentState) {
            case INITIAL: {
                if ((entry >= 'A' && entry <= 'Z') || (entry >= 'a' && entry <= 'z'))
                    return State.Q1; // USER DEFINED NAME

                else if (entry >= '0' && entry <= '9')
                    return State.Q2; // INTERGER_VALUE
                else if (entry == '+' || entry == '-')
                    return State.Q3; // SIGN FOR AN INTEGER_VALUE
                else {

                    return State.INVALID_STATE;
                }

            }

            case Q1: {
                return (entry >= 'A' && entry <= 'Z')
                        || (entry >= 'a' && entry <= 'z')
                        || (entry >= '0' && entry <= '9')
                        ? State.Q1 : State.INVALID_STATE;
            }



            case Q2: {

                if (entry >= '0' && entry <= '9')
                    return State.Q2;
                else
                    return State.INVALID_STATE;
            }

            case Q3: {
                return (entry >= '0' && entry <= '9') ? State.Q2 : State.INVALID_STATE;
            }

            default:


                return State.INVALID_STATE;
        }
    }

    public Token evaluate(String str) {
        State state = State.INITIAL;
        char[] chars = str.toCharArray();
        for (char c : chars) {
            state = executeTransition(state, c);


        }
        return finalStates.getOrDefault(state, Token.INVALID);
    }
}
