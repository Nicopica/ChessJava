import chess_game.Board;
import chess_game.History;

import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private Board board;
    private final History history;
    private List<Players> players = new ArrayList<>();

    public ChessGame(Board initialState) {
        this.board = initialState;
        this.history = new History(initialState);
        this.players = new ArrayList<>();
    }

    public ChessGame(Board initialState, History history) {
        this.board = initialState;
        this.history = history;
    }

    public Board getBoard() {
        return board;
    }

    public History getHistory() {
        return this.history;
    }


    public List<Players> getPlayers() {
        return players;
    }

    public void move(Board board) {
        this.board = board;
        this.history.addBoardState(board);
    }

    public Board getLastBoardState() {
        return this.history.getLastBoardState();
    }
}
