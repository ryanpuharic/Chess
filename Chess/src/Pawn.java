
public class Pawn extends ChessPiece{
    int originalRow, originalCol; //to check if pawn can move two spaces
    String[] colors = {"b", "w"};

    public Pawn(int initialRow, int initialCol, int pieceColor){
        super(initialRow, initialCol, pieceColor);
        originalRow = initialRow;
        originalCol = initialCol;
	}

    public boolean canMoveTo(int nextRow, int nextCol, ChessBoard board)
    {
		// make sure the move is inside the board
		if(!(nextRow < 8 && nextRow >= 0 && nextCol < 8 && nextCol >= 0)) return false;
		
		// make sure it isnt one of the homes
		if (board.pieceAt(nextRow, nextCol) != null) 
			if (board.pieceAt(nextRow, nextCol).getColor() == color) 
				return false;

		int rowChange = Math.abs(row-nextRow);
		int colChange = Math.abs(col-nextCol);
		int rowDir = nextRow-row; //direction of movement

		// wrong direction
		if (rowDir < 0 && color == 0) return false;
        if (rowDir > 0 && color == 1) return false;

		ChessPiece nextPiece = board.pieceAt(nextRow,nextCol);
		ChessPiece betweenPiece = board.pieceAt(Math.min(nextRow,row)+1, nextCol);

		// if its 2 ahead and there is nothing in between and in a straight line
		if (colChange == 0 && rowChange == 2 && row == originalRow && nextPiece == null && betweenPiece == null) 
			return true;

		// if its a diagonal move check if its an enemay
		if (rowChange == 1 && colChange == 1 && nextPiece != null && nextPiece.getColor() != color) 	
			return true;		

		// otherwise just check if its a normal move
		return colChange == 0 && rowChange == 1 && nextPiece == null;
    }

    public PieceType getType() 
	{
		return PieceType.PAWN;
	}

    public String toString(){
		return colors[color] + "P";
	}

}