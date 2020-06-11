import java.io.*;
import java.util.*;

class Main {
    /** Runs the main menu where you can choose to play chess, test chess, or quit */
	static String[] colors = {"Black", "White"};
    static String[] winners = {"White", "Black"};


    public static void main(String[] args) throws FileNotFoundException
    {
		boolean playAgain = true;
		Scanner scan = new Scanner(System.in);

		System.out.println("=======================================================");
		System.out.println("Welcome to Chess by Tarun and Ryan!");
		System.out.println("When prompted, enter your move in format 'a1 b2'");
		System.out.println("Type 'QUIT' at any point to end the game.");
		System.out.println("=======================================================");
		
		while (playAgain){
			System.out.println("How would you like to play?");
			System.out.print("[1 = normal game | 2 = read board from file | 3 = read moves from file]: ");
			String boardType = scan.nextLine().toLowerCase().trim();
			while (!(boardType.equals("1") || boardType.equals("2") || boardType.equals("3") || boardType.equals("quit"))){
				System.out.println("\nPlease enter a valid input.");
				System.out.println("How would you like to play?");
				System.out.print("[1 = normal game | 2 = read board from file | 3 = read moves from file]: ");
				boardType = scan.nextLine().toLowerCase().trim();
			}
			if (boardType.equals("1")) playChess();
			else if (boardType.equals("2")){
				System.out.print("Enter board file name: ");
				String fileName = scan.nextLine().toLowerCase();
				File file = new File(fileName);
				while (!file.exists() && !fileName.equals("quit")){ // make sure the file exists
					System.out.println("\nThat file does not exist.");
					System.out.print("Enter board file name: ");
					fileName = scan.nextLine();
					file = new File(fileName);
				}
				if (!fileName.equals("quit")) testChess(new ChessBoard(file));
			}
			else if (boardType.equals("3")){
				System.out.print("Enter moves file name: ");
				String fileName = scan.nextLine().toLowerCase();
				File file = new File(fileName);
				while (!file.exists() && !fileName.equals("quit")){ // make sure the file exists
					System.out.println("\nThat file does not exist.");
					System.out.print("Enter moves file name: ");
					fileName = scan.nextLine();
					file = new File(fileName);
				}
				if (!fileName.equals("quit")) playChess(file);			
			}
			String input = "";
			while(!(input.toLowerCase().equals("y") || input.toLowerCase().equals("n"))){
				System.out.print("\nWould you like to play again? (y/n): ");
				input = scan.nextLine();
				if(input.toLowerCase().equals("n")) playAgain = false;
				System.out.println("=======================================================");
			}
		} // end playAgain loop
    } // end main

	/** Where you can test your chess pieces on an otherwise empty board*/
    public static void testChess(ChessBoard board)
    {
        Scanner scan = new Scanner(System.in);

		boolean done = false;
		int checkmate = -1; // color in checkmate

		while (!done){ // single game loop
			//white turn
			done = playerMove(1, board, scan);
			//check game done
			checkmate = board.findCheckmate();
			if (checkmate != -1 || done){
				done = true;
				continue;
			}
			//black turn
			done = playerMove(0, board, scan);
			//check game done
			checkmate = board.findCheckmate();
			if (checkmate != -1 || done){
				done = true;
				continue;
			}
		} // end game loop

		System.out.println(board);
		System.out.println("Game Over");

		if (checkmate != -1)
			System.out.println(String.format("Player %s won!", winners[checkmate]));
    } // end testChess

	public static void playChess(File moves) throws FileNotFoundException{
		// play chess with a list of predetermined moves
		Scanner scan = new Scanner(moves);
		ChessBoard board = new ChessBoard();
		int turnCount = 1;
		String[] playerColors = {"Black", "White"};
		boolean done = false;
		int checkmate = -1;

		System.out.println(board);
		while (scan.hasNext()){ // read through list of moves and execute them
			String input = scan.nextLine();
			int[][] moveCoords = board.moveStringToCoord(input);
			int color = turnCount%2; 
			turnCount += 1;

			if (!(moveCoords != null && board.pieceAt(moveCoords[0][0],moveCoords[0][1]) != null && board.pieceAt(moveCoords[0][0],moveCoords[0][1]).getColor() == color && board.pieceAt(moveCoords[0][0],moveCoords[0][1]).canMoveTo(moveCoords[1][0],moveCoords[1][1],board))){ // validate move
				System.out.println("'"+input+"' is an invalid move for player "+playerColors[color]+".");
				return;
			}
       		board.pieceAt(moveCoords[0][0],moveCoords[0][1]).move(moveCoords[1][0],moveCoords[1][1],board); // do the move

			if(board.findCheck() == color){ // make sure you're not in check
         		System.out.println(String.format("'%s' is an invalid move for player %s. You have to move out of check.", input, playerColors[color]));
				return;
			}

			System.out.print(board);
			System.out.println("Move: "+input+"\n");

			// pawn promotion
			ChessPiece pawnPromotion = board.findPawnPromotion();
			if (pawnPromotion != null){
				Scanner scan2 = new Scanner(System.in);
				System.out.print("What would you like to promote Pawn " + input.substring(0,2) + " to [B, N, R, Q]: ");
				String promoteResponse = scan2.nextLine().toLowerCase();
				String[] pResponses = {"b","n","r","q"};
				while (!Arrays.asList(pResponses).contains(promoteResponse.trim())){
					System.out.println("\nInvalid promotion piece, make sure you choose from [B, N, R, Q]");
					System.out.print("What would you like to promote Pawn " + input.substring(0,2) + " to [B, N, R, Q]: ");
					promoteResponse = scan2.nextLine().toLowerCase();
				}

				// do the promotion
				int nextRow = moveCoords[1][0];
				int nextCol = moveCoords[1][1];
				int nextColor = pawnPromotion.getColor();
				if (promoteResponse.equals("b")) board.addPiece(new Bishop(nextRow, nextCol, nextColor));
				if (promoteResponse.equals("n")) board.addPiece(new Knight(nextRow, nextCol, nextColor));
				if (promoteResponse.equals("r")) board.addPiece(new Rook(nextRow, nextCol, nextColor));
				if (promoteResponse.equals("q")) board.addPiece(new Queen(nextRow, nextCol, nextColor));
			}

			//check game done
			checkmate = board.findCheckmate();
			if (checkmate != -1){
				done = true;
				break;
			}

		} // end read loop

		if (checkmate != -1) done = true;

		// after doing the moves, continue with game normally
		scan = new Scanner(System.in);
		while (!done){ // single game loop
			int color = turnCount%2;
			turnCount += 1;
			done = playerMove(color, board, scan);
			//check game done
			checkmate = board.findCheckmate();
			if (checkmate != -1 || done){
				done = true;
				continue;
			}
		
		} // end game loop

		System.out.println(board);
		System.out.println("Game Over");

		if (checkmate != -1)
			System.out.println(String.format("Player %s won!", winners[checkmate]));

	} // end playChess(moves)

