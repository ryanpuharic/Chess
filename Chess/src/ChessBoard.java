import java.io.*;
import java.util.*;
public class ChessBoard {
	/** Constructor for the ChessBoard class (implement a 2D array) */
	private ChessPiece[][] board = new ChessPiece[8][8];
    boolean[] hasChecked = new boolean[2];
   
	public ChessBoard(){
		// instantiate board array
        fillBoard();
		
	}

    public void fillBoard(){
        board[0][0] = new Rook(0,0,0);
		board[0][7] = new Rook(0,7,0);
		board[7][0] = new Rook(7,0,1);
		board[7][7] = new Rook(7,7,1);
	
        board[0][1] = new Knight(0,1,0);
        board[0][6] = new Knight(0,6,0);
        board[7][1] = new Knight(7,1,1);
        board[7][6] = new Knight(7,6,1);

		board[0][2] = new Bishop(0,2,0);
		board[0][5] = new Bishop(0,5,0);
        board[7][2] = new Bishop(0,2,1);
		board[7][5] = new Bishop(0,5,1);
        
        board[0][3] = new Queen(0,3,0);
        board[0][4] = new King(0,4,0);
        board[7][3] = new Queen(7,3,1);
        board[7][4] = new King(7,4,1);

        for(int i = 0; i < 8; i++)
        {
            board[1][i] = new Pawn(1,i,0);
            board[6][i] = new Pawn(6,i,1);
        }
    }

    public ChessBoard(File f) throws FileNotFoundException
	{

        ArrayList<String> input = new ArrayList<String>();

        try {
            Scanner scan = new Scanner(f);
            int count = 0;
            while(scan.hasNext()) {
                input.add(scan.next());
            }

            for(int i = 0; i < input.size(); i++){
                int newColor = 0;
                if(input.get(i).charAt(0) == 'w')
                    newColor = 1;
                if(input.get(i).charAt(1) == 'P')
                    board[i/8][i%8] = new Pawn(i/8,i%8,newColor);
                if(input.get(i).charAt(1) == 'R')
                    board[i/8][i%8] = new Rook(i/8,i%8,newColor);
                if(input.get(i).charAt(1) == 'N')
                    board[i/8][i%8] = new Knight(i/8,i%8,newColor);
                if(input.get(i).charAt(1) == 'B')
                    board[i/8][i%8] = new Bishop(i/8,i%8,newColor);
                if(input.get(i).charAt(1) == 'Q')
                    board[i/8][i%8] = new Queen(i/8,i%8,newColor);
                if(input.get(i).charAt(1) == 'K')
                    board[i/8][i%8] = new King(i/8,i%8,newColor); 
            }
        } catch (FileNotFoundException e) {
			// pee marinade   
        }
		
	}

    /** Returns the ChessPiece currently residing at the specified row and 
	 * column. If no piece exists at the specified location, should return 
	 * null.
	 */
	public ChessPiece pieceAt(int row, int col){
		return board[row][col];	// This should eventually do something more than return null
	}

    /** Adds the specified ChessPiece to the ChessBoard (hint: ChessPieces know their
	 * own rows and columns. You can use this to figure out where to place the piece)
	 */
	public void addPiece(int row, int col, ChessPiece piece){
		board[row][col] = piece;
	}

    public void addPiece(ChessPiece piece){
		board[piece.getRow()][piece.getCol()] = piece;
	}


	/** Removes the piece at the specified location from the board.
	 */
	public void removePiece(ChessPiece piece){
		// You fill this in.
		board[piece.getRow()][piece.getCol()] = null;
	}

	public void removePiece(int row, int col, ChessPiece piece){
		board[row][col] = null;
	}

    public ChessPiece[][] getBoard(){
        return board;
    }

	public int[][] findKings(ChessPiece[] pieces){
		// get king locations in the format [[pee,pee], [poo,poo]]
		int[][] kingLocs = new int[2][2];

		for (ChessPiece p : pieces){
			if(p.getClass().getName().equals("King")){
				kingLocs[p.getColor()][0] = p.getRow();
				kingLocs[p.getColor()][1] = p.getCol();
			}
		}
		
		return kingLocs;
	}

	public int findCheck(){
		// returns the color of the king in check, -1 if neither
		ChessPiece[] pieces = findPieces();

		// king locations peëpé fart juice marinade
		int[][] kingLocs = findKings(pieces);

		// loop through each tile for every piece
		for (ChessPiece piece : pieces){
			for (int color = 0; color < 2; color++){
				if (!piece.getClass().getName().equals("King") && piece.canMoveTo(kingLocs[color][0],kingLocs[color][1],this)){
                    hasChecked[color] = true;
					return color;
                }
			}
		}
		return -1;
    }

	public ChessPiece findPawnPromotion(){
		// returns object of pawn which needs to be promoted, null if no promotions
		int[] checkRows = {0,7}; // rows we're checking for pawns

		for (int row : checkRows){
			for(int col = 0; col < 8; col++){
				ChessPiece piece = pieceAt(row,col);
				if (piece != null && piece.getClass().getName().equals("Pawn")) return piece;
			}
		}
		return null;
	}

