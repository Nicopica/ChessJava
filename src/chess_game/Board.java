package chess_game;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final List<Piece> pieces = new ArrayList<>();

    public Board(Piece piece) {
        this.pieces.add(piece);
    }

    public Board(List<Piece> pieces) {
        this.pieces.addAll(pieces);
    }

    public Board(Board board) {
        pieces.addAll(new ArrayList<>(board.getPiecesCopy()));
    }

    public List<Piece> getPieces() {
        return this.pieces;
    }

    public List<Piece> getPiecesCopy() {
        ArrayList<Piece> output = new ArrayList<>();
        for (Piece piece : pieces) {
            output.add(piece.clone());
        }
        return output;
    }

    public List<Piece> getPiecesCopy(int color) {
        ArrayList<Piece> output = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece.getColor() == color) output.add(piece.clone());
        }
        return output;
    }

    public List<Piece> getPiecesColor(int turnsColor) {
        return this.pieces.stream()
                .filter(piece -> piece.getColor() == turnsColor).toList();
    }

    public List<Position> getPositions() {
        List<Position> result = new ArrayList<>();
        for (Piece piece : pieces) {
            result.add(piece.getPosition());
        }
        return result;
    }

    public Piece findPiece(Piece piece) {
        for (Piece PieceToSearch : pieces) {
            if (piece.equals(PieceToSearch)) {
                return PieceToSearch;
            }
        }

        System.out.println("No such piece: " + piece);
        System.out.println(this.pieces);
        System.out.println("ERROR: No piece found. Look in Board");
        System.exit(0);
        return null;
    }

    public List<Position> getPositionsColor(int turnsColor) {
        List<Position> result = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece.getColor() == turnsColor) result.add(piece.getPosition());
        }
        return result;
    }

    public Piece getPieceMatchingPosition(Position position, int color) {
        for (Piece piece : pieces) {
            if (piece.getColor() == color && piece.getPosition().equals(position)) return piece;
        }
        return null;
    }

    public Position findKing(int color) {
        for (Piece piece : this.pieces) {
            if (piece instanceof King && piece.getColor() == color) {
                return piece.getPosition();
            }
        }
        System.err.println("Could not find king, color: " + color);
        return null;
    }

    public List<Rook> getRooks(int color) {
        return this.pieces.stream()
                .filter(piece -> piece instanceof Rook && piece.getColor() == color)
                .map(Rook.class::cast)
                .toList();
    }

    public void remove(Piece piece) {
        pieces.remove(piece);
    }

    public int invertColor(int color) {
        if (color == 0) return 1;
        return 0;
    }

    public Piece selectTile(Position selected, int myColor) {
        if (!getPositions().contains(selected)) return null;
        for (Piece piece : pieces) {
            if (piece.getPosition().equals(selected) && piece.getColor() == myColor) {
                return piece;
            }
        }
        return null;
    }

    public List<Position> reachablePositions(Piece piece, History history) {
        return piece.allowedMoves( history);
    }

    public boolean testBoardStateChecks() {
        return testBoardStateChecks(0) || testBoardStateChecks(1);
    }

    public boolean testBoardStateChecks(int color) {
        // true: check
        // false: not check

        List<Piece> colorPieces = this.getPiecesColor(invertColor(color));
        Position kingPosition = this.findKing(color);

        for (Piece piece : colorPieces) {
            if (piece.reachablePositions(this).contains(kingPosition)) return true;
        }
        return false;
    }

    public Piece removeIfPiece(Position deletePosition, int color) {
        for (Piece toRemove : this.getPiecesColor(invertColor(color))) {  // Remove enemy if there is
            if (toRemove.position().equals(deletePosition)) {
                return toRemove;
            }
        }
        return null;
    }

    public boolean movePiece(Piece piece, Position destination) {
        // True: moved with capture
        // False: moved without capture

        if (piece == null) return false;

        Piece pieceMoved = piece;

        for (Piece movePiece : this.getPieces()) {  // Make move
            if (movePiece.equals(piece)) {
                movePiece.setPosition(destination);
                pieceMoved = movePiece;
                break;
            }
        }

        if (piece.name().equals("pawn")) {
            Pawn pawn = (Pawn) pieceMoved;

            // TODO Promote options
            if (pawn.isEndingPawn()) {  // Check Promotion
                Piece replacement = new Queen(pawn.getFile(), pawn.getLine(), pawn.getColor());
                pieces.remove(pieceMoved);
                this.pieces.add(replacement);
            }
            else if (piece.position().file() != destination.file() &&  // Check if En Passant
                removeIfPiece(destination, piece.getColor()) == null) {
                    Position newDest = new Position(destination.file(), piece.position().line());
                    return this.pieces.remove(removeIfPiece(newDest, piece.getColor()));
                }
        }

        if (piece.name().equals("king") && destination.distance(piece.position()) >= 2) {  // Check crowning
            Position piecePosition = piece.position();
            if (destination.equals(new Position(piecePosition.file() + 2, piecePosition.line()))) {
                Piece rook = getPieceMatchingPosition(new Position(8, piece.getLine()), piece.getColor());
                Position rookDestination = new Position(6, piece.getLine());
                movePiece(rook, rookDestination);
            }
            else if (destination.equals(new Position(piecePosition.file() - 3, piecePosition.line()))) {
                Piece rook = getPieceMatchingPosition(new Position(1, piece.getLine()), piece.getColor());
                Position rookDestination = new Position(4, piece.getLine());
                movePiece(rook, rookDestination);
            }
        }

        return this.pieces.remove(removeIfPiece(destination, piece.getColor()));  // Capture
    }

    public boolean isNotPieceInPosition(Position position) {
        for (Piece piece : pieces) {
            if (piece.position().equals(position)) return false;
        }
        return true;
    }

    public int size() {
        return this.getPieces().size();
    }
}
