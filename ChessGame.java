import java.awt.*;
import javax.swing.*;

public class ChessGame extends JFrame {

    private JButton[][] board = new JButton[8][8];
    private Piece[][] pieces = new Piece[8][8];

    private boolean whiteTurn = true;
    private boolean gameOver = false;

    private int selectedRow = -1;
    private int selectedCol = -1;

    public ChessGame() {

        setTitle("Java Swing Chess");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        initializeBoard();
        initializePieces();
        updateBoard();

        setVisible(true);
    }

    // ================= BOARD =================

    private void initializeBoard() {

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                JButton square = new JButton();
                square.setFocusPainted(false);

                if ((r + c) % 2 == 0)
                    square.setBackground(new Color(240, 217, 181));
                else
                    square.setBackground(new Color(181, 136, 99));

                final int row = r;
                final int col = c;

                square.addActionListener(e -> {
                    if (!gameOver)
                        handleClick(row, col);
                });

                board[r][c] = square;
                add(square);
            }
        }
    }

    // ================= INITIAL SETUP =================

    private void initializePieces() {

        // Black
        pieces[0][0] = new Rook(false);
        pieces[0][1] = new Knight(false);
        pieces[0][2] = new Bishop(false);
        pieces[0][3] = new Queen(false);
        pieces[0][4] = new King(false);
        pieces[0][5] = new Bishop(false);
        pieces[0][6] = new Knight(false);
        pieces[0][7] = new Rook(false);

        for (int i = 0; i < 8; i++) {
            pieces[1][i] = new Pawn(false);
            pieces[6][i] = new Pawn(true);
        }

        // White
        pieces[7][0] = new Rook(true);
        pieces[7][1] = new Knight(true);
        pieces[7][2] = new Bishop(true);
        pieces[7][3] = new Queen(true);
        pieces[7][4] = new King(true);
        pieces[7][5] = new Bishop(true);
        pieces[7][6] = new Knight(true);
        pieces[7][7] = new Rook(true);
    }

    // ================= UPDATE =================

    private void updateBoard() {

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                if (pieces[r][c] != null) {

                    String path = "pieces/" +
                            pieces[r][c].getImageName() + ".png";

                    ImageIcon icon = new ImageIcon(path);
                    Image img = icon.getImage()
                            .getScaledInstance(80, 80, Image.SCALE_SMOOTH);

                    board[r][c].setIcon(new ImageIcon(img));
                } else {
                    board[r][c].setIcon(null);
                }
            }
        }
        // Highlight king if in check
if (isKingInCheck(true))
    highlightKing(true);

