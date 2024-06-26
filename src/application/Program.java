package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chest.ChessMatch;
import chest.ChessPiece;
import chest.chessException;
import chest.chessPosition;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		ChessMatch mat = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<>();
		
		while(!mat.getCheckMate()) {
		   try {
			   UI.clearScreen();
	           UI.printMatch(mat, captured);
	           System.out.println();
	           System.out.print("Source: ");
	           chessPosition source = UI.readChessPosition(sc);
	           
	           boolean[][] possibleMoves = mat.possibleMoves(source);
	           UI.clearScreen();
	           UI.printBoard(mat.getPieces(), possibleMoves);
	           
	           System.out.println();
	           System.out.print("Target: ");
	           chessPosition target = UI.readChessPosition(sc);
	           
	           ChessPiece capturedPiece = mat.performChessMove(source, target);
	           
	           if(capturedPiece != null) {
	        	   captured.add(capturedPiece);
	           }
	           
	           if(mat.getPromoted() != null) {
	        	   System.out.print("Enter piece for promotion (B/N/R/Q): ");
	        	   String type = sc.next().toUpperCase();
	        	   while(!type.equals("B")&&!type.equals("N")&&!type.equals("Q")&&!type.equals("R")) {
	        		   System.out.print("Invalid value! Enter piece for promotion (B/N/R/Q): ");
		        	   type = sc.next().toUpperCase();
	        	   }
	        	   mat.replacePromotedPiece(type);
	           }
	        	   
	           
		   }catch(chessException e) {
			   System.out.println(e.getMessage());
			   sc.nextLine();
			   sc.nextLine();
		   }catch(InputMismatchException e1) {
			   System.out.println(e1.getMessage());
			   sc.nextLine();
			   sc.nextLine();
		   }
	   }
		UI.clearScreen();
		UI.printMatch(mat, captured);
	}
}
