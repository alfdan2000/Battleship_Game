package edu.utep.cs.cs4330.battleship;
import android.media.MediaPlayer;

import java.util.Arrays;
import java.util.Random;
/*
This class used soley for the computer ships and has methods for placing those comp ships and placing hits on comp ships

 */

public class GameBoard {
    /**
     * Size of this board. This board has
     * <code>size*size </code> places.
     */

    private final int size;
    //static Boolean[] ships = new Boolean[5]; // Added new for random ships
    //static Boolean[][]shipBoard;   // added new for random ships

    /** Create a new board of the given size. */
    public GameBoard(int size) {
        this.size = size;
    }

    /** Return the size of this board. */
    public int size() {
        return size;
    }

    public int randomStrat;

    public int health;//health of computer ships
    public boolean[][] hits=new boolean[10][10];
    public boolean [][]board=new boolean[10][10];        //2-d representation of actual board

    /** Creates 5 ships of different names and sizes */
    public final Ship s= new Ship("Aircraft Carrier",5);
    public final Ship s2= new Ship("Battleship",4);
    public final Ship s3= new Ship("Frigate",3);
    public final Ship s4= new Ship("Submarine",3);
    public final Ship s5=new Ship("Minesweeper",2);


    //an array that keeps track of a taken column
    public boolean [] taken=new boolean [10];


    public void initialize(){
        for(int index=0;index<taken.length;index++){
            this.taken[index]=false;
        }
        for(int col=0;col<=9;col++){
            for(int row=0;row<=9;row++){
                this.board[col][row]=false;
            }
        }
        placeRandom();

    }
    private void placeRandom(){
        int row,col,position;
        int shipCount = 0;
        boolean result;
        boolean allPlaced = false;
        Ship[] shipList;

        shipList = createShipList();




        while(!allPlaced) {
            for(int x = 0; x < 5; x++) {
                if((shipList[x]).getIsPlaced() == false) {
                    row = randomizer(10); //This selects a random row
                    col = randomizer(10); //This selects a random col
                    position = randomizer(4); //This selects a random direction 0-UP, 1-RIGHT, 2-DOWN, 3-RIGHT
                    switch(x){
                        case 0:
                            result = placeShip(shipList[x].getLength(), position, row, col);
                            if(result){
                                shipList[0].shipPlaced();
                                shipCount++;
                            }
                            break;
                        case 1:
                            result = placeShip(shipList[x].getLength(), position, row, col);
                            if(result){
                                shipList[1].shipPlaced();
                                shipCount++;
                            }
                            break;
                        case 2:
                            result = placeShip(shipList[x].getLength(), position, row, col);
                            if(result){
                                shipList[2].shipPlaced();
                                shipCount++;
                            }
                            break;
                        case 3:
                            result = placeShip(shipList[x].getLength(), position, row, col);
                            if(result){
                                shipList[3].shipPlaced();
                                shipCount++;}
                            break;
                        case 4:
                            result = placeShip(shipList[x].getLength(), position, row, col);
                            if(result){
                                shipList[4].shipPlaced();
                                shipCount++;}
                            break;
                    }
                }if(shipCount == 5){
                    allPlaced = true;
                }
            }
        }

    }

    // This method adds the created ships onto an array
    private Ship[] createShipList(){
        Ship[] shipList = new Ship[5];
        shipList[0] = s;
        shipList[1] = s2;
        shipList[2] = s3;
        shipList[3] = s4;
        shipList[4] = s5;
        return shipList;

    }



