package chess_game;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Piece {
    private Position position;
    private final int color;

    protected Piece(int file, int line, int color) {
        this.position = new Position(file, line);
        this.color = color;
    }

    protected Piece(Piece piece) {
        this.position = new Position(piece.getFile(), piece.getLine());
        this.color = piece.color;
    }

    public abstract List<Position> moveSet();

    public abstract List<Position> reachablePositions(Board board);

    public abstract List<Position> allowedMoves(History history);

    public abstract String name();

    public abstract String getSprite();

    public Position position() {
        return this.position;
    }

    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }

    public Position getPosition() {
        return this.position;
    }

    public int getColor() {
        return this.color;
    }

    public int getFile() {
        return this.position.file();
    }

    public int getLine() {
        return this.position.line();
    }

    public List<Position> moveWithDirection(@org.jetbrains.annotations.NotNull int[][] directions, Board board) {
        List<Position> moves = new ArrayList<>();
        for (int[] direction : directions) {
            int i = 1; // Step counter
            while (true) {
                Position nextPos = this.position().relativ(i * direction[0], i * direction[1]);
                if (!nextPos.available()) break; // Stop if not available

                boolean foundOpponent = false;
                for (Piece boardPiece : board.getPieces()) {
                    if (nextPos.equals(boardPiece.position())) {
                        if (boardPiece.getColor() != this.getColor())
                            moves.add(nextPos); // Add if opponent piece
                        foundOpponent = true; // Found a piece
                        break; // Exit loop
                    }
                }
                if (foundOpponent) break; // Stop
                moves.add(nextPos); // Add
                i++; // Move next
            }
        }
        return moves;
    }

    public List<Position> moveWithPositions(List<Position> positions, Board board) {
        List<Position> moves = new ArrayList<>();
        pp:
        for (Position pos : positions) {
            for (Piece boardPiece : board.getPieces()) {
                if (pos.equals(boardPiece.position())) {
                    if (boardPiece.getColor() != this.getColor()) moves.add(pos);
                    continue pp;
                }
            }
            moves.add(pos);
        }
        return moves;
    }

    public List<Position> positionsInCheck(Board board, @NotNull List<Position> moves) {
        List<Position> output = new ArrayList<>();
        for (Position pos : moves) {
            Board boardCopy = new Board(board.getPiecesCopy());
            boardCopy.movePiece(this, pos);

            if (boardCopy.testBoardStateChecks(this.color)) output.add(pos);
        }
        return output;
    }

    @Override
    public Piece clone() {
        int pieceColor = this.color;
        String name = this.getClass().getSimpleName().toLowerCase();

        return switch (name) {
            case "pawn" -> new Pawn(this.position.file(), this.position.line(), pieceColor);
            case "knight" -> new Knight(this.position.file(), this.position.line(), pieceColor);
            case "bishop" -> new Bishop(this.position.file(), this.position.line(), pieceColor);
            case "rook" -> new Rook(this.position.file(), this.position.line(), pieceColor);
            case "queen" -> new Queen(this.position.file(), this.position.line(), pieceColor);
            case "king" -> new King(this.position.file(), this.position.line(), pieceColor);
            default -> throw new IllegalStateException("Unknown piece type: " + name);
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Piece piece = (Piece) obj;
        return  this.position().equals(piece.position()) &&
                this.name().equals(piece.name()) &&
                this.color == piece.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name(), getColor(), getPosition());
    }

    @Override
    public String toString() {
        return name() + " pos: " + position + ", col: " + color;
    }
}