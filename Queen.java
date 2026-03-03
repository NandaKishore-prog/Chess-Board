public class Queen extends Piece {

    public Queen(boolean white) {
        super(white);
    }

    @Override
    public boolean isValidMove(Piece[][] board,
                               int sr, int sc,
                               int er, int ec) {

        Rook rook = new Rook(isWhite());
        Bishop bishop = new Bishop(isWhite());

        return rook.isValidMove(board, sr, sc, er, ec) ||
               bishop.isValidMove(board, sr, sc, er, ec);
    }

    @Override
    public String getImageName() {
        return isWhite() ? "wq" : "bq";
    }
}