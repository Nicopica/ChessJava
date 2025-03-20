/**
 *
 * ## Possible Chessmen
 *
 * Ein Schachbrett ist waagrecht in acht Reihen (A bis H) und
 * senkrecht in acht Linien (1 bis 8) aufgebaut.
 * Auf so einem Schachbrett seien nun mehrere Zugfolgen gegeben.
 * Z.B.:
 *
 * B2 - C3 - E5 - E8 - G6
 *
 * Gem. den Schachregeln kann diese Zugfolge nicht jede Figur ziehen.
 * Sie sollen in dieser Aufgabe nun eine Methode `possibleChessmen()` entwickeln,
 * die bestimmen kann, welche Schachfiguren (König, Dame, Turm, Läufer, Springer)
 * in der Lage sind, eine __beliebig vorgegebene Zugfolge__ zu ziehen.
 *
 * Zur Hilfe sei Ihnen folgendes UML Klassendiagramm an die Hand gegeben,
 * mit denen das Problem objektorientiert strukturiert werden kann.
 *
 * ![UML](../unit-06/ex-03-possible-chessmen/UML.png)
 *
 * Implementieren Sie nun bitte die Methode `possibleChessmen()` in der `Main`-Klasse
 * und die Klassen des UML-Klassendiagramm um das Problem zu lösen.
 *
 * __Hinweise:__
 *
 * - Die Bauern werden bewusst nicht berücksichtigt.
 * - Beachten Sie, dass die Klasse `Figur` bewusst als abstrakte Klasse konzipiert wurde.
 * - Nehmen Sie sich ein Schachbrett (oder mindestens ein Blatt Papier) zur Hand,
 *   um das Problem nachzuvollziehen.
 *
 */
class Main {

    public static void main(String[] args) {

        for (int x = 1; x < 8; x++) {
            int a = (x - 1) % 2;
            System.out.println(a);
        }

/*
        List<Piece> defaultChessConfiguration = new ArrayList<>();
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
        History history = new History(defaultBoard);
        ChessGame game1 = new ChessGame(defaultBoard, history);

//        System.out.println(defaultBoard.selectTile(new Position(1, 1), 0));

//        Piece myPiece = new Queen(5, 5, 1);
        Piece myPiece = defaultBoard.selectTile(new Position(2, 1), 0);
        System.out.println(myPiece);

        System.out.println(defaultBoard.reachablePositions(myPiece, history));
*/
    }
}
