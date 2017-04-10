package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//Thomas Payan and Alfred Cazares
/*
This is a variation of BoardView but this is only used for placing ships
 */

public class place extends View {


    public MediaPlayer mediaPlayer = new MediaPlayer();




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
    private final List<place.BoardTouchListener> listeners = new ArrayList<>();

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

    public boolean [][]hits=new boolean[10][10];
    public boolean [][]shipPlacement=new boolean[10][10];

    public boolean firstTouch=false;
    public int health=0;

    public int checkers=10;



    /** Size of the board. */
    private int boardSize;

    /** Create a new board view to be run in the given context. */
    public place(Context context) {
        super(context);
    }

    /** Create a new board view with the given attribute set. */
    public place(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Create a new board view with the given attribute set and style. */
    public place(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** Set the board to to be displayed by this view. */
    public void setBoard(Board board) {
        this.board = board;
        this.boardSize = board.size();
        for(int i=0;i<=9;i++){//initialize the hit board
            for(int j=0;j<=9;j++) {
                shipPlacement[i][j]=false;
                hits[i][j] = false;
            }
        }
        //board.initialize();
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
     * @see place.BoardTouchListener
     */
    @Override


    public boolean onTouchEvent(MotionEvent event) {
        firstTouch=true;
        int xy2=locatePlace(event.getX(), event.getY());
        x=(xy2 / 100)*65;//update variables to use in other method
        y=(xy2 % 100)*65;//change to 65

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

    private void drawPlaces(Canvas canvas) {
        int newx = ((int) Math.round(place.x)) / 65;// this is used to convert the float into a usable index
        int newy = ((int) Math.round(place.y)) / 65;
        if(checkers>0) {
            if (newx >= 0 && newy >= 0 && newx < 10 && newy < 10) {//checks if bound
                if (shipPlacement[newx][newy] == false)
                    checkers--;//subtract from checkers and place after
                shipPlacement[newx][newy] = true;
            }
        }
        if(firstTouch==false) {
            shipPlacement[0][0] = false;
        }

        //this just prints all placed every touch
        final float placeSize = lineGap(); /**This has the size of the gaps between the lines.*/
        final float maxCoord = maxCoord(); /**This returns the size of the entire board.*/
        for (int col=0;col<10;col++) {
            for (int row = 0; row < 10; row++) {
                float xy = col * placeSize;
                float xz = row * placeSize;
                if (shipPlacement[row][col] == true) {
                    canvas.drawRect(xz, xy, xz + placeSize, xy + placeSize, hit);
                }
            }
        }
           /* for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (shipPlacement[i][j] == true) {
                        canvas.drawRect(i * 75, j * 75, (i * 75) + 75, (j * 75) + 75, hit);
                    }
                }
            }*/

              //  }
            //}
        //}
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
    public void addBoardTouchListener(place.BoardTouchListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /** Unregister the given listener. */
    public void removeBoardTouchListener(place.BoardTouchListener listener) {
        listeners.remove(listener);
    }

    /** Notify all registered listeners. */
    private void notifyBoardTouch(int x, int y) {
        for (place.BoardTouchListener listener: listeners) {
            listener.onTouch(x, y);
        }
    }
}
