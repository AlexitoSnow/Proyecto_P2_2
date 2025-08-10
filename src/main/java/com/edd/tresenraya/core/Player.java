package com.edd.tresenraya.core;

public class Player {
    private String name;
    private Character symbol; // X  O

    public Player(String name, Character symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public void marcarCasilla(Box[] casillas, int index) {
        if (casillas[index].isFree) {
            casillas[index].setSymbol(symbol);
        } else {
            throw new IllegalStateException("Casilla ocupada");
        }
    }


    public String getName() {
        return name;
    }

    public Character getSymbol() {
        return symbol;
    }

}