	public int findCheckmate(){
		// returns color of king in checkmate, -1 if neither
		ChessPiece[] pieces = findPieces();

		// color of king in check
		int check = findCheck();

		// if you're not in check
		if (check == -1) return -1;

		int[][] kingLocs = findKings(pieces);
		int[] checkLoc = kingLocs[check]; // location of king in check

		ChessPiece king = pieceAt(checkLoc[0], checkLoc[1]);
		int kingRow = king.getRow();
		int kingCol = king.getCol();

		// try to move anywhere
		for (int rowChange = -1; rowChange < 2; rowChange++){
			for (int colChange = -1; colChange < 2; colChange++){
				if (rowChange == 0 && colChange == 0) continue;
				if (!(kingRow + rowChange < 8 && kingRow + rowChange >= 0 && kingCol+colChange < 8 && kingCol+colChange >= 0)) continue;
				if (king.canMoveTo(kingRow + rowChange, kingCol + colChange, this)) return -1; // not in checkmate if you can move here				
			}
		}

		// check if any piece can save you
		//loop through every tile and get same color pieces
		for (int row = 0; row < 8; row++){
			for (int col = 0; col < 8; col++){
				ChessPiece piece = this.pieceAt(row,col);
				if (piece != null && piece.getColor() == king.getColor()){ // same color as checked king
					// move to every tile and see if that removes check
					int ogRow = piece.getRow();
					int ogCol = piece.getCol();
					for (int checkRow = 0; checkRow < 8; checkRow++){
						for (int checkCol = 0; checkCol < 8; checkCol++){
							if (piece.canMoveTo(checkRow, checkCol, this)){
								ChessPiece nextPiece = this.pieceAt(checkRow,checkCol);
								piece.setPos(checkRow, checkCol);
								this.addPiece(piece);
								check = this.findCheck(); // once the piece is moved look for check
								// put everything back to normal
								piece.setPos(ogRow,ogCol);
								this.addPiece(checkRow,checkCol,nextPiece);
								this.addPiece(piece);
								if (check != king.getColor()) return -1;
							}
						}
					}
				}
			}
		}

		return king.getColor();
	}

	public int[][] moveStringToCoord(String input){
		// converts a move such as "a1 a2" to an array of coordinates such as ((1,1),(1,2))
		if (input.length() > 5) return null;

		String[] abc = "abcdefgh".split("(?!^)"); // split string into array of its chars
		String[] splitInput = input.split(" ");
		if (splitInput.length != 2 || (splitInput[0].length() != 2 || splitInput[1].length() != 2)) return null;
		try {
			Integer.parseInt(splitInput[0].charAt(1) + "");
			Integer.parseInt(splitInput[1].charAt(1) + "");
		}
		catch (NumberFormatException e) {
			return null; // if second numbers are not int
		}
		int x = 8-Integer.parseInt(splitInput[0].charAt(1) + "");
		int y = 8-Integer.parseInt(splitInput[1].charAt(1) + "");
		if (x < 0 || x > 8 || y < 0 || y > 8) return null; // out of bounds
		int[][] ret = {
			{x,0},
			{y,0}
			};
		

		int hits = 0; // for coord validation

		// alphabet to int
		for(int i = 0; i < 8 && hits != 2; i++){
			if (abc[i].equals((splitInput[0].charAt(0) + "").toLowerCase())){
				ret[0][1] = i;
				hits += 1;
			}
			if (abc[i].equals((splitInput[1].charAt(0) + "").toLowerCase())){
				ret[1][1] = i;
				hits += 1;
			}
		}

		if(hits < 2) return null;

		return ret;
	}

    public ChessPiece[] findPieces(){
		// get all pieces on the board and return as an array
        ArrayList<ChessPiece> pieceList = new ArrayList<ChessPiece>();
        
        for(int r = 0; r < 8; r++){ //add pieces to list
            for(int c = 0; c < 8; c++){
                if(board[r][c] != null)
                    pieceList.add(board[r][c]);
            }     
        }

        ChessPiece[] result = new ChessPiece[pieceList.size()];
        for(int i = 0; i < pieceList.size(); i++) //convert to array
            result[i] = pieceList.get(i);
        
        return result; 
    }

    public void reset(){
        board = new ChessPiece[8][8];
        fillBoard();
    }

    public String toString(){
        String result = "   _______________________________________\n";
        for(int i = 0; i < 8; i++){ //my credit card didnt go thru im ballin i feel like im goku yeah come again ballin i feel like im goku, goku, goku,goku, goku, YEEEEEEEAAAAAAAHH
            result += 8-i +" ";
            for(int j = 0; j< 8; j++){
				String pieceStr;

				if (board[i][j] == null) pieceStr = "  ";
				else pieceStr = board[i][j].toString();
				
                result +="| " +pieceStr+ " ";
            }
            
            result+= "|\n";
            if(i<7)
                result+= "  |----|----|----|----|----|----|----|----|\n";
        }
		result +=    "   ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\n";
        result +=    "    A    B    C    D    E    F    G    H\n";
		return result;
	
	}

}