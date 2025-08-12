package com.edd.tresenraya.core;

public class Player {
    private final String name;
    private final Character symbol; // X  O

    public Player(String name, Character symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public Character getSymbol() {
        return symbol;
    }

}
