public class Rook extends Piece {

    public Rook(boolean white) {
        super(white);
    }

    @Override
    public boolean isValidMove(Piece[][] board,
                               int sr, int sc,
                               int er, int ec) {

        if (sr != er && sc != ec)
            return false;

        int rowStep = Integer.compare(er, sr);
        int colStep = Integer.compare(ec, sc);

        int r = sr + rowStep;
        int c = sc + colStep;

        while (r != er || c != ec) {
            if (board[r][c] != null)
                return false;

            r += rowStep;
            c += colStep;
        }

        return true;
    }

    @Override
    public String getImageName() {
        return isWhite() ? "wr" : "br";
    }
}