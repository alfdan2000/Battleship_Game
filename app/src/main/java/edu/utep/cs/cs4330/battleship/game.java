package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//Thomas Payan and Alfred Cazares
public class game extends AppCompatActivity {

    private GameBoard board;//board used for computer ships
    public static Board secondBoard;//board used for the players ships
    private BoardView boardView;//board view for computer
    public static PlayerBoardView playerBoard;//board view for player
    private final Paint ya=new Paint(Paint.ANTI_ALIAS_FLAG);;
    int shot=0;//keeps track of the number of shots
    boolean [] shipsplace;//saves the players ships saved in bundle\
    public static boolean [] oppShipsPlace;
    public static boolean [][] twoDOppShipsPlace=new boolean[10][10];
    boolean [][] twoDShipsPlace= new boolean [10][10];//convert the players ships to 2-d array
    public static boolean internet=false;
    boolean isHost=false;
    boolean isClient=false;
    Handler handler;
    private static String LOCAL_HOST ="172.19.159.1";//put your ip address
    //private static String LOCAL_HOST = "opuntia.cs.utep.edu";
    private static final String CHAT_SERVER = LOCAL_HOST;
    private static final int PORT_NUMBER = 8000;
    private Socket socket;
    private ServerSocket serverSocket;
    public static int sentX;
    public static int sentY;
    public static boolean [][] yourShipLocation=new boolean[10][10];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if(intent != null){
            isClient = intent.getBooleanExtra("isClient",false);
            toast("isCLient is " + isClient);
            isHost = intent.getBooleanExtra("isHost",false);
            toast(" isHost is " + isHost);
            internet = intent.getBooleanExtra("internet",false);

        }else{
            toast("savedInstanceState is null");
        }
        setContentView(R.layout.activity_game);
        final TextView label2 = (TextView) findViewById(R.id.s);
        final Button newGame = (Button) findViewById(R.id.newButton);
        final TextView label = (TextView) findViewById(R.id.numShot);
        final TextView player = (TextView) findViewById(R.id.playersTurn);
        board = new GameBoard(10);
        secondBoard= new Board(10);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shipsplace= extras.getBooleanArray("playerShips");//saves to 1-d array
            internet= extras.getBoolean("internet");
            isHost= extras.getBoolean("isHost");
            isClient= extras.getBoolean("isClient");
            if(internet){
                oppShipsPlace=extras.getBooleanArray("oppShips");
                twoDOppShipsPlace=singleToDoubleArray(oppShipsPlace);
            }

            for(int r=0;r<10;r++){
                for(int c=0;c<10;c++){
                    twoDShipsPlace[r][c]=false;//make sure empty first
                }
            }
            twoDShipsPlace=singleToDoubleArray(shipsplace);//convert to 2-d

            playerBoard.ships=twoDShipsPlace;//give value to playerBoardView
            for(int a=0;a<=9;a++){
                for(int b=0;b<=9;b++){
                    if(playerBoard.ships[a][b]== true) {
                        secondBoard.health++;
                        yourShipLocation[a][b]=true;
                    }
                }
            }
            //toast("number of ship places: "+Integer.toString(secondBoard.health));


        }
        if(internet==true) {
            handler = new Handler();
            if (isClient) {
                connectToServer(CHAT_SERVER, PORT_NUMBER);
                // if(socket==null)toast("cant connect");
            } else {
                createServer();
            }
        }


        //this is for comp
        boardView = (BoardView) findViewById(R.id.boardView);
        boardView.setBoard(board);


        //this is for player
        playerBoard=(PlayerBoardView) findViewById(R.id.boardView2);
        playerBoard.setBoard(secondBoard);

        ya.setColor(Color.WHITE);





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
                if(internet==true) {
                    sentX = x;//saved to vars in order to use when receives hit or miss
                    sentY = y;
                    sendToRequest(sentX,sentY);
                }
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
        //outState.putSerializable("socket",socket);
        outState.putBoolean("internet",internet);
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
        internet=savedInstanceState.getBoolean("internet");
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
//////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /** Connect to the specified chat server. */
    private void createServer(){
        new Thread(new Runnable()  {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT_NUMBER);
                    for(;;){//searches for connection
                        Socket socket1= serverSocket.accept();//accepts client
                        socket=socket1;//store it to global to use in other methods
                        if (socket1 != null) {
                            try {
                                readMessage(socket1);//receves messages from client
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }
    private void connectToServer(final String host, final int port) {
        new Thread(new Runnable()  {
            @Override
            public void run() {
                socket = createSocket(host, port);
                if (socket != null) {
                    try {
                        readMessage(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Socket createSocket(String host, int port) {
        try {
            return new Socket(host, port);
        } catch (Exception e) {
            Log.d("TAG---", e.toString());
        }
        return null;
    }


    //used to send boolean
    private void sendMessage(String msg) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.println(msg);
        out.flush();
    }
    //method to read opp msg
    private void readMessage(Socket socket) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        while(true) {
            final String msg=in.readLine();

            if(msg.equals("miss")){
                //do miss stuff
                //must remember to save what you sent do miss stuff with those coordinates
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast("miss");
                    }
                });

            }else if(msg.equals("hit")){
                //do hit stuff
                //must remember to save what you sent to do hit stuff with those coordinates
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ///can be used to update health but may need to update textview can follow format of action listner update
                        //board.health--;
                        toast("hit");
                    }
                });

            }else {//else the message recieved was coordinates
                String [] cordinates=msg.split(",");
                int xcord = Integer.parseInt(cordinates[0]); // cordinates to use for cheching your own board
                int ycord = Integer.parseInt(cordinates[1]);
                if(yourShipLocation[xcord][ycord]==true){
                    //secondBoard.health--; //can be used to update health but may need to update textview
                    sendMessage("hit");
                }
                else{
                    sendMessage("miss");
                }

                if (msg == null) {
                    break;
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast("opp touched x,y:" + msg);
                        }
                    });

                }
            }
        }
    }
    //use when sending coordinates
    public void sendToRequest(int x,int y){
        if(internet==true) {
            int x2 = x;
            int y2 = y;
            String msg=Integer.toString(x2)+","+Integer.toString(y2);
            sendMessage(msg);
            //sendMessage(Integer.toString(y2));
        }
    }
}
