public class Knight extends Piece {

    public Knight(boolean white) {
        super(white);
    }

    @Override
    public boolean isValidMove(Piece[][] board,
                               int sr, int sc,
                               int er, int ec) {

        int dr = Math.abs(er - sr);
        int dc = Math.abs(ec - sc);

        return (dr == 2 && dc == 1) ||
               (dr == 1 && dc == 2);
    }

    @Override
    public String getImageName() {
        return isWhite() ? "wn" : "bn";
    }
}