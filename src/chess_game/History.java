package chess_game;

import java.util.ArrayList;
import java.util.List;

public class History {
    private final List<Board> boardStates = new ArrayList<>();
    private final List<List<Piece>> changes = new ArrayList<>();

    public History(Board board) {
        this.boardStates.add(new Board(board.getPiecesCopy()));
    }

    public void addBoardState(Board board) {
        this.boardStates.add(new Board(board.getPiecesCopy()));
        this.changes.add(getLastMove());
    }

    public Board getBoardState() {
        return new Board(boardStates.get(boardStates.size() - 1).getPiecesCopy());
    }

    public Board getLastBoardState() {
        if (boardStates.size() < 2) return null;
        return new Board(boardStates.get(boardStates.size() - 2).getPiecesCopy());
    }

    public List<Integer> getBoardStatesSizes() {
        return boardStates.stream()
                .map(Board::size) // Map each board to its size
                .toList(); // Collect the sizes
    }

    public List<Piece> getLastMove() {
        List<Piece> result = new ArrayList<>();

        if (boardStates.size() == 1) return result;

        List<Piece> oldPieces = new ArrayList<>(this.getLastBoardState().getPiecesCopy());
        List<Piece> newPieces = new ArrayList<>(this.getBoardState().getPiecesCopy());

        result.add(getDifference(oldPieces, newPieces));
        result.add(getDifference(newPieces, oldPieces));

        return result;
    }

    public Piece getDifference(List<Piece> listOne, List<Piece> listTwo) {
            for (Piece pieceOne : listOne) {
                boolean found = false;
                for (Piece pieceTwo : listTwo) {
                    if (pieceTwo.equals(pieceOne)) {
                        found = true;
                        break;
                    }
                }
                if (!found) return pieceOne;
            }
            System.err.println("ERROR History, getDifference returned null");
            return null;
        }

    public List<List<Piece>> getAllMoves() {
        return changes;
    }

    public List<Piece> getMovedPieces() {
        List<Piece> result = new ArrayList<>();
        if (changes.isEmpty()) return result;

        for (List<Piece> move : changes) {
            result.add(move.get(1));
        }
        return result;
    }

    public List<Position> getLastPositionChange() {
        return getLastMove().stream()
                .map(Piece::position) // Extract positions
                .toList(); // Store in a List
    }

    public List<List<Position>> getAllPositionChanges() {
        return changes.stream()
                .map(change -> change.stream().map(Piece::getPosition).toList())
                .toList();
    }

    public boolean pieceHasMoved(Piece piece) {
        if (getMovedPieces().isEmpty()) return false;

        for (Piece movedPiece : getMovedPieces()) {
            if (movedPiece.position().equals(piece.getPosition())) return true;
        }
        return false;
    }

    public boolean isCheckmate() {
        Board board = getBoardState();

        if (!board.testBoardStateChecks()) return false;

        for (Piece piece : board.getPiecesCopy(getTurn())) {
            if (!piece.allowedMoves(this).isEmpty()) return false;
        }

        return true;
    }

    public int getTurn() {
        return ((this.boardStates.size() - 1) % 2);
    }

}
