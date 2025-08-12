package com.edd.tresenraya.core.ai;

import java.util.*;

/**
 * Representa un nodo en el árbol de decisiones del algoritmo Minimax.
 * Almacena el estado del juego y sus posibles movimientos.
 */
public class DecisionTreeNode {
    private final GameState state;
    private final List<DecisionTreeNode> children;
    int utility; // solo para nodos hoja
    int depth;

    /**
     * Crea un nuevo nodo del árbol.
     *
     * @param state Estado del juego en este nodo
     * @param depth Profundidad actual en el árbol
     */
    public DecisionTreeNode(GameState state, int depth) {
        this.state = state;
        this.children = new ArrayList<>();
        this.depth = depth;
    }

    /**
     * Obtiene los nodos hijos (posibles movimientos).
     *
     * @return Lista de nodos hijos
     */
    public List<DecisionTreeNode> getChildren() {
        return children;
    }

    /**
     * Obtiene el estado del juego asociado a este nodo.
     *
     * @return Estado del juego
     */
    public GameState getState() {
        return state;
    }
}
