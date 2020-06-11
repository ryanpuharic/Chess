public abstract class ChessPiece {
	public int row;
    public int col;
    public int color; //0 black, 1 white
	public boolean moved; // if the piece has moved
    public boolean[][] availableMoves;


    public ChessPiece(int initialRow, int initialCol, int pieceColor)
    {
        row = initialRow;
        col = initialCol;
        color = pieceColor;
		moved = false;
    }

    /** Method that returns a boolean indicating whether or not the piece can legally move
    *  to the specified location (you need to fill this one in).
    */
    public abstract boolean canMoveTo(int nextRow, int nextCol, ChessBoard board);

    /** Method that returns the pieceType enum of the specific piece
    */
    public abstract PieceType getType();

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public int getColor(){
		return color;
	}
	
	public void setPos(int nextRow, int nextCol){
		row = nextRow;
		col = nextCol;
	}

	public boolean move(int nextRow, int nextCol, ChessBoard board){
		// no need to check for canMove because we check anyways before moving
		board.removePiece(this);
		setPos(nextRow,nextCol);
		board.addPiece(this);
		moved = true;
		return true;
	}

}