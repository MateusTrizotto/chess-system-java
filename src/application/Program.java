package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import boardgame.BoardException;
import chest.ChessMatch;
import chest.ChessPiece;
import chest.chessException;
import chest.chessPosition;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		ChessMatch mat = new ChessMatch();
		while(true) {
		   try {
			   UI.clearScreen();
	           UI.printBoard(mat.getPieces());
	           System.out.println();
	           System.out.print("Source: ");
	           chessPosition source = UI.readChessPosition(sc);
	           
	           System.out.println();
	           System.out.print("Target: ");
	           chessPosition target = UI.readChessPosition(sc);
	           
	           ChessPiece capturedPiece = mat.performChessMove(source, target);
		   }catch(chessException e) {
			   System.out.println(e.getMessage());
			   sc.nextLine();
		   }catch(InputMismatchException e1) {
			   System.out.println(e1.getMessage());
			   sc.nextLine();
		   }
		   }
	}
}
