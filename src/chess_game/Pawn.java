package chess_game;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(int file, int line, int color) {
        super(file,line, color);
    }

    public boolean canMoveForward(Board board, Position destination) {
        for (Piece piece : board.getPieces()) {
            if (piece.position().equals(destination)) return false;
        }
        return true;
    }

    public boolean isStartingPawn() {
        if (this.getColor() == 0 && this.getLine() == 2) return true;
        return this.getColor() == 1 && this.getLine() == 7;
    }

    public List<Position> canAttackDiagonally(Board board) {
        int attackColor = 0;
        Position rightAttack = null;
        Position leftAttack = null;

        if (this.getColor() == 0) {
            attackColor = 1;
            rightAttack = new Position(this.getFile() + 1, this.getLine() + 1);
            leftAttack = new Position(this.getFile() - 1, this.getLine() + 1);
        }
        if (this.getColor() == 1) {
            attackColor = 0;
            rightAttack = new Position(this.getFile() + 1, this.getLine() - 1);
            leftAttack = new Position(this.getFile() - 1, this.getLine() - 1);
        }


        List<Position> result = new ArrayList<>();
        for (Piece piece : board.getPiecesColor(attackColor)) {
            if (piece.getPosition().equals(leftAttack)) result.add(piece.getPosition());

            if (piece.getPosition().equals(rightAttack)) result.add(piece.getPosition());
        }

        return result;
    }

    public List<Position> canMoveDouble(Board board) {
        List<Position> result = new ArrayList<>();
        if (!this.isStartingPawn()) return result;

        int tempDirection = 2;
        if (this.getColor() == 1) {
            tempDirection = -2;
        }

        for (Piece piece : board.getPieces()) {
            if (piece.position().equals(new Position(this.getFile(), this.getLine() + tempDirection))) return result;
        }
        result.add(new Position(this.getFile(), this.getLine() + tempDirection));
        return result;
    }

    public List<Position> enPassant(History history) {
        List<Position> result = new ArrayList<>();
        List<Piece> lastMove = history.getLastMove();

        if (lastMove.isEmpty()) return result;

        if (lastMove.get(0).name().equals("pawn")) {
            int tempDirection = 1;
            if (this.getColor() == 1) {
                tempDirection = -1;
            }

            Position pawnFirstLeft = new Position(this.getFile() - 1, this.getLine() + 2 * tempDirection);
            Position pawnSecondLeft = new Position(this.getFile() - 1, this.getLine());
            if (lastMove.get(0).position().equals(pawnFirstLeft) &&
                lastMove.get(1).position().equals(pawnSecondLeft)) {
                result.add(new Position(lastMove.get(1).getFile(), this.getLine() + tempDirection));
            }

            Position pawnFirstRight = new Position(this.getFile() + 1, this.getLine() + 2 * tempDirection);
            Position pawnSecondRight = new Position(this.getFile() + 1, this.getLine());
            if (lastMove.get(0).position().equals(pawnFirstRight) &&
                lastMove.get(1).position().equals(pawnSecondRight)) {
                result.add(new Position(lastMove.get(1).getFile(), this.getLine() + tempDirection));
            }
        }
        return result;
    }

    public boolean isEndingPawn() {
        if (this.getColor() == 0 && this.getLine() == 8) return true;
        return this.getColor() == 1 && this.getLine() == 1;
    }

    @Override
    public List<Position> moveSet() {
        int[] moves = {0, 1};
        if (this.getColor() == 1) {
            moves = new int[]{0, -1};
        }
        List<Position> positions = new ArrayList<>();

        Position newPos = position().relativ(moves[0], moves[1]);
        if (newPos.available()) positions.add(newPos);
        return positions;
    }

    @Override
    public List<Position> reachablePositions(Board board) {
        // Check for attack diagonal
        return new ArrayList<>(canAttackDiagonally(board));
    }

    @Override
    public List<Position> allowedMoves(History history) {
        Board board = history.getBoardState();
        List<Position> moves = this.moveSet();

        if (moves.isEmpty()) return moves;

        if (!canMoveForward(board, moves.get(0))) moves.remove(0);
        else moves.addAll(canMoveDouble(board));

        moves.addAll(this.reachablePositions(board));

        moves.removeAll(positionsInCheck(board, moves));

        moves.addAll(enPassant(history));

        return moves;
//          TODO: Crowning
    }

    @Override
    public String getSprite() {
        return this.name() + "_" + (this.getColor() == 1 ? "b" : "w") + ".png";
    }


    @Override
    public String name() {
        return "pawn";
    }

}
