public class Rook extends ChessPiece{
    String[] colors = {"b", "w"};

    public Rook(int initialRow, int initialCol, int pieceColor){
        super(initialRow, initialCol, pieceColor);
	}

    public boolean canMoveTo(int nextRow, int nextCol, ChessBoard board){
		// make sure the move is inside the board
		if(!(nextRow < 8 && nextRow >= 0 && nextCol < 8 && nextCol >= 0)) return false;
		
		// make sure it isnt one of the homeis
		if (board.pieceAt(nextRow, nextCol) != null) 
			if (board.pieceAt(nextRow, nextCol).getColor() == color) 
				return false;
		
		// make sure its a straight move
		if (row-nextRow != 0 && col-nextCol != 0) return false;

		// make sure no people in the way
		// direction of movement
		// start at 0 so if its in the same row it doesnt change the loop val
		int rowDir = 0, colDir = 0;
		if (row < nextRow) rowDir = 1;
		else if (row > nextRow) rowDir = -1;

		if (col < nextCol) colDir = 1;
		else if (col > nextCol) colDir = -1;

		// loop through every piece on the way to the destination
		// you have to do 2 loops because only one will run since either col or row does not change
		for(int tempRow = row + rowDir; tempRow != nextRow; tempRow += rowDir){
			//System.out.println(board.pieceAt(tempRow,col));
			if (board.pieceAt(tempRow, col) != null) return false;
		}
		for(int tempCol = col + colDir; tempCol != nextCol; tempCol += colDir){
			//System.out.println(board.pieceAt(row,tempCol));
			if (board.pieceAt(row, tempCol) != null) return false;
		}

       return true;
    }

    public PieceType getType() 
	{
		return PieceType.ROOK;
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
		return colors[color] + "R";
	}
}