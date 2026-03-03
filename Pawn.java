public class Pawn extends Piece {

    public Pawn(boolean white) {
        super(white);
    }

    @Override
    public boolean isValidMove(Piece[][] board,
                               int sr, int sc,
                               int er, int ec) {

        int direction = isWhite() ? -1 : 1;

        // Forward move
        if (sc == ec && board[er][ec] == null) {

            if (er == sr + direction)
                return true;

            if (!hasMoved() &&
                er == sr + 2 * direction &&
                board[sr + direction][sc] == null)
                return true;
        }

        // Capture
        if (er == sr + direction &&
           (ec == sc + 1 || ec == sc - 1) &&
            board[er][ec] != null &&
            board[er][ec].isWhite() != isWhite())
            return true;

        return false;
    }

    @Override
    public String getImageName() {
        return isWhite() ? "wp" : "bp";
    }
}