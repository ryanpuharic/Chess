public class Bishop extends ChessPiece{
    String[] colors = {"b", "w"};

    public Bishop(int initialRow, int initialCol, int pieceColor){
        super(initialRow, initialCol, pieceColor);
	}

    public boolean canMoveTo(int nextRow, int nextCol, ChessBoard board){
		// make sure the move is inside the board
		if(!(nextRow < 8 && nextRow >= 0 && nextCol < 8 && nextCol >= 0)) return false;

		// make sure it isnt one of the homeis
		if (board.pieceAt(nextRow, nextCol) != null) 
			if (board.pieceAt(nextRow, nextCol).getColor() == color) 
				return false;

		// make sure its not in the same row or anythign
		if (row == nextRow || col == nextCol) return false;
		
		// make sure its diagonal -- row change has to be same as col change
		if (Math.abs(row-nextRow) != Math.abs(col-nextCol)) return false;

		// make sure no people in the way
		int rowDir, colDir; // direction of movement
		if (row < nextRow) rowDir = 1;
		else rowDir = -1;

		if (col < nextCol) colDir = 1;
		else colDir = -1;
        
		// loop through every piece on the way to the destination
		int tempCol = col + colDir;
		for(int tempRow = row + rowDir; tempRow != nextRow; tempRow += rowDir){
			if (board.pieceAt(tempRow, tempCol) != null) return false;
			tempCol += colDir;
		}

		return true;
    }

    public PieceType getType() {
		return PieceType.BISHOP;
	}

    public int getRow()
    {
        return row;
    }

	public int getColor(){
		return color;
	}

    public int getCol()
    {
        return col;
    }

	public String toString(){
		return colors[color]+"B";
	}

}