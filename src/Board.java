import java.util.LinkedList;
import java.util.List;

/**
 * Created by John Griffith on 05/02/2015.
 */
class Board {

    private final Integer ROW_SIZE = 3;
    private final Integer COL_SIZE = 3;

    private boolean xTurn = true;

    private List<Position> grid =           new LinkedList<Position>() ;
    private List<Position> freePositions =  new LinkedList<Position>();
    private List<Position> xPositions =     new LinkedList<Position>();
    private List<Position> oPositions =     new LinkedList<Position>();

    public Board(){
        freePositions = restart();
        grid = restart();

    }


    public Position[] blanks(){
        Position arrayBlanks[] = new Position[freePositions.size()];
        int i = 0;
        for(Position position: freePositions){
            arrayBlanks[i] = position;
            i++;
        }
        return arrayBlanks;
    }

    public Position position(String position){
        if(validInput(position)) {
            Position temp = new Position(position.charAt(1)-'1',position.charAt(0) - 'a');
            if (arrayContains(temp, freePositions)>=0) {
                return freePositions.get(arrayContains(temp, freePositions));
            }
        }
        return null;
    }

    private int arrayContains(Position input, List<Position> positions){
        int i=0;
        for(Position position:positions){
            if( position.col()==input.col() && position.row()==input.row() )return i;
            i++;
        }
        return -1;
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
        int place = arrayContains(position, freePositions);
        freePositions.remove(place);
        if(xTurn){
            xMove(position);
        }else{
            oMove(position);
        }
    }

    private void xMove(Position position){
        xTurn = false;
        xPositions.add(position);

    }

    private void oMove(Position position){
        xTurn = true;
        oPositions.add(position);
    }

    private boolean checkWin(List<Position> list){
        int sumX = 0;

        if(list.size()<3) return  false;
        int i = 0;
        int count = 0;
        int y[] = new int[3];
        int d[] = new int[2];

        for (Position p : grid){

            if(arrayContains(p,list)>=0){
                sumX++;
                y[i] ++;
                if(sumX>2)return true;
                if(y[i]>2)return true;
                if( count==2 ||count==4||count == 6) {d[1] ++;if(d[1]>2)return true;}
                if((count % 4 )== 0) {d[0] ++;if(d[0]>2)return true;}
            }

            if(i < 2) {
                i++;
            }else {

                i = 0;
                sumX=0;

            }
            count ++;
        }

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
        if(checkWin(xPositions)){return Player.X;}
        if(checkWin(oPositions)){return Player.O;}
        if(xPositions.size()+oPositions.size()>=grid.size()){return Player.Both;}
        return Player.None;
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        int i = 1;
        for(Position p : grid){

            if( arrayContains( p,  xPositions ) >= 0 ){
                output.append('X');
            }else if( arrayContains( p , oPositions ) >= 0 ) {
                output.append('O');
            }else {
                output.append('.');
            }

            if(i >= COL_SIZE){
                i = 1;
                output.append('\n');
            }else{
                i++;
            }

        }
        return output.toString();
    }




}
