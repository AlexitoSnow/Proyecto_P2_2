package com.edd.tresenraya.core.ai;

import java.util.*;

public class GameState {

    private char[][] board;
    private char currentTurn;

    public GameState(char[][] board, char currentTurn) {
        this.board = board;
        this.currentTurn = currentTurn;
    }

    public char[][] getBoard() {
        return board;
    }

    public char getCurrentTurn() {
        return currentTurn;
    }

    public boolean isTerminal() {
        return getWinner() != ' ' || getAvailableMoves().isEmpty();
    }

    public char getWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' &&
                    board[i][0] == board[i][1] &&
                    board[i][1] == board[i][2]) return board[i][0];

            if (board[0][i] != ' ' &&
                    board[0][i] == board[1][i] &&
                    board[1][i] == board[2][i]) return board[0][i];
        }

        if (board[0][0] != ' ' &&
                board[0][0] == board[1][1] &&
                board[1][1] == board[2][2]) return board[0][0];

        if (board[0][2] != ' ' &&
                board[0][2] == board[1][1] &&
                board[1][1] == board[2][0]) return board[0][2];

        return ' ';
    }

    public List<int[]> getAvailableMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ')
                    moves.add(new int[]{i, j});
        return moves;
    }

    public GameState simulateMove(int row, int col) {
        char[][] newBoard = new char[3][3];
        for (int i = 0; i < 3; i++)
            System.arraycopy(board[i], 0, newBoard[i], 0, 3);

        newBoard[row][col] = currentTurn;
        char nextTurn = (currentTurn == 'X') ? 'O' : 'X';
        return new GameState(newBoard, nextTurn);
    }

    public int evaluateUtility(char computerSymbol) {
        char opponent = (computerSymbol == 'X') ? 'O' : 'X';

        int pc = countPossibleLines(computerSymbol);
        int po = countPossibleLines(opponent);

        return pc - po;
    }

    private int countPossibleLines(char symbol) {
        int count = 0;

        for (int i = 0; i < 3; i++) {
            String row = "" + board[i][0] + board[i][1] + board[i][2];
            if (row.indexOf(opponentOf(symbol)) == -1) count++;

            String col = "" + board[0][i] + board[1][i] + board[2][i];
            if (col.indexOf(opponentOf(symbol)) == -1) count++;
        }

        String diag1 = "" + board[0][0] + board[1][1] + board[2][2];
        if (diag1.indexOf(opponentOf(symbol)) == -1) count++;

        String diag2 = "" + board[0][2] + board[1][1] + board[2][0];
        if (diag2.indexOf(opponentOf(symbol)) == -1) count++;

        return count;
    }

    private char opponentOf(char symbol) {
        return symbol == 'X' ? 'O' : 'X';
    }


}
