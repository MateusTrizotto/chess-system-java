package chest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chest.pieces.Bishop;
import chest.pieces.King;
import chest.pieces.Knight;
import chest.pieces.Pawn;
import chest.pieces.Quenn;
import chest.pieces.Rook;

public class ChessMatch {
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	private ChessPiece promoted;
	
	public ChessMatch() {
		board = new Board(8,8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
	public int getTurn() {
		return this.turn;
	}
	
	public Color getCurrentPlayer() {
		return this.currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for(int i = 0; i<mat.length;i++) {
			for(int j = 0;j<mat.length;j++) {
				mat[i][j]= (ChessPiece)board.piece(i, j);
			}
		}
		return mat;
	}
	
	private void initialSetup() {
		 placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		 placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		 placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		 placeNewPiece('d', 1, new Quenn(board, Color.WHITE));
	        placeNewPiece('e', 1, new King(board, Color.WHITE));
	        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
	        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
	        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
	        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

	        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
	        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
	        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
	        placeNewPiece('d', 8, new Quenn(board, Color.BLACK));
	        placeNewPiece('e', 8, new King(board, Color.BLACK));
	        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
	        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
	        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
	        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
	}
	
	public boolean[][] possibleMoves(chessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	
	public ChessPiece performChessMove(chessPosition sourcePosition, chessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new chessException("You can't put yourself on check!");
		}
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		//Special move promotion.
		promoted = null;
		if(movedPiece instanceof Pawn) {
			if(movedPiece.getColor()==Color.WHITE && target.getRow()==0||movedPiece.getColor()==Color.BLACK && target.getRow()==7) {
			promoted = (ChessPiece)board.piece(target);
			promoted = replacePromotedPiece("Q");
			}
		}
		
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}else {
			nextTurn();
		}
		return (ChessPiece)capturedPiece;
	}
	
	public ChessPiece replacePromotedPiece(String type) {
		if(promoted == null) {
			throw new IllegalStateException("There is no piece to be promoted!");
		}
		if(!type.equals("B")&&!type.equals("N")&&!type.equals("Q")&&!type.equals("R")) {
			return promoted;
		}
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	}
	
	private ChessPiece newPiece(String type, Color color) {
		if(type.equals("B")) return new Bishop(board, color);
		if(type.equals("N")) return new Knight(board, color);
		if(type.equals("Q")) return new Quenn(board, color);
		return new Rook(board, color);
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream()
				.filter(p -> ((ChessPiece)p).getColor() == color)
				.collect(Collectors.toList());
		for(Piece p: list) {
			if(p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no king of color "+color+" to be find");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream()
				.filter(x -> ((ChessPiece)x).getColor() == opponent(color))
				.collect(Collectors.toList());
		for(Piece p: opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseCount();
		Piece cap = board.removePiece(target);
		board.placePiece(p, target);
		
		if(cap != null) {
			piecesOnTheBoard.remove(cap);
			capturedPieces.add(cap);
		}
		return cap;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseCount();
		board.placePiece(p, source);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) {
			throw new chessException("There is no capable position");
		}
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new chessException("You can't move opponent pieces");
		}
		if(!board.piece(position).isThereAnyPossibelMove()) {
			throw new chessException("There is no possible mive");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new chessException("The chosen piece can't move to the target choosen");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer==Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new chessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	private boolean testCheckMate(Color color) {
		if(!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream()
				.filter(p -> ((ChessPiece)p).getColor() == color)
				.collect(Collectors.toList());
		for(Piece p: list) {
			boolean[][] mat = p.possibleMoves();
			for(int i = 0; i<board.getRows();i++) {
				for (int j = 0; j<board.getColumns();j++) {
					if(mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testcheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if(!testcheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}
