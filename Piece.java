public abstract class Piece {

    private boolean white;
    private boolean moved = false;

    public Piece(boolean white) {
        this.white = white;
    }

    public boolean isWhite() {
        return white;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved() {
        moved = true;
    }

    public abstract boolean isValidMove(
            Piece[][] board,
            int sr, int sc,
            int er, int ec);

    public abstract String getImageName();
}