if (isKingInCheck(false))
    highlightKing(false);
        
    }
    private void highlightKing(boolean white) {

    for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {

            if (pieces[r][c] instanceof King &&
                pieces[r][c].isWhite() == white) {

                board[r][c].setBackground(Color.RED);
            }
        }
    }
}


    private void handleClick(int row, int col) {

        if (selectedRow == -1) {

            if (pieces[row][col] != null &&
                pieces[row][col].isWhite() == whiteTurn) {

               selectedRow = row;
selectedCol = col;

resetBoardColors();

board[row][col].setBorder(
        BorderFactory.createLineBorder(Color.YELLOW, 3));

highlightMoves(row, col);
            }

        } else {

            Piece selectedPiece = pieces[selectedRow][selectedCol];

            if (selectedPiece != null &&
                selectedPiece.isValidMove(
                        pieces, selectedRow, selectedCol, row, col) &&
                (pieces[row][col] == null ||
                 pieces[row][col].isWhite() != whiteTurn)) {

                Piece captured = pieces[row][col];

                // TEMP MOVE
                pieces[row][col] = selectedPiece;
                pieces[selectedRow][selectedCol] = null;

                if (isKingInCheck(whiteTurn)) {

                    // Undo
                    pieces[selectedRow][selectedCol] = selectedPiece;
                    pieces[row][col] = captured;

                } else {

                 // Castling
if (selectedPiece instanceof King &&
    Math.abs(col - selectedCol) == 2 &&
    !selectedPiece.hasMoved()) {

    if (col > selectedCol) { // King side

        Piece rook = pieces[row][7];

        if (rook instanceof Rook && !rook.hasMoved()) {

            pieces[row][5] = rook;
            pieces[row][7] = null;
            rook.setMoved();
        }

    } else { // Queen side

        Piece rook = pieces[row][0];

        if (rook instanceof Rook && !rook.hasMoved()) {

            pieces[row][3] = rook;
            pieces[row][0] = null;
            rook.setMoved();
        }
    }
}
                    selectedPiece.setMoved();
                    // ================= PAWN PROMOTION =================
if (selectedPiece instanceof Pawn) {

    // White pawn reaches top
    if (selectedPiece.isWhite() && row == 0) {
        pieces[row][col] = new Queen(true);
    }

    // Black pawn reaches bottom
    if (!selectedPiece.isWhite() && row == 7) {
        pieces[row][col] = new Queen(false);
    }
}
whiteTurn = !whiteTurn;

// Update board FIRST
updateBoard();

// Now check checkmate
if (isCheckmate(whiteTurn)) {

    gameOver = true;

    String winner = whiteTurn ? "Black" : "White";
SwingUtilities.invokeLater(() -> {

    JButton rematchBtn = new JButton("Rematch");
    JButton exitBtn = new JButton("Exit");

    // Remove focus border
    rematchBtn.setFocusPainted(false);
    exitBtn.setFocusPainted(false);

    // Optional: Make buttons look nicer
    rematchBtn.setBackground(new Color(70, 130, 180));
    rematchBtn.setForeground(Color.WHITE);

    exitBtn.setBackground(new Color(178, 34, 34));
    exitBtn.setForeground(Color.WHITE);

    Object[] options = { rematchBtn, exitBtn };

    int choice = JOptionPane.showOptionDialog(
            this,
            "♚ CHECKMATE ♚\n\n" + winner + " Wins!\n\nPlay Again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
    );

    if (choice == 0) {
        restartGame();
    } else {
        System.exit(0);
    }
});
}
                }
            }

            board[selectedRow][selectedCol].setBorder(null);
selectedRow = -1;
selectedCol = -1;

resetBoardColors();
updateBoard();

        }
    }
    private void restartGame() {

    // Clear board
    pieces = new Piece[8][8];

    whiteTurn = true;
    gameOver = false;

    selectedRow = -1;
    selectedCol = -1;

    initializePieces();
    resetBoardColors();
    updateBoard();
}

    // ================= CHECK =================

    private boolean isKingInCheck(boolean white) {

        int kingRow = -1, kingCol = -1;

        // Find king
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (pieces[r][c] instanceof King &&
                    pieces[r][c].isWhite() == white) {
                    kingRow = r;
                    kingCol = c;
                }

        // Check attacks
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                if (pieces[r][c] != null &&
                    pieces[r][c].isWhite() != white) {

                    // Special Pawn attack logic
                    if (pieces[r][c] instanceof Pawn) {

                        int direction =
                                pieces[r][c].isWhite() ? -1 : 1;

                        if (kingRow == r + direction &&
                           (kingCol == c + 1 ||
                            kingCol == c - 1)) {
                            return true;
                        }

                    } else if (pieces[r][c].isValidMove(
                            pieces, r, c, kingRow, kingCol)) {

                        return true;
                    }
                }
            }
        }

        return false;
    }

    // ================= CHECKMATE =================
// ================= HIGHLIGHT MOVES =================

private void highlightMoves(int sr, int sc) {

    for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {

            if (pieces[sr][sc].isValidMove(pieces, sr, sc, r, c) &&
                (pieces[r][c] == null ||
                 pieces[r][c].isWhite() != pieces[sr][sc].isWhite())) {

                // TEMP MOVE to check king safety
                Piece moving = pieces[sr][sc];
                Piece captured = pieces[r][c];

                pieces[r][c] = moving;
                pieces[sr][sc] = null;

                boolean inCheck = isKingInCheck(moving.isWhite());

                pieces[sr][sc] = moving;
                pieces[r][c] = captured;

                if (!inCheck) {
board[r][c].setBackground(new Color(196, 189, 139)); // green fill
board[r][c].setBorder(
        BorderFactory.createLineBorder(new Color(0, 0, 0), 1)); // dark green border
                        }
            }
        }
    }
}
// ================= RESET COLORS =================

private void resetBoardColors() {

    for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {

            if ((r + c) % 2 == 0)
                board[r][c].setBackground(new Color(240, 217, 181));
            else
                board[r][c].setBackground(new Color(181, 136, 99));

            board[r][c].setBorder(null);
        }
    }
}
    private boolean isCheckmate(boolean white) {

        if (!isKingInCheck(white))
            return false;

        for (int sr = 0; sr < 8; sr++) {
            for (int sc = 0; sc < 8; sc++) {

                if (pieces[sr][sc] != null &&
                    pieces[sr][sc].isWhite() == white) {

                    for (int er = 0; er < 8; er++) {
                        for (int ec = 0; ec < 8; ec++) {

                            if (pieces[sr][sc].isValidMove(
                                    pieces, sr, sc, er, ec) &&
                                (pieces[er][ec] == null ||
                                 pieces[er][ec].isWhite() != white)) {

                                Piece moving = pieces[sr][sc];
                                Piece captured = pieces[er][ec];

                                pieces[er][ec] = moving;
                                pieces[sr][sc] = null;

                                boolean stillCheck =
                                        isKingInCheck(white);

                                pieces[sr][sc] = moving;
                                pieces[er][ec] = captured;

                                if (!stillCheck)
                                    return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGame::new);
    }
}