    /** Where your chess game will run */
    public static void playChess() throws FileNotFoundException
    {
		Scanner scan = new Scanner(System.in);
		ChessBoard board = new ChessBoard();

		boolean done = false;
		int checkmate = -1; // color in checkmate

		while (!done){ // single game loop
			//white turn
			done = playerMove(1, board, scan);
			//check game done
			checkmate = board.findCheckmate();
			if (checkmate != -1 || done){
				done = true;
				continue;
			}
			//black turn
			done = playerMove(0, board, scan);
			//check game done
			checkmate = board.findCheckmate();
			if (checkmate != -1 || done){
				done = true;
				continue;
			}
		} // end game loop

		System.out.println(board);
		System.out.println("Game Over");

		if (checkmate != -1)
			System.out.println(String.format("Player %s won!", winners[checkmate]));

    } // end playChess

	public static boolean playerMove(int color, ChessBoard board, Scanner scan){
		//returns boolean whether or not they quit the game	
		System.out.println(board);
		System.out.print(String.format("Player %s, where would you like to move? ", colors[color]));
		String moveResponse = scan.nextLine();
		
		// quit?
		if (moveResponse.toLowerCase().equals("quit")) return true;
		

		// validate move input
		int[][] moveCoords = board.moveStringToCoord(moveResponse);

		while(!(moveCoords != null && board.pieceAt(moveCoords[0][0],moveCoords[0][1]) != null && board.pieceAt(moveCoords[0][0],moveCoords[0][1]).getColor() == color && board.pieceAt(moveCoords[0][0],moveCoords[0][1]).canMoveTo(moveCoords[1][0],moveCoords[1][1],board))){
			System.out.println("\nInvalid move, make sure input is in format 'a1 b2' and is a valid move");
			System.out.print(String.format("Player %s, where would you like to move? ", colors[color]));
			moveResponse = scan.nextLine();
			if (moveResponse.toLowerCase().equals("quit")) return true;
			moveCoords = board.moveStringToCoord(moveResponse);
		}

		ChessPiece capturedPiece = board.pieceAt(moveCoords[1][0],moveCoords[1][1]);
		// do the move once validated
        board.pieceAt(moveCoords[0][0],moveCoords[0][1]).move(moveCoords[1][0],moveCoords[1][1],board);
        if(board.findCheck() == color){ // make sure you're not putting yourself in check
            System.out.println("\nMake sure you move out of check.");
            ChessPiece tempPiece = board.pieceAt(moveCoords[1][0],moveCoords[1][1]); // store og piece
            board.removePiece(board.pieceAt(moveCoords[1][0],moveCoords[1][1]));
			tempPiece.setPos(moveCoords[0][0],moveCoords[0][1]);
            board.addPiece(tempPiece);
			board.addPiece(capturedPiece);
            playerMove(color, board, scan);
        }

		// pawn promotion
		ChessPiece pawnPromotion = board.findPawnPromotion();
		if (pawnPromotion != null){
			System.out.print("What would you like to promote Pawn to [B, N, R, Q]: ");
			String promoteResponse = scan.nextLine().toLowerCase();
			String[] pResponses = {"b","n","r","q"};
			while (!Arrays.asList(pResponses).contains(promoteResponse.trim())){
				System.out.println("\nInvalid promotion piece, make sure you choose from [B, N, R, Q]");
				System.out.print("What would you like to promote Pawn to [B, N, R, Q]: ");
				promoteResponse = scan.nextLine().toLowerCase();
			}

			// do the promotion
			int nextRow = moveCoords[1][0];
			int nextCol = moveCoords[1][1];
			int nextColor = pawnPromotion.getColor();
			if (promoteResponse.equals("b")) board.addPiece(new Bishop(nextRow, nextCol, nextColor));
			if (promoteResponse.equals("n")) board.addPiece(new Knight(nextRow, nextCol, nextColor));
			if (promoteResponse.equals("r")) board.addPiece(new Rook(nextRow, nextCol, nextColor));
			if (promoteResponse.equals("q")) board.addPiece(new Queen(nextRow, nextCol, nextColor));
		}

		return false;
	}

} 