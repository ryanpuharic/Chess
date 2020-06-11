public class Knight extends ChessPiece{
    String[] colors = {"b", "w"};

    public Knight(int initialRow, int initialCol, int pieceColor){
        super(initialRow, initialCol, pieceColor);
	}

    public boolean canMoveTo(int nextRow, int nextCol, ChessBoard board)
    {
		// make sure the move is inside the board
		if(!(nextRow < 8 && nextRow >= 0 && nextCol < 8 && nextCol >= 0)) return false;

		// make sure it isnt one of the homeis
		if (board.pieceAt(nextRow, nextCol) != null) 
			if (board.pieceAt(nextRow, nextCol).getColor() == color) 
				return false;

		int diffRow = Math.abs(nextRow-row);
		int diffCol = Math.abs(nextCol-col);

		return (diffRow+diffCol == 3 && diffRow != 0 && diffCol != 0);
    }

    public PieceType getType() 
	{
		return PieceType.KNIGHT;
	}

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }
    
    public int getColor(){
		return color;
	}

    public String toString(){
		return colors[color] + "N";
	}

}