package application;

import java.util.Scanner;

import chest.ChessMatch;
import chest.ChessPiece;
import chest.chessPosition;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		ChessMatch mat = new ChessMatch();
		while(true) {
           UI.printBoard(mat.getPieces());
           System.out.println();
           System.out.print("Source: ");
           chessPosition source = UI.readChessPosition(sc);
           
           System.out.println();
           System.out.print("Target: ");
           chessPosition target = UI.readChessPosition(sc);
           
           ChessPiece capturedPiece = mat.performChessMove(source, target);
		}
	}
}
