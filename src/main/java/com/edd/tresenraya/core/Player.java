package com.edd.tresenraya.core;

public class Player {
    private String nombre;
    private char simbolo; // X  O

    public Player(String playerName) {
        this.nombre = playerName;
        this.simbolo = 'X';

    }

    public void marcarCasilla(box[] casillas, int index) {

        if (casillas[index].isFree) {
            casillas[index].setSymbol(simbolo);
        } else {
            throw new IllegalStateException("Casilla ocupada");
        }

    }

    public int utilidad(Table tablero){

        return 0;
    }
}
