package edu.utep.cs.cs4330.battleship;
//Thomas Payan and Alfred Cazares
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.os.Bundle;



/**
 * A special view class to display a battleship board as a2D grid.
 *
 * @see Board
 */
public class BoardView extends View {

    Board newBoard;


    int playerTurn = 1;

    private final Paint hit = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        hit.setColor(Color.RED);
    }

    /** Callback interface to listen for board touches. */
    public interface BoardTouchListener {
        /**
         * Called when a place of the board is touched.
         * The coordinate of the touched place is provided.
         *
         * @param x 0-based column index of the touched place
         * @param y 0-based row index of the touched place
         *
         */

        void onTouch(int x, int y);


    }

    /** Listeners to be notified upon board touches. */
    private final List<BoardTouchListener> listeners = new ArrayList<>();

    /** Board background color. */
    private final int boardColor = Color.rgb(102, 163, 255);

    /** Board background paint. */
    private final Paint boardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        boardPaint.setColor(boardColor);
    }

    /** Board grid line color. */
    private final int boardLineColor = Color.WHITE;

    /** Board grid line paint. */
    private final Paint boardLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        boardLinePaint.setColor(boardLineColor);
        boardLinePaint.setStrokeWidth(2);
    }

    /** Board to be displayed by this view. */
    private GameBoard board;

    public static boolean [][]hits=new boolean[10][10];

    public int health=0;



    /** Size of the board. */
    private int boardSize;

    /** Create a new board view to be run in the given context. */
    public BoardView(Context context) {
        super(context);
    }

    /** Create a new board view with the given attribute set. */
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Create a new board view with the given attribute set and style. */
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** Set the board to to be displayed by this view. */
    public void setBoard(GameBoard board) {
        this.board = board;
        this.boardSize = board.size();
        for(int i=0;i<=9;i++){//initialize the hit board
            for(int j=0;j<=9;j++)
                hits[i][j]=false;
        }
        board.initialize();
        board.health=0;
        //now to count the health of the ships
        for(int a=0;a<=9;a++){
            for(int b=0;b<=9;b++){
                if(board.board[a][b]== true)
                    board.health++;
            }
        }

    }

    /**
     * Overridden here to detect a board touch. When the board is
     * touched, the corresponding place is identified,
     * and registered listeners are notified.
     *
     * @see BoardTouchListener
     */

    MainActivity main;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        AppCompatActivity host = (AppCompatActivity) this.getContext();

        //playerText.setText("Player " + playerTurn +"'s turn");

       // if(player == null){
         //   Log.d("PLAYER NOT FOUND","COULD NOT GET HANDLE AT LINE 138");
        //}

           // if(playerTurn == 2){
               // game.secondBoard.computerRandom();
                //player.postInvalidate();

               // playerTurn = 1;
             //   return false;
           // }else {
                int xy2 = locatePlace(event.getX(), event.getY());
                x = (xy2 / 100) * 65;//update variables to use in other method
                y = (xy2 % 100) * 65;
                this.invalidate();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        int xy = locatePlace(event.getX(), event.getY());
                        if (xy >= 0) {
                            notifyBoardTouch(xy / 100, xy % 100);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
               // playerTurn = 2;

                return true;
           // }


    }

    /** Overridden here to draw a 2-D representation of the board. */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrid(canvas);
        drawPlaces(canvas);

    }

    /** Draw all the places of the board. */
    public static float x=-1;//-1
    public static float y=-1;//-1
    public static float prevx=-2;
    public static float prevy=-2;
    public boolean isTouch=false;



    private void drawPlaces(Canvas canvas) {
        //newBoard.initialize();

        hits=board.placehit();
            playerTurn = 1;

            //for loop is used to redraw the places the ships are hit everytime
            for (int i=0;i<10;i++) {
                for(int j=0;j<10;j++) {
                    if(hits[i][j]==true) {
                        canvas.drawRect(i * 132, j * 132, (i * 132) + 128, (j * 132) + 128, hit);//65 so
                    }
                }
            }
        if(prevx!=x&&prevy!=y) {

            playerTurn = 1;
            board.computerRandom();
            //board.computerShot();
            game.playerBoard.invalidate();
            prevx = x;
            prevy = y;
            for (int l=0;l<10;l++) {
                for(int m=0;m<10;m++) {

                }
                }

        }


    }


    /** Draw horizontal and vertical lines. */
    private void drawGrid(Canvas canvas) {
        final float maxCoord = maxCoord();
        final float placeSize = lineGap();
        canvas.drawRect(0, 0, maxCoord, maxCoord, boardPaint);
        for (int i = 0; i < numOfLines(); i++) {
            float xy = i * placeSize;
            canvas.drawLine(0, xy, maxCoord, xy, boardLinePaint); // horizontal line
            canvas.drawLine(xy, 0, xy, maxCoord, boardLinePaint); // vertical line
        }
    }

    /** Calculate the gap between two horizontal/vertical lines. */
    protected float lineGap() {
        return Math.min(getMeasuredWidth(), getMeasuredHeight()) / (float) boardSize;
    }

    /** Calculate the number of horizontal/vertical lines. */
    private int numOfLines() {
        return boardSize + 1;
    }

    /** Calculate the maximum screen coordinate. */
    protected float maxCoord() {
        return lineGap() * (numOfLines() - 1);
    }

    /**
     * Given screen coordinates, locate the corresponding place in the board
     * and return its coordinates; return -1 if the screen coordinates
     * don't correspond to any place in the board.
     * The returned coordinates are encoded as <code>x*100 + y</code>.
     */
    private int locatePlace(float x, float y) {
        if (x <= maxCoord() && y <= maxCoord()) {
            final float placeSize = lineGap();
            int ix = (int) (x / placeSize);
            int iy = (int) (y / placeSize);
            return ix * 100 + iy;
        }
        return -1;
    }

    /** Register the given listener. */
    public void addBoardTouchListener(BoardTouchListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }

    }

    /** Unregister the given listener. */
    public void removeBoardTouchListener(BoardTouchListener listener) {
        listeners.remove(listener);
    }

    /** Notify all registered listeners. */
    private void notifyBoardTouch(int x, int y) {
        for (BoardTouchListener listener: listeners) {
            listener.onTouch(x, y);
        }
    }
}

