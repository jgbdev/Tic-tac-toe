import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by John Griffith on 05/02/2015.
 */
class Board {

    private final Integer ROW_SIZE = 3;
    private final Integer COL_SIZE = 3;

    private Player nextPlayer ;
    private Player[][] board;

    //Debug
    long calculations;
    Boolean debug = false;

    // Pruning variables
    boolean pruning;

    public Board(Boolean pruning) {

        board = new Player[3][3];
        nextPlayer = Player.X;
        for(int x = 0 ; x < ROW_SIZE;x++){
            for(int y = 0; y < COL_SIZE ; y++){
                board[x][y] = Player.None;
            }
        }
        this.pruning = pruning;
    }

    private int getScore(Player player, Player[][] bb){
        calculations++;
        List<Integer> scores = new ArrayList<>();
        Player winner = winner(bb);
        if(winner.equals(nextPlayer)) {
            return 1;
        }

        if(winner.equals(Player.Both)){
            return 0;
        }

        if(!winner.equals(player.None)){
            return -1;
        }

        //Iterate through possible moves
        for( int x = 0; x<COL_SIZE ; x++ ){
            for( int y = 0; y<ROW_SIZE ; y++ ){
                if( bb[x][y]==Player.None ){

                    bb[x][y] = player;



                    Integer score = getScore( player.equals(Player.O) ? Player.X : Player.O , bb );
                    bb[x][y] = Player.None;
                    if(pruning) {
                        if(player.equals(nextPlayer)){
                            if(score>0)return score;
                        }else{
                            if(score<0)return score;
                        }
                    }

                    scores.add(score);


                }
            }
        }

        if(player.equals(nextPlayer)){
            return Collections.max(scores);
        }else{
            return Collections.min(scores);
        }
    }

    public Position suggest(){
        List<Position> positions = new ArrayList<Position>();
        List<Integer> scores = new ArrayList<Integer>();
        calculations = 0L;

        //Iterate through possible moves
        for(int x = 0; x < COL_SIZE; x++){
            for(int y = 0; y<ROW_SIZE; y++){
                if(board[x][y]==Player.None){

                    board[x][y] = nextPlayer;

                    Integer score = getScore(nextPlayer.equals(Player.O) ? Player.X : Player.O, board );
                    if(pruning && (score>0)){
                        board[x][y] = Player.None;
                        if(debug){System.out.printf("Calculations: %d\n",calculations);}
                        return new Position(x,y);
                    }
                    scores.add(score);
                    positions.add(new Position(x,y));

                    board[x][y] = Player.None;
                }
            }
        }

        int j = maxIndex(scores);
        if(debug){System.out.printf("Calculations: %d\n",calculations);}
        return positions.get(j);
    }

    private int maxIndex(List<Integer> integers){
        int max = integers.get(0);

        int j=0;
        int i= 0;
        for(Integer in: integers){
            if(in>max){
                max = in;
                j = i;
            }
            i++;
        }
        return  j;
    }

    public Position[] blanks(){
        List<Position> output = new LinkedList<Position>();
        for(int x = 0; x<COL_SIZE;x++){
            for(int y = 0; y<ROW_SIZE;y++){
                if(board[x][y]==Player.None)output.add(new Position(x,y));
            }
        }
        return output.toArray(new Position[output.size()]);
    }

    public Position position(String position){
        if(validInput(position)) {
            Position temp = new Position(position.charAt(0)-'a',position.charAt(1) - '1');
            if(board[temp.row()][temp.col()]==Player.None)return new Position(temp.row(),temp.col());
        }
        return null;
    }

    private Boolean validInput(String input){
        if( input.length()==2 ){
            if( input.charAt(0)>='a' && input.charAt(0)<='c' ) {
                if( input.charAt(1)>='1' && input.charAt(1)<='3' ){
                    return true;
                }
            }
        }
        return false;
    }

    public void move(Position position){
        board[position.row()][position.col()] = nextPlayer;
        if(nextPlayer==Player.X)nextPlayer=Player.O;
        else nextPlayer=Player.X;
    }

    private boolean checkWin(Player player,Player[][] board){
        int countX = 0;
        int countY[] = {0,0,0};

        for( int x=0 ; x<COL_SIZE ;x++ ){

            for( int y = 0 ; y < ROW_SIZE ; y++ ){

                if(board[x][y] == player) {
                    countX++;
                    countY[y]++;
                    if(countY[y]>2)return true;
                }

                if(countX>2)return true;
            }
            countX = 0;

        }

        if(board[0][0] == player && board[1][1] == player && board[2][2] == player )return true;
        if(board[2][0] == player && board[1][1] == player && board[0][2] == player )return true;


        return false;
    }

    private List restart(){
        List output = new LinkedList();
        for ( int y = 0 ; y < ROW_SIZE ; y++){
            for ( int x = 0; x < COL_SIZE ; x++){
                output.add(new Position(x,y));
            }
        }
        return output;
    }

    public Player winner(){
        if(checkWin(Player.X,board)){return Player.X;}
        if(checkWin(Player.O,board)){return Player.O;}
        if(freePositions(board)==0){return Player.Both;}
        return Player.None;
    }

    private int freePositions(Player[][] bb){
        int i = 0;

        for ( int y = 0 ; y < ROW_SIZE ; y++){
            for ( int x = 0; x < COL_SIZE ; x++){
                if(bb[x][y] == Player.None){i++;}
            }
        }

        return i;
    }

    public Player winner(Player[][] bb){
        if(checkWin(Player.X,bb)){return Player.X;}
        if(checkWin(Player.O,bb)){return Player.O;}
        if(freePositions(bb) == 0){return Player.Both;}
        return Player.None;
    }

    private Character getChar(Player p){

        if(p==Player.X)return 'X';
        if(p==Player.O)return 'O';
        return ' ';
    }

    private Character[] charArgs(){
        Character[] output = new Character[ROW_SIZE*COL_SIZE];
        int i = 0;

        for (int x=0;x<COL_SIZE;x++){
            for(int y=0;y<COL_SIZE;y++){
                output[i] = getChar(board[x][y]);
                i++;
            }


        }
        return output;
    }

    @Override
    public String toString(){
        String s = String.format
                ("     1   2   3\n\n" +
                 " a   %s | %s | %s \n" +
                 "    ---+---+---\n" +
                 " b   %s | %s | %s \n" +
                 "    ---+---+---\n" +
                 " c   %s | %s | %s \n",
                        charArgs()
                        );

        return s.toString();
    }




}
