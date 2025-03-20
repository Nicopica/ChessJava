package chess_game;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(int reihe, int linie, int color) {
        super(reihe, linie, color);
    }

    private List<Position> castlingMoves(History history) {
        List<Position> output = new ArrayList<>();
        Board board = history.getBoardState();

        if (history.pieceHasMoved(this)) return output;

        if (board.testBoardStateChecks()) return output;

        List<Rook> rooks = board.getRooks(this.getColor());

        int castlingSide = this.getLine();

        if (rooks.isEmpty()) return output;

        if (!history.pieceHasMoved(rooks.get(1)) &&  // Left side
                board.isNotPieceInPosition(new Position(this.getFile() - 1, castlingSide)) &&
                board.isNotPieceInPosition(new Position(this.getFile() - 2, castlingSide)) &&
                board.isNotPieceInPosition(new Position(this.getFile() - 3, castlingSide))) {

            board.movePiece(this, new Position(this.getFile() - 1, castlingSide));
            if (!board.testBoardStateChecks()) {

                board.movePiece(this, new Position(this.getFile() - 2, castlingSide));
                if (!board.testBoardStateChecks()) {

                    board.movePiece(this, new Position(this.getFile() - 3, castlingSide));
                    if (!board.testBoardStateChecks()) {
                        output.add(new Position(this.getFile() - 2, castlingSide));
                    }
                }
            }
        }

        if (rooks.size() == 1 || history.pieceHasMoved(rooks.get(1))) return output;

        if (board.isNotPieceInPosition(new Position(this.getFile() + 1, castlingSide)) &&  // Right side
                board.isNotPieceInPosition(new Position(this.getFile() + 2, castlingSide))) {

            board.movePiece(this, new Position(this.getFile() + 1, castlingSide));
            if (!board.testBoardStateChecks()) {

                board.movePiece(this, new Position(this.getFile() + 2, castlingSide));
                if (!board.testBoardStateChecks()) {
                    output.add(new Position(this.getFile() + 2, castlingSide));
                }
            }

        }
        return output;
    }

    @Override
    public List<Position> moveSet() {
        List<Position> moves = new ArrayList<>();
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 0; i < 8; i++) {
            Position newPos = position().relativ(dx[i], dy[i]);
            if (newPos.available()) moves.add(newPos);
        }
        return moves;
    }

    @Override
    public List<Position> reachablePositions(Board board) {
        return this.moveWithPositions(this.moveSet(), board);
    }

    @Override
    public List<Position> allowedMoves(History history) {
        Board board = history.getBoardState();
        List<Position> moves = this.reachablePositions(board);

        moves.addAll(this.castlingMoves(history));

        moves.removeAll(positionsInCheck(board, moves));
        return moves;
    }

    @Override
    public String getSprite() {
        return this.name() + "_" + (this.getColor() == 1 ? "b" : "w") + ".png";
    }

    @Override
    public String name() {
        return "king";
    }
}
