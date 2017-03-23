package edu.utep.cs.cs4330.battleship;
//Thomas Payan and Alfred Cazares
import java.util.Random;
import java.util.Arrays;

/**
This class is soley used for the player ships when they are being placed and after during the game
 and placing hits on player ships
 */
public class Board {

    /**
     * Size of this board. This board has
     * <code>size*size </code> places.
     */
    private final int size;

    /** Create a new board of the given size. */
    public Board(int size) {
        this.size = size;
    }

    /** Return the size of this board. */
    public int size() {
        return size;
    }

    public int randomStrat;

    public int health;//health of player ships
    public boolean[][] hits=new boolean[10][10];
    public boolean [][]board=new boolean[10][10];//2-d representation of actual board

    public Ship s= new Ship("Aircraft Carrier",5);
    public Ship s2= new Ship("Battleship",4);
    public Ship s3= new Ship("Frigate",3);
    public Ship s4= new Ship("Submarine",3);
    public Ship s5=new Ship("Minesweeper",2);


    //an array that keeps track of a taken column
    public boolean [] taken=new boolean [10];


    public void initialize(){
        for(int i=0;i<taken.length;i++){
            this.taken[i]=false;
        }
        for(int l=0;l<=9;l++){
            for(int m=0;m<=9;m++){
                this.board[l][m]=false;
            }
        }

    }

    public boolean[][] placehit(){
        int health=0;
        int newx=((int)Math.round(BoardView.x))/65;// this is used to convert the float into a usable index
        int newy=((int)Math.round(BoardView.y))/65;
        if(newx>=0&&newy>=0&&newx<10&&newy<10) {
            if (board[newx][newy] == true && hits[newx][newy] == false) {// checks if the place selected contains a ship
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

    // This is just a simple random computer AI. It takes a random number
    // between 0 and 9 and uses it to set a square on the randomBoard array as true.
    boolean [][] randomBoard = new boolean[10][10];

    public void computerRandom(){
        int col = randomizer(10);
        int row = randomizer(10);

        randomBoard[row][col] = true;

    }


    // This is supposed to be the smart computer AI.
    boolean [][] hasHitBoard = new boolean[10][10];
    boolean [][] hasShotBoard = new boolean[10][10];

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

            hasHitBoard[row][col] = true;
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
                        bestDirection[1] = right;

                        up = checkUp(row, col);
                        bestDirection[2] = up;

                        down = checkDown(row, col);
                        bestDirection[3] = down;

                        Arrays.sort(bestDirection);


                        if(left == bestDirection[3]){
                            hasHitBoard[row][col-left] = true;
                            hasShotBoard[row][col-left] = true;

                        }
                        else if(right == bestDirection[3]){
                            hasHitBoard[row][col+right] = true;
                            hasShotBoard[row][col+right] = true;
                        }
                        else if(up == bestDirection[3]){
                            hasHitBoard[row-up][col] = true;
                            hasShotBoard[row-up][col] = true;
                        }
                        else if(down == bestDirection[3]){
                            hasHitBoard[row+down][col] = true;
                            hasShotBoard[row+down][col] = true;
                        }
                        else{
                            int randomRow = randomizer(10);
                            int randomCol = randomizer(10);

                            hasHitBoard[randomRow][randomCol] = true;
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

        while(hasHitBoard[row][col++] == hasShotBoard[row][col++] && col < 10){
            bestSlot++;
        }

        return bestSlot;

    }

    int checkLeft(int row, int col){
        int bestSlot = 1;

        if(col <= 0){
            return 0;
        }

        while(hasHitBoard[row][col--] == hasShotBoard[row][col--] && col > -1){
            bestSlot++;
        }

        return bestSlot;

    }

    int checkUp(int row, int col){
        int bestSlot = 1;

        if(row <= 0){
            return 0;
        }

        while(hasHitBoard[row--][col] == hasShotBoard[row--][col] && row > -1){
            bestSlot++;
        }

        return bestSlot;

    }

    int checkDown(int row, int col){
        int bestSlot = 1;

        if(row >= 9){
            return 0;
        }

        while(hasHitBoard[row++][col] == hasShotBoard[row++][col] && row < 10){
            bestSlot++;
        }

        return bestSlot;

    }

    public int randomizer(int range){
        Random random = new Random();
        int num = random.nextInt(range);
        return num;
    }







}
