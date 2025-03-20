package chess_game;

public class Position {
    private final int file;
    private final int line;

    public Position(int f, int l) {
        this.file = f;
        this.line = l;
    }

    public int file() {
        return file;
    }

    public int line() {
        return line;
    }

    public boolean available() {
        return this.line <= 8 && this.line >= 1 && this.file >= 1 && this.file <= 8;
    }

    private char convertNumberToChar(int number) {
        return Character.toUpperCase((char) ('a' + (number - 1)));
    }

    private int convertCharToNumber(char c) {
        return (c - 'a') + 1;
    }

    public int distance(Position position) {
        return Math.max(Math.abs(this.file() - position.file()), Math.abs(this.line() - position.line()));

    }

    public Position relativ(int f, int l) {
        return new Position(this.file + f, this.line + l);
    }

    public String toChessFormat() {
        return String.valueOf(convertNumberToChar(file)) + line;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position pos)) return false;
        return this.file == pos.file && this.line == pos.line;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(file);
        result = 31 * result + Integer.hashCode(line);
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(file) + line;
    }
}