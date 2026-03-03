public class Bishop extends Piece {

    public Bishop(boolean white) {
        super(white);
    }

    @Override
    public boolean isValidMove(Piece[][] board,
                               int sr, int sc,
                               int er, int ec) {

        if (Math.abs(er - sr) != Math.abs(ec - sc))
            return false;

        int rowStep = Integer.compare(er, sr);
        int colStep = Integer.compare(ec, sc);

        int r = sr + rowStep;
        int c = sc + colStep;

        while (r != er) {
            if (board[r][c] != null)
                return false;

            r += rowStep;
            c += colStep;
        }

        return true;
    }

    @Override
    public String getImageName() {
        return isWhite() ? "wb" : "bb";
    }
}