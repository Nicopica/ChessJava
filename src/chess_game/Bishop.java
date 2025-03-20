package chess_game;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(int reihe, int linie, int color) {
        super(reihe, linie, color);
    }

    @Override
    public List<Position> moveSet() {
        List<Position> moves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            moves.add(position().relativ(i, i));
            moves.add(position().relativ(i, -i));
            moves.add(position().relativ(-i, i));
            moves.add(position().relativ(-i, -i));
        }
        return moves.stream().filter(Position::available).toList();
    }

    @Override
    public List<Position> reachablePositions(Board board) {
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}}; // Directions: NE, NW, SE, SW
        return this.moveWithDirection(directions, board);
    }

    @Override
    public List<Position> allowedMoves(History history) {
        Board board = history.getBoardState();
        List<Position> moves = reachablePositions(board);
        moves.removeAll(positionsInCheck(board, moves));
        return moves;
    }


    @Override
    public String getSprite() {
        return this.name() + "_" + (this.getColor() == 1 ? "b" : "w") + ".png";
    }

    @Override
    public String name() {
        return "bishop";
    }
}
