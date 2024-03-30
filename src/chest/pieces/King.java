package chest.pieces;

import boardgame.Board;
import chest.ChessPiece;
import chest.Color;

public class King extends ChessPiece{

	public King(Board board, Color color) {
		super(board, color);
	}

	public String toString() {
		return "K";
	}

	@Override
	public boolean[][] possibleMoves() {
		// TODO Auto-generated method stub
		return null;
	}
}
