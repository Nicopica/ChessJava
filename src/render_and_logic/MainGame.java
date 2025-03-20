package render_and_logic;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;
import chess_game.*;


public class MainGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture chessBoard;
    private List<Texture> pieceTextures;
    private List<Integer> pieceX;
    private List<Integer> pieceY;

    private History history;

    private Piece selectedPiece = null;  // this is probably bugged
    private boolean positionHasBeenSelected = false;
    private List<Position> possibleMoves = new ArrayList<>();
    private final SoundManager sm = new SoundManager();

    @Override
    public void create() {

        List<Piece> defaultChessConfiguration = new ArrayList<>();


        sm.loadSound("cancelMove", "src/assets/sound/effects/cancelMove.wav");
        sm.loadSound("capture", "src/assets/sound/effects/capture.wav");
        sm.loadSound("check", "src/assets/sound/effects/check.wav");
        sm.loadSound("gameOver", "src/assets/sound/effects/gameOver.wav");
        sm.loadSound("move", "src/assets/sound/effects/move.wav");
        sm.loadSound("promotion", "src/assets/sound/effects/promotion.wav");
        sm.loadSound("select", "src/assets/sound/effects/select.wav");

        sm.loadSound("music", "src/assets/sound/music/music.wav");
        sm.playSound("music");

        for (int i = 1; i <= 8; i++) {
            defaultChessConfiguration.add(new Pawn(i, 2, 0));
            defaultChessConfiguration.add(new Pawn(i, 7, 1));
        }

        int line = 1;
        for (int color = 0; color <= 1; color++) {
            defaultChessConfiguration.add(new Rook(1, line, color));
            defaultChessConfiguration.add(new Rook(8, line, color));

            defaultChessConfiguration.add(new Knight(2, line, color));
            defaultChessConfiguration.add(new Knight(7, line, color));

            defaultChessConfiguration.add(new Bishop(3, line, color));
            defaultChessConfiguration.add(new Bishop(6, line, color));

            defaultChessConfiguration.add(new Queen(4, line, color));
            defaultChessConfiguration.add(new King(5, line, color));
            line = 8;
        }


        Board defaultBoard = new Board(defaultChessConfiguration);

        addBoard(defaultBoard);

        history = new History(defaultBoard);
//        ChessGame game1 = new ChessGame(defaultBoard, history);

        batch = new SpriteBatch();
        chessBoard = new Texture(Gdx.files.internal("assets/images/other/bg.png"));

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    handleClick(screenX, screenY);
                }
                return true;
            }
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(chessBoard, 0, 0, 800, 800);

        for (int i = 0; i < pieceTextures.size(); i++) {
            batch.draw(pieceTextures.get(i), pieceX.get(i), pieceY.get(i), 100, 100);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        chessBoard.dispose();
        for (Texture t : pieceTextures) t.dispose();
    }

    public void addSprite(Position position, String path) {
        pieceTextures.add(new Texture(Gdx.files.internal(path)));
        pieceX.add((position.file()  - 1) * 100);
        pieceY.add((position.line() - 1) * 100);
    }

    public void addPiece(Piece piece) {
        pieceTextures.add(new Texture(Gdx.files.internal("assets/images/pieces/" + piece.getSprite())));
        pieceX.add((piece.getFile() - 1) * 100);
        pieceY.add((piece.getLine() - 1) * 100);
    }

    public void addBoard(Board board) {
        pieceTextures = new ArrayList<>();
        pieceX = new ArrayList<>();
        pieceY = new ArrayList<>();

        for (Piece piece : board.getPieces()) {
            addPiece(piece);
        }
    }

    private void handleClick(int screenX, int screenY) {

        int x = screenX / 100 + 1;
        int y = (800 - screenY) / 100 + 1;
        Position selectedPosition = new Position(x, y);

        if (!selectedPosition.available()) return;

        Board board = history.getBoardState();
        addBoard(board);  // Deselect tile
        int turn = history.getTurn();

        if (positionHasBeenSelected && possibleMoves.contains(selectedPosition)) {
                boolean capture = board.movePiece(selectedPiece, selectedPosition);
                history.addBoardState(new Board(board.getPiecesCopy()));
                addBoard(board);  // Render new board
                positionHasBeenSelected = false;

                if (history.isCheckmate()) sm.playSound("gameOver");
                else if (board.testBoardStateChecks()) sm.playSound("check");
                else if (capture) sm.playSound("capture");
                else {

                    sm.playSound("move");
                }


                return;
            }


        if (board.getPieceMatchingPosition(selectedPosition, turn) != null) {
            selectedPiece = board.getPieceMatchingPosition(selectedPosition, turn);
            addSprite(selectedPosition, "assets/images/other/Untitled.png"); // Color selected piece
            possibleMoves = selectedPiece.allowedMoves(history);
            for (Position p : possibleMoves) {
                addSprite(p, "assets/images/other/dot.png"); // Display possible moves
            }
            positionHasBeenSelected = true;
            sm.playSound("select");
        }
        else {
            sm.playSound("cancelMove");
            sm.playSound("cancelMove");
            sm.playSound("cancelMove");
        }
    }
}
