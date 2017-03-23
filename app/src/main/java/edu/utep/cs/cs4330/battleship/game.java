package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//Thomas Payan and Alfred Cazares
public class game extends AppCompatActivity {

    private GameBoard board;//board used for computer ships
    public static Board secondBoard;//board used for the players ships
    private BoardView boardView;//board view for computer
    public static PlayerBoardView playerBoard;//board view for player
    private final Paint ya=new Paint(Paint.ANTI_ALIAS_FLAG);;
    int shot=0;//keeps track of the number of shots
    boolean [] shipsplace;//saves the players ships saved in bundle
    boolean [][] twoDShipsPlace= new boolean [10][10];//convert the players ships to 2-d array


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        final TextView label2 = (TextView) findViewById(R.id.s);
        final Button newGame = (Button) findViewById(R.id.newButton);
        final TextView label = (TextView) findViewById(R.id.numShot);
        final TextView player = (TextView) findViewById(R.id.playersTurn);
        board = new GameBoard(10);
        secondBoard= new Board(10);

        //this is for comp
        boardView = (BoardView) findViewById(R.id.boardView);
        boardView.setBoard(board);

        //this is for player
        playerBoard=(PlayerBoardView) findViewById(R.id.boardView2);
        playerBoard.setBoard(secondBoard);

        ya.setColor(Color.WHITE);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shipsplace= extras.getBooleanArray("playerShips");//saves to 1-d array

            for(int r=0;r<10;r++){
                for(int c=0;c<10;c++){
                    twoDShipsPlace[r][c]=false;//make sure empty first
                }
            }
            twoDShipsPlace=singleToDoubleArray(shipsplace);//convert to 2-d

            playerBoard.ships=twoDShipsPlace;//give value to playerBoardView
            for(int a=0;a<=9;a++){
                for(int b=0;b<=9;b++){
                    if(playerBoard.ships[a][b]== true)
                        secondBoard.health++;
                }
            }

        }


        //when select new game
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<10;i++){
                    for(int j=0;j<10;j++){
                        secondBoard.hasHitBoard[i][j]=false;
                        secondBoard.hasShotBoard[i][j]=false;
                        PlayerBoardView.gameBoard.randomBoard[i][j]=false;
                        
                    }
                }
                playerBoard.invalidate();
                Intent i=new Intent (game.this,MainActivity.class);
                startActivity(i);
            }
        });

        boardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
            @Override
            public void onTouch(int x, int y) {
                shot++;
                if(board.health<=0)
                    label2.setText("!!Player Wins!!");
                else{
                    label2.setText("!!comp wins!!");
                }
                if(board.health>0&& secondBoard.health>0) {
                    label2.setText("Cmp hp:" + board.health + ",Plyr hp:"+secondBoard.health);
                    //}
                    //label2.setText("ships health:"+board.s.getHealth()+","+board.s2.getHealth()+","+board.s3.getHealth()+","+board.s4.getHealth()+","+board.s5.getHealth()+",");
                }
                label.setText("Number of Shots: "+shot);
                boardView.playerTurn = 1;
                player.setText("Player " + boardView.playerTurn +"'s turn");
                toast(String.format("Touched: %d, %d", x, y));
            }
        });
    }

    //the next two methods save and restore on screen orientation
    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putBooleanArray("places",shipsplace);//saves ships places
        outState.putInt("shotCount",shot);
        boolean [] newBoardHits=doubleToSingleArray(board.hits);
        boolean [] newBoardShips=doubleToSingleArray(board.board);
        boolean [] newBoardViewHits= doubleToSingleArray(boardView.hits);
        boolean [] newSecondBoardHits=doubleToSingleArray(secondBoard.hits);
        boolean [] newSecondBoardBoard=doubleToSingleArray(secondBoard.board);
        outState.putBooleanArray("boardHits",newBoardHits);
        outState.putBooleanArray("boardShips",newBoardShips);
        outState.putBooleanArray("bvHits",newBoardViewHits);
        outState.putBooleanArray("secondBoardHits",newSecondBoardHits);
        outState.putBooleanArray("secondBoardBoard",newSecondBoardBoard);
        outState.putInt("oppHealth",board.health);
        outState.putInt("plyrHealth",secondBoard.health);

        outState.putInt("playerTurn",boardView.playerTurn);
        super.onSaveInstanceState(outState);

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        shot=savedInstanceState.getInt("shotCount");
        final TextView label = (TextView) findViewById(R.id.numShot);
        final TextView player2 = (TextView) findViewById(R.id.playersTurn);
        label.setText("Number of Shots: "+shot);

        boolean []restoreBoardHits=savedInstanceState.getBooleanArray("boardHits");
        boolean []restoreBoardShips=savedInstanceState.getBooleanArray("boardShips");
        boolean []restoreBoardViewHits=savedInstanceState.getBooleanArray("bvHits");
        boolean [] restoreSecondBoardHits=savedInstanceState.getBooleanArray("secondBoardHits");
        boolean [] restoreSecondBoardBoard=savedInstanceState.getBooleanArray("secondBoardBoard");
        int savedPlayerTurn = savedInstanceState.getInt("playerTurn");

        board.hits=singleToDoubleArray(restoreBoardHits);
        board.board=singleToDoubleArray(restoreBoardShips);
        boardView.hits=singleToDoubleArray(restoreBoardViewHits);
        board.health=savedInstanceState.getInt("oppHealth");
        secondBoard.health=savedInstanceState.getInt("plyrHealth");
        secondBoard.hits=singleToDoubleArray(restoreSecondBoardHits);
        secondBoard.board=singleToDoubleArray(restoreSecondBoardBoard);
        final TextView label2= (TextView) findViewById(R.id.s);
        label2.setText("cmp hp:" + board.health+",plyr hp:"+secondBoard.health);
        player2.setText("Player " + savedPlayerTurn +"'s turn");
        //player2.setText("You Suck");
        boolean [] place=savedInstanceState.getBooleanArray("places");
        for(int r=0;r<10;r++){
            for(int c=0;c<10;c++){
                twoDShipsPlace[r][c]=false;
            }
        }
        twoDShipsPlace=singleToDoubleArray(place);
        playerBoard.ships=twoDShipsPlace;



    }
    public boolean [] doubleToSingleArray(boolean [][] old){
        int counting=0;
        boolean [] newArray= new boolean [100];
        for(int x=0;x<10;x++){
            for (int y=0;y<10;y++){
                newArray[counting]=old[x][y];
                counting++;
            }
        }
        return newArray;
    }
    public boolean [][] singleToDoubleArray(boolean [] old){
        int traverse=0;int newx=0;int newy=0;
        boolean [][] newArray=new boolean [10][10];
        while(traverse<100){
            if(newy==10){
                newy=0;
            }
            newArray[newx][newy]=old[traverse];
            traverse++;
            if(traverse%10==0) {
                newx++;
            }
            newy++;
        }
        return newArray;
    }

    public void setText(){
        final TextView player = (TextView) findViewById(R.id.playersTurn);
        player.setText("Player " + boardView.playerTurn +"'s turn");
    }


    /** Show a toast message. */
    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
