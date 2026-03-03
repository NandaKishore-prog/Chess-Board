import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChessGame extends JFrame {

    private JButton[][] board = new JButton[8][8];
private Piece[][] pieces = new Piece[8][8];
private boolean whiteTurn = true;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public ChessGame() {
        setTitle("Java Swing Chess");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        initializeBoard();
        initializePieces();
        updateBoard();

        setVisible(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                JButton square = new JButton();
                square.setFocusPainted(false);

                if ((row + col) % 2 == 0) {
                    square.setBackground(new Color(240, 217, 181)); // Light
                } else {
                    square.setBackground(new Color(181, 136, 99)); // Dark
                }

                final int r = row;
                final int c = col;

                square.addActionListener(e -> handleClick(r, c));

                board[row][col] = square;
                add(square);
            }
        }
    }

private void initializePieces() {

    // Black pieces
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

    // White pieces
    pieces[7][0] = new Rook(true);
    pieces[7][1] = new Knight(true);
    pieces[7][2] = new Bishop(true);
    pieces[7][3] = new Queen(true);
    pieces[7][4] = new King(true);
    pieces[7][5] = new Bishop(true);
    pieces[7][6] = new Knight(true);
    pieces[7][7] = new Rook(true);
}

   private void updateBoard() {
    for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {

            if (pieces[row][col] != null) {

                String imagePath = "pieces/" +
                        pieces[row][col].getImageName() + ".png";

                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage()
                        .getScaledInstance(80, 80, Image.SCALE_SMOOTH);

                board[row][col].setIcon(new ImageIcon(img));

            } else {
                board[row][col].setIcon(null);
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
            board[row][col].setBorder(
                    BorderFactory.createLineBorder(Color.YELLOW, 3));
        }

    } else {

        Piece selectedPiece = pieces[selectedRow][selectedCol];

        if (selectedPiece != null &&
                selectedPiece.isValidMove(pieces, selectedRow, selectedCol, row, col)) {

            // Don't allow capturing own piece
            if (pieces[row][col] == null ||
                    pieces[row][col].isWhite() != selectedPiece.isWhite()) {

                pieces[row][col] = selectedPiece;
                pieces[selectedRow][selectedCol] = null;

                whiteTurn = !whiteTurn; // switch turn
            }
        }

        board[selectedRow][selectedCol].setBorder(null);
        selectedRow = -1;
        selectedCol = -1;

        updateBoard();
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGame::new);
    }
}