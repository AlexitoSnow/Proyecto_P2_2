package com.edd.tresenraya.core.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AI {

    public static int[] bestMove(GameState state, char computerSymbol, int difficultyDepth) {
        int bestValue = Integer.MIN_VALUE;
        List<int[]> bestMoves = new ArrayList<>();

        for (int[] move : state.getAvailableMoves()) {
            GameState nextState = state.simulateMove(move[0], move[1]);
            int value = minimax(nextState, difficultyDepth - 1, false, computerSymbol);

            if (value > bestValue) {
                bestValue = value;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (value == bestValue) {
                bestMoves.add(move);
            }
        }

        if (!bestMoves.isEmpty()) {
            Collections.shuffle(bestMoves);
            return bestMoves.get(0);
        }

        return null;
    }

    private static int minimax(GameState state, int depth, boolean isMaximizing, char computerSymbol) {
        if (state.isTerminal() || depth == 0) {
            return state.evaluateUtility(computerSymbol);
        }

        int bestValue = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int[] move : state.getAvailableMoves()) {
            GameState nextState = state.simulateMove(move[0], move[1]);
            int value = minimax(nextState, depth - 1, !isMaximizing, computerSymbol);
            bestValue = isMaximizing ? Math.max(bestValue, value) : Math.min(bestValue, value);
        }

        return bestValue;
    }
}
