package com.edd.tresenraya.core.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementación del algoritmo Minimax para la IA del juego.
 */
public class AI {
    /**
     * Determina el mejor movimiento posible para la IA.
     *
     * @param state Estado actual del juego
     * @param computerSymbol Símbolo que usa la IA (X o O)
     * @param difficultyDepth Profundidad máxima de búsqueda
     * @return Coordenadas del mejor movimiento [fila, columna]
     */
    public static int[] bestMove(GameState state, char computerSymbol, int difficultyDepth) {

        DecisionTreeNode root = construirArbol(state, computerSymbol, difficultyDepth, true);

        int bestValue = Integer.MIN_VALUE;
        List<int[]> bestMoves = new ArrayList<>();

        for (DecisionTreeNode child : root.getChildren()) {
            int value = minimaxNodo(child, difficultyDepth - 1, false, computerSymbol);

            if (value > bestValue) {
                bestValue = value;
                bestMoves.clear();
                bestMoves.add(child.getState().getUltimoMovimiento());
            } else if (value == bestValue) {
                bestMoves.add(child.getState().getUltimoMovimiento());
            }
        }

        if (!bestMoves.isEmpty()) {
            Collections.shuffle(bestMoves);
            return bestMoves.get(0);
        }

        return null;
    }

    /**
     * Construye el árbol de decisiones para el algoritmo Minimax.
     *
     * @param estado Estado actual del juego
     * @param computerSymbol Símbolo de la IA
     * @param difficultyDepth Profundidad restante de búsqueda
     * @param turnoComputadora True si es turno de la IA
     * @return Nodo raíz del árbol de decisiones
     */
    private static DecisionTreeNode construirArbol(GameState estado, char computerSymbol, int difficultyDepth, boolean turnoComputadora) {
        DecisionTreeNode nodo = new DecisionTreeNode(estado, difficultyDepth);

        // Si llegamos a una hoja
        if (estado.isTerminal() || difficultyDepth == 0) {
            nodo.utility = estado.evaluateUtility(computerSymbol);
            return nodo;
        }

        // Generar hijos
        for (int[] movimiento : estado.getAvailableMoves()) {
            GameState siguiente = estado.simulateMove(movimiento[0], movimiento[1]);
            siguiente.setUltimoMovimiento(movimiento); // para saber de dónde viene
            nodo.getChildren().add(construirArbol(siguiente, computerSymbol, difficultyDepth - 1, !turnoComputadora));
        }

        return nodo;
    }

    /**
     * Implementa el algoritmo Minimax para evaluar los nodos del árbol.
     *
     * @param nodo Nodo actual a evaluar
     * @param depth Profundidad restante
     * @param isMaximizing True si es turno del maximizador
     * @param computerSymbol Símbolo de la IA
     * @return Valor de utilidad del nodo
     */
    private static int minimaxNodo(DecisionTreeNode nodo, int depth, boolean isMaximizing, char computerSymbol) {

        if (nodo.getChildren().isEmpty() || depth == 0) {
            return nodo.utility;
        }

        if (isMaximizing) {
            int mejor = Integer.MIN_VALUE;
            for (DecisionTreeNode hijo : nodo.getChildren()) {
                mejor = Math.max(mejor, minimaxNodo(hijo, depth - 1, false, computerSymbol));
            }
            return mejor;
        } else {
            int peor = Integer.MAX_VALUE;
            for (DecisionTreeNode hijo : nodo.getChildren()) {
                peor = Math.min(peor, minimaxNodo(hijo, depth - 1, true, computerSymbol));
            }
            return peor;
        }
    }
}
