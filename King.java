public class King extends Piece {

    public King(boolean white) {
        super(white);
    }

    @Override
    public boolean isValidMove(Piece[][] board,
                               int sr, int sc,
                               int er, int ec) {

        int dr = Math.abs(er - sr);
        int dc = Math.abs(ec - sc);

        // Normal move
        if (dr <= 1 && dc <= 1)
            return true;

        // Castling
        if (!hasMoved() && dr == 0 && dc == 2)
            return true;

        return false;
    }

    @Override
    public String getImageName() {
        return isWhite() ? "wk" : "bk";
    }
}