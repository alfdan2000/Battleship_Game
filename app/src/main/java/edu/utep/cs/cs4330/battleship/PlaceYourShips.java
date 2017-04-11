package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PlaceYourShips extends AppCompatActivity {
    private static String LOCAL_HOST ="172.19.156.174";//put your ip address
    //private static String LOCAL_HOST = "opuntia.cs.utep.edu";
    private static final String CHAT_SERVER = LOCAL_HOST;
    private static final int PORT_NUMBER = 8000;
    private Socket socket;
    private ServerSocket serverSocket;
    private Handler handler;
    boolean isHost = false;
    boolean isClient = false;
    public static boolean internet=false;
    boolean [][] oppShips=new boolean[10][10];






    private Board board;
    private place boardView;// uses object place(similar and derived from BoardView)
    private PlayerBoardView player;
    private Spinner menu;//drop down menu
    private connect conn;

    //booleans to see what menu item is selected
    public boolean frigateSelected=false;
    public boolean subSelected=false;
    public boolean sweeperSelected=false;
    public boolean battleSelected=false;
    public boolean carrierSelected=false;


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

        setContentView(R.layout.activity_place_your_ships);
        final Button placeShips = (Button) findViewById(R.id.place);//button to complete the placement of ships
        final Button clear=(Button) findViewById(R.id.clear);//clear board button
        board = new Board(10);
        //conn=new conn();
        boardView = (place) findViewById(R.id.boardView);//its labeled boardView in the layout id but is object place
        player = (PlayerBoardView) findViewById(R.id.boardView2);
        boardView.setBoard(board);//agian boardView variable is object place
        /*
       // Boolean isHost=false;
        Bundle extras = getIntent().getExtras();

        //////////////////possible if to see if pvp///////////////////////////////
        if (extras != null) {
            isHost= extras.getBoolean("isHost");//saves to 1-d array
        }
        handler = new Handler();

        if(!isHost) {
            connectToServer(CHAT_SERVER, PORT_NUMBER);
        } else{
            createServer();
        }
        ///////////////possible end if///////////////////
        */



        frigateSelected=false;
        subSelected=false;
        sweeperSelected=false;
        battleSelected=false;
        carrierSelected=false;
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                boardView.shipPlacement[i][j]=false;
            }
        }
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.buzzer);
        boardView.invalidate();

        menu = (Spinner)findViewById(R.id.spinner);//defines menu
        String[] items = new String[]{"","Frigate - 3 spaces", "Submarine - 3 spaces", "Minesweeper - 2 spaces","BattleShip - 4 spaces","Carrier - 5 spaces"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menu.setAdapter(adapter);
        menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int selected, long l) {//switch that deals with selecting ship
                //when giving boardView.checkers a value that means how long the ship is and how much you can place
                if(selected==0){
                    toast("use menu to select a ship");
                    boardView.checkers=0;
                }

                if (selected == 1) {
                    toast("Frigate Selected");
                    if (frigateSelected == false) {
                        boardView.checkers = 3;
                    }
                    frigateSelected = true;
                }
                if (selected == 2){
                    toast("Submarine Selected");
                    if (subSelected == false) {
                        boardView.checkers = 3;
                    }
                    subSelected = true;
                }
                if(selected == 3) {
                    toast("MineSweeper Selected");
                    if (sweeperSelected == false) {
                        boardView.checkers = 2;
                    }
                    sweeperSelected = true;
                }
                if(selected == 4) {
                    toast("Battleship Selected");
                    if (battleSelected == false) {
                        boardView.checkers = 4;
                    }
                    battleSelected = true;
                }
                if(selected == 5) {
                    toast("Carrier Selected");
                    if (carrierSelected == false) {
                        boardView.checkers = 5;
                    }
                    carrierSelected = true;
                }


            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        placeShips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //only proceeds if all ships placed
                if(areAllShipsPlaced()) {
                    //saves the places of ships into bundle
                    Intent i = new Intent("edu.utep.cs.cs4330.PLAY");
                    Bundle extras = new Bundle();
                    boolean[] newShipPlacement = new boolean[100];
                    int counting = 0;
                    for (int x = 0; x < 10; x++) {
                        for (int y = 0; y < 10; y++) {
                            newShipPlacement[counting] = boardView.shipPlacement[x][y];
                            counting++;
                        }
                    }
                    boolean [] newOppShips=doubleToSingleArray(oppShips);
                    extras.putBooleanArray("oppShips",newOppShips);
                    extras.putBooleanArray("playerShips", newShipPlacement);
                    extras.putBoolean("internet", internet);
                    extras.putBoolean("isHost", isHost);
                    extras.putBoolean("isClient", isClient);

                    i.putExtras(extras);
                    startActivity(i);
                }else{
                    toast("You havent completed placing");
                }

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {//resets board
            @Override
            public void onClick(View v) {
// clears board
                frigateSelected=false;
                subSelected=false;
                sweeperSelected=false;
                battleSelected=false;
                carrierSelected=false;
                for(int i=0;i<10;i++){
                    for(int j=0;j<10;j++){
                        boardView.shipPlacement[i][j]=false;
                    }
                }
                boardView.invalidate();
            }
        });

        boardView.addBoardTouchListener(new place.BoardTouchListener() {
            @Override
            public void onTouch(int x, int y) {
                if(internet==true) {
                    int x2 = x;
                    int y2 = y;
                    //sendMessage(Integer.toString(x2));
                    //sendMessage(Integer.toString(y2));
                }
            }
        });

    }
    //////////////////////////////////////////////////////////






    // the following two methods save and restore on screen orientation
    @Override
    protected void onSaveInstanceState(Bundle outState){
        boolean [][] saveShipPlacement=boardView.shipPlacement;
        boolean [] convertShipPlacement=doubleToSingleArray(saveShipPlacement);
        outState.putBooleanArray("playerPlaces",convertShipPlacement);
        outState.putBoolean("isfrig",frigateSelected);
        outState.putBoolean("issub",subSelected);
        outState.putBoolean("issweep",sweeperSelected);
        outState.putBoolean("isbat",battleSelected);
        outState.putBoolean("iscarr",carrierSelected);
        outState.putInt("checkers",boardView.checkers);
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boolean [] restoreShipPlacement=savedInstanceState.getBooleanArray("playerPlaces");
        boardView.shipPlacement=singleToDoubleArray(restoreShipPlacement);
        frigateSelected=savedInstanceState.getBoolean("isfrig");
        subSelected=savedInstanceState.getBoolean("issub");
        sweeperSelected=savedInstanceState.getBoolean("issweep");
        battleSelected=savedInstanceState.getBoolean("isbat");
        carrierSelected=savedInstanceState.getBoolean("iscarr");
        boardView.checkers=savedInstanceState.getInt("checkers");
        isHost = savedInstanceState.getBoolean("isHost");
        isClient = savedInstanceState.getBoolean("isClient");
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
    public boolean areAllShipsPlaced(){
        int completed=0;
        for(int r=0;r<10;r++){
            for (int c=0;c<10;c++) {
                if(boardView.shipPlacement[r][c]==true){
                    completed++;
                }

            }
        }
        if (completed==17){
            return true;
        }
        else{
            return false;
        }
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
///////////////////////////////////////////////////////////////////////////////////////////////////



}