    public boolean placeShip(int length, int position, int row, int col) {

        final int min = -1;
        final int max = 10;
        int checkCol = col;  // Used to check is any ships are in the way vertical
        int checkRow = row;  //// Used to check is any ships are in the way Horizontal.

        //If UP direction is selected. The squares above are checked for
        //another ship and also to make sure it does not go out of bounds
        if (position == 0 && row >= min + length) {
            for(int checkArea = 0; checkArea < length;checkArea++){
                if(board[checkCol][checkRow--] == true){
                    return false;
                }
            }
            for(int x = 0; x < length; x++) {
                board[col][row--] = true;
            }
            return true;

        }

        //If RIGHT direction is selected. The squares above are checked for
        //another ship and also to make sure it does not go out of bounds
        else if (position == 1 && col <= max - length) {
            for(int checkArea = 0; checkArea < length;checkArea++){
                if(board[checkCol++][checkRow] == true){
                    return false;
                }
            }
            for(int x = 0; x < length; x++) {
                board[col++][row] = true;
            }
            return true;
        }

        //If DOWN direction is selected. The squares above are checked for
        //another ship and also to make sure it does not go out of bounds
        else if(position == 2 && row <= max - length) {
            for(int checkArea = 0; checkArea < length;checkArea++){
                if(board[checkCol][checkRow++] == true){
                    return false;
                }
            }
            for(int x = 0; x < length; x++) {
                board[col][row++] = true;
            }
            return true;
        }

        //If LEFT direction is selected. The squares above are checked for
        //another ship and also to make sure it does not go out of bounds
        else if (position == 3 && col >= min + length) {
            for(int checkArea = 0; checkArea < length;checkArea++){
                if(board[checkCol--][checkRow] == true){
                    return false;
                }
            }
            for(int x = 0; x < length; x++) {
                board[col--][row] = true;
            }
            return true;
        }

        //If unable to place ship with given coordinates nothing is placed
        //and placing ship is retried with different numbers.
        else{
            return false;
        }
    }



    public int randomizer(int range){
        Random random = new Random();
        int num = random.nextInt(range);
        return num;
    }


    /////////
    public boolean[][] placehit(){
        int health=0;
        int newx=((int)Math.round(BoardView.x))/65;// this is used to convert the float into a usable index
        int newy=((int)Math.round(BoardView.y))/65;
        if(newx>=0&&newy>=0&&newx<10&&newy<10) {
            if (board[newx][newy] == true && hits[newx][newy] == false) {// checks if the place selected contains a Ship
                if (randomStrat == 0) {
                    if (s.getPlace() == newx) {
                        health = s.getHealth() - 1;
                        s.setHealth(health);
                    }
                    if (s2.getPlace() == newx) {
                        health = s2.getHealth() - 1;
                        s2.setHealth(health);
                    }
                    if (s3.getPlace() == newx) {
                        health = s3.getHealth() - 1;
                        s3.setHealth(health);
                    }
                    if (s4.getPlace() == newx) {
                        health = s4.getHealth() - 1;
                        s4.setHealth(health);
                    }
                    if (s5.getPlace() == newx) {
                        health = s5.getHealth() - 1;
                        s5.setHealth(health);
                    }
                } else {
                    if (s.getPlace() == newy) {
                        health = s.getHealth() - 1;
                        s.setHealth(health);
                    }
                    if (s2.getPlace() == newy) {
                        health = s2.getHealth() - 1;
                        s2.setHealth(health);
                    }
                    if (s3.getPlace() == newy) {
                        health = s3.getHealth() - 1;
                        s3.setHealth(health);
                    }
                    if (s4.getPlace() == newy) {
                        health = s4.getHealth() - 1;
                        s4.setHealth(health);
                    }
                    if (s5.getPlace() == newy) {
                        health = s5.getHealth() - 1;
                        s5.setHealth(health);
                    }
                }
                if (hits[newx][newy] == false)//if its the first time there is a hit in that place
                    this.health--;// then subtract from the health
                //then update the places you have hit in the 2-d representation of places hit
                hits[newx][newy] = true;

                //for loop is used to redraw the places the ships are hit everytime
                return hits;
            }//end if
        }
        return hits;
    }


    static boolean [][] randomBoard = new boolean[10][10];

