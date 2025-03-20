package render_and_logic;

import chess_game.*;

import java.util.ArrayList;
import java.util.List;

class gameInteraction {
//                while (true) {
//        // ToDo Listener
//        int[] selectedCoordinates = {50, 50};
//        boardInteraction(selectedCoordinates, history);
//    }


    static Piece selectedPiece = null;  // this is probably bugged
    static boolean positionHasBeenSelected = false;
    static List<Position> possibleMoves = new ArrayList<>();

    public gameInteraction(int screenX, int screenY, History history) {

        // ToDo Unlight tile


        int x = screenX / 100 + 1;
        int y = (800 - screenY) / 100 + 1;
        System.out.println("clickX " + x + " clickY " + y);

        Position selectedPosition = new Position(x, y);

        if (!selectedPosition.available()) return;

        Board board = history.getBoardState();
        int turn = history.getTurn();

        if (positionHasBeenSelected) {
            if (possibleMoves.contains(selectedPosition)) {
                board.movePiece(selectedPiece, selectedPosition);
                history.addBoardState(board);
                // ToDo Render new board
            }
        }

        positionHasBeenSelected = false;

        if (board.getPieceMatchingPosition(selectedPosition, turn) != null) {
            selectedPiece = board.getPieceMatchingPosition(selectedPosition, turn);
            System.out.println(selectedPiece);
//            Piece myPiece = new Queen(5, 5, 1);
//            addSprite(myPiece);
            // ToDo Color selected piece
            possibleMoves = selectedPiece.allowedMoves(history);
            // ToDo Display possible moves
            positionHasBeenSelected = true;
        }
    }
}
