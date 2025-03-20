package chess_game;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(int reihe, int linie, int color) {
        super(reihe, linie, color);
    }

    @Override
    public List<Position> moveSet() {
        int[][] moves = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};
        List<Position> positions = new ArrayList<>();

        for (int[] move : moves) {
            Position newPos = position().relativ(move[0], move[1]);
            if (newPos.available()) {
                positions.add(newPos);
            }
        }
        return positions;
    }

    @Override
    public List<Position> reachablePositions(Board board) {
        return this.moveWithPositions(this.moveSet(), board);
    }

    @Override
    public List<Position> allowedMoves(History history) {
        Board board = history.getBoardState();
        List<Position> moves = this.reachablePositions(board);
        moves.removeAll(positionsInCheck(board, moves));
        return moves;
    }

    @Override
    public String getSprite() {
        return this.name() + "_" + (this.getColor() == 1 ? "b" : "w") + ".png";
    }


    @Override
    public String name() {
        return "knight";
    }
}