    public void computerRandom(){
        int col = randomizer(10);
        int row = randomizer(10);

        randomBoard[row][col] = true;

    }





    // This is supposed to be the smart computer AI.
    static boolean [][] hasHitBoard = new boolean[11][11];
    static boolean [][] hasShotBoard = new boolean[11][11];

    public void computerShot(){

        boolean shotsOnBoard = false;

        for(int row = 0; row < hasHitBoard.length;row++){
            for(int col = 0; col < hasHitBoard[row].length;col++){
                if(hasHitBoard[row][col] == true){
                    shotsOnBoard = true;
                }
            }
        }


	/*
	if there are no shots on the hasHitBoard(computer hasn't taken a turn yet)
	then the computer randomly selects a place to take a shot. Then keeps track
	of the shot in the hasShotBoard
	*/
        if(shotsOnBoard == false){
            int col = randomizer(10);
            int row = randomizer(10);

            //hasHitBoard[row][col] = true;
            hasShotBoard[row][col] = true;
        }


	/*
	If the board already has a shot. Then the computer checks to see if
	there is a hit on the board. If there is it will begin to take shots
	around it until the ship has been sunk
	*/

        else{
            int left,right,up,down;

            for(int row = 0; row < hasHitBoard.length;row++){
                for(int col = 0; col < hasHitBoard[row].length;col++){
                    if(hasHitBoard[row][col] == true){
                        int [] bestDirection = new int [4];

                        left = checkLeft(row, col);
                        bestDirection[0] = left;

                        right = checkRight(row, col);
                        bestDirection[3] = right;

                        up = checkUp(row, col);
                        bestDirection[2] = up;

                        down = checkDown(row, col);
                        bestDirection[1] = down;

                        Arrays.sort(bestDirection);


                        if(left == bestDirection[3]){
                            //hasHitBoard[row][col-left] = true;
                            hasShotBoard[row][col-left] = true;

                        }
                        else if(right == bestDirection[3]){
                            //hasHitBoard[row][col+right] = true;
                            hasShotBoard[row][col+right] = true;
                        }
                        else if(up == bestDirection[3]){
                            //hasHitBoard[row-up][col] = true;
                            hasShotBoard[row-up][col] = true;
                        }
                        else if(down == bestDirection[3]){
                            //hasHitBoard[row+down][col] = true;
                            hasShotBoard[row+down][col] = true;
                        }
                        else{
                            int randomRow = randomizer(10);
                            int randomCol = randomizer(10);

                            //hasHitBoard[randomRow][randomCol] = true;
                            hasShotBoard[randomRow][randomCol] = true;
                        }
                    }
                }
            }
        }

    }

    int checkRight(int row, int col){
        int bestSlot = 1;

        if(col >= 9){
            return 0;
        }

        while((hasHitBoard[row][col++] == hasShotBoard[row][col++]) && col < 10){

            bestSlot++;
            if(col == 9){
                return bestSlot;
            }
        }

        return bestSlot;

    }

    int checkLeft(int row, int col){
        int bestSlot = 1;

        if(col <= 0){
            return 0;
        }

        while((hasHitBoard[row][col--] == hasShotBoard[row][col--]) && col > -1){

            bestSlot++;
            if(col == 0){
                return bestSlot;
            }
        }

        return bestSlot;

    }

    int checkUp(int row, int col){
        int bestSlot = 1;

        if(row <= 0){
            return 0;
        }

        while((hasHitBoard[row--][col] == hasShotBoard[row--][col]) && row > -1){

            bestSlot++;
            if(row == 0){
                return bestSlot;
            }
        }

        return bestSlot;

    }

    int checkDown(int row, int col){
        int bestSlot = 1;

        if(row >= 9){
            return 0;
        }

        while((hasHitBoard[row++][col] == hasShotBoard[row++][col]) && row < 10){

            bestSlot++;
            if(row == 9){
                return bestSlot;
            }
        }

        return bestSlot;

    }
}
