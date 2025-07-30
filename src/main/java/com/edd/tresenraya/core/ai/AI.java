package com.edd.tresenraya.core.ai;

public class AI {
    public static int[] bestMove(GameState state, char computerSymbol) {
        int bestValue = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int[] move : state.getAvailableMoves()) {
            GameState nextState = state.simulateMove(move[0], move[1]);
            int value = minimax(nextState, 2, false, computerSymbol);
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private static int minimax(GameState state, int depth, boolean isMaximizing, char computerSymbol) {
        if (state.isTerminal() || depth == 0)
            return state.evaluateUtility(computerSymbol);

        int bestValue = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int[] move : state.getAvailableMoves()) {
            GameState nextState = state.simulateMove(move[0], move[1]);
            int value = minimax(nextState, depth - 1, !isMaximizing, computerSymbol);
            bestValue = isMaximizing ? Math.max(bestValue, value) : Math.min(bestValue, value);
        }

        return bestValue;
    }
}
