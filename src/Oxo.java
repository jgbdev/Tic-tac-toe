import java.io.Console;
import java.util.Scanner;
import java.util.Random;

// Play the noughts and crosses game, using the Player, Position and Board
// classes.  This class deals with the user interface and game play.

class Oxo
{
    // The current state of the game is represented by a board object.  A
    // random number generator is used to pick the computer's moves.

    private Board board;
    private Random gen;

    // Start the game.

    public static void main(String[] args)
    {
        Oxo program = new Oxo();
        program.run(args);
    }

    // Find out whether the human player wants to be X or O, and initialise.

    void run(String[] args)
    {

        Boolean pruning = false;
        if(args.length>0){
            pruning = (args[0].charAt(0) == '1');
        }

        System.out.println("Would you like to be X or O?");
        Scanner scanner = new Scanner(System.in);
        String human = scanner.nextLine();
        System.out.println();
        if (human.equals("O")||human.equals("o")) play(scanner, Player.O,pruning);
        else play(scanner, Player.X, pruning);
    }

    // Initialise and loop through pairs of moves, one by the human player (who
    // plays first and is 'X') and one by the computer player.

    void play(Scanner scanner, Player human, Boolean pruning)
    {
        board = new Board(pruning);
        gen = new Random();
        if (human == Player.O) computerMove(board);
        System.out.print(board.toString());
        while (true)
        {
            System.out.println();
            humanMove(scanner, board);
            Player winner = board.winner();
            if (winner != Player.None) stop(winner);
            computerMove(board);
            System.out.println();
            System.out.print(board.toString());
            winner = board.winner();
            if (winner != Player.None) stop(winner);
        }
    }

    // Make a move by the human

    void humanMove(Scanner scanner, Board board)
    {
        Position position = null;
        while (position == null)
        {
            System.out.print("Please type in your move, ");
            System.out.println("a,b or c then 1,2 or 3, e.g. a2");
            String pos = scanner.nextLine();
            position = board.position(pos);
            if (position == null) System.out.println("Bad position");
        }
        board.move(position);
    }

    // Make a move by the computer

    void computerMove(Board board)
    {
        Player p = Player.O;
        Position[] blanks = board.blanks();
        int index = gen.nextInt(blanks.length);
        Position position = blanks[index];
        Position idea  = board.suggest();
        if(idea!=null){
        System.out.println("Suggests" + idea.row() + "  " + idea.col() );
    }
        board.move(idea);
    }

    // Check whether one player has won, print and stop the game if so.

    private void stop(Player winner)
    {
        System.out.println();
        System.out.print(board.toString());
        System.out.println();
        if (winner == Player.X) System.out.println("X wins");
        else if (winner == Player.O) System.out.println("O wins");
        else System.out.println("Draw");
        System.exit(0);
    }
}