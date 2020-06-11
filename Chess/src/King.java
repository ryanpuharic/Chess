public class King extends ChessPiece{
	String[] colors = {"b", "w"};
    public King(int initialRow, int initialCol, int pieceColor)
    {
        super(initialRow, initialCol, pieceColor);
	}

    public boolean canMoveTo(int nextRow, int nextCol, ChessBoard board){
		// make sure the move is inside the board
		if(!(nextRow < 8 && nextRow >= 0 && nextCol < 8 && nextCol >= 0)) return false;
		
		// make sure it isnt one of the homeis
		if (board.pieceAt(nextRow, nextCol) != null) 
			if (board.pieceAt(nextRow, nextCol).getColor() == color) 
				return false;

		// make sure you aren't moving next to a king
		for (int rowChange = -1; rowChange < 2; rowChange++){
			for (int colChange = -1; colChange < 2; colChange++){
				if (rowChange == 0 && colChange == 0) continue; // dont check yourself
				if (!(row + rowChange < 8 && row + rowChange >= 0 && col+colChange < 8 && col+colChange >= 0)) continue; // only check inside the board
				if (board.pieceAt(row + rowChange, col + colChange) instanceof King) return false;			
			}
		}

		// make sure you're not in check with this move
		// simulates the move and if it makes you in check then make it back to original
		ChessPiece nextPiece = board.pieceAt(nextRow,nextCol);
		int tempRow = row;
		int tempCol = col;
		row = nextRow;
		col = nextCol;
		board.addPiece(this);
		int check = board.findCheck(); // once the piece is moved look for check
		// put everything back to normal
		row = tempRow;
		col = tempCol;
		board.addPiece(nextRow,nextCol,nextPiece);
		board.addPiece(this);
		
		// if you are in check
		if (check == color) return false;

		// castle
        if(Math.abs(col-nextCol) == 2 && row-nextRow == 0){ // attempting to move 2 spaces horizontal
			if (moved) return false; // no castle if king moved		
			int colDir;
			if (col < nextCol) colDir = 1; //going right
			else colDir = -1;
			// loop through all the pieces on the way to the edge of the board
			int ogCol = col; // store the original col for reset
			for (tempCol = col; tempCol != 0 && tempCol !=7; tempCol += colDir){
				if (Math.abs(tempCol-col) <= 2){
					// simulate the move to make sure you are not in check
					board.removePiece(this);
					col = tempCol;
					board.addPiece(this);
					check = board.findCheck();
					//System.out.println(board + " " + check);
					// reset position
					board.removePiece(this);
					col = ogCol;
					board.addPiece(this);
					if (check == color) return false;
				}
			}
			ChessPiece rook = board.pieceAt(row, tempCol);
			if (rook instanceof Rook && !rook.moved) { // if the piece at the edge is a rook and has not moved
				// move the rook
				board.removePiece(rook);
				board.addPiece(new Rook(row, nextCol-colDir, color));
				board.pieceAt(row,nextCol-colDir).moved = true;
				return true;
			}
			return false;
		} // end castle

		//otherwise normal move
		return (Math.abs(row-nextRow) <= 1 && Math.abs(col-nextCol) <= 1);
	} // end canMoveTo

    public PieceType getType(){
		return PieceType.KING;
	}

    public int getRow(){
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
		return colors[color] + "K";
	}

}