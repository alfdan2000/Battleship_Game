package edu.utep.cs.cs4330.battleship;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
//Thomas Payan and Alfred Cazares
public class PlayerBoardView extends View{



    private final Paint shipsPlacement = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        shipsPlacement.setColor(Color.GREEN);
    }
    private final Paint hitShip = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        hitShip.setColor(Color.RED);
    }
    private final Paint shotsTaken = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        shotsTaken.setColor(Color.YELLOW);
    }

    /** Callback interface to listen for board touches. */
    public static interface BoardTouchListener {
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
    private final List<BoardView.BoardTouchListener> listeners = new ArrayList<>();

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
    private Board board;

    public static boolean [][]ships=new boolean[10][10];

    public int health=0;



    /** Size of the board. */
    private int boardSize;

    /** Create a new board view to be run in the given context. */
    public PlayerBoardView(Context context) {
        super(context);
    }

    /** Create a new board view with the given attribute set. */
    public PlayerBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Create a new board view with the given attribute set and style. */
    public PlayerBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** Set the board to to be displayed by this view. */
    public void setBoard(Board board) {
        this.board = board;
        this.boardSize = board.size();
        for(int i=0;i<=9;i++){//initialize the hit board
            for(int j=0;j<=9;j++)
                ships[i][j]=false;
        }
        board.initialize();
        board.health=0;
        //now to count the health of the ships


    }

    /**
     * Overridden here to detect a board touch. When the board is
     * touched, the corresponding place is identified,
     * and registered listeners are notified.
     *
     * @see BoardView.BoardTouchListener
     */
    @Override


    public boolean onTouchEvent(MotionEvent event) {
        int xy2=locatePlace(event.getX(), event.getY());
        x=(xy2 / 100)*65;//update variables to use in other method
        y=(xy2 % 100)*65;

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

        return true;
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
    public boolean isTouch=false;
    public static int previ=-1;
    public static int prevj=-1;
    public int count=0;
    public int prevCount=-1;
    public static GameBoard gameBoard;


    public void drawPlaces(Canvas canvas) {
        final float placeSize = lineGap(); /**This has the size of the gaps between the lines.*/
        final float maxCoord = maxCoord(); /**This returns the size of the entire board.*/
        for (int col=0;col<10;col++) {
            for (int row = 0; row < 10; row++) {
                float xy = col * placeSize;
                float xz = row * placeSize;
                if (ships[row][col] == true) {
                    canvas.drawRect(xz, xy, xz + placeSize, xy + placeSize, shipsPlacement);
                }
                if (gameBoard.randomBoard[row][col] == true) {
                    canvas.drawRect(xz, xy, xz + placeSize, xy + placeSize, shotsTaken);
                }
                if (gameBoard.randomBoard[row][col] & ships[row][col] == true) {
                    canvas.drawRect(xz, xy, xz + placeSize, xy + placeSize, hitShip);
                    count++;
                }



                /*if(ships[i][j]==true) {
                    canvas.drawRect(i*20, j*20, (i*20) + 20, (j*20) + 20, shipsPlacement);//65 so
                }
                if(gameBoard.randomBoard[i][j] == true){
                    canvas.drawRect(i*20, j*20, (i*20) + 20, (j*20) + 20, shotsTaken);
                }
                if(gameBoard.randomBoard[i][j] & ships[i][j] == true){
                    canvas.drawRect(i*20, j*20, (i*20) + 20, (j*20) + 20, hitShip);
                    count++;

                    //gameBoard.hasHitBoard[i][j] = true;
                }*/



            }
        }
        if(prevCount!=count) {
            game.secondBoard.health = 17 - count;
            prevCount=count;
        }
        count=0;
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
    public void addBoardTouchListener(BoardView.BoardTouchListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /** Unregister the given listener. */
    public void removeBoardTouchListener(BoardView.BoardTouchListener listener) {
        listeners.remove(listener);
    }

    /** Notify all registered listeners. */
    private void notifyBoardTouch(int x, int y) {
        for (BoardView.BoardTouchListener listener: listeners) {
            listener.onTouch(x, y);
        }
    }


}
