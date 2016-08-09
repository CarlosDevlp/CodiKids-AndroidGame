package carlos.demo.com.beacoin;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.bq.robotic.droid2ino.activities.BaseBluetoothConnectionActivity;
import com.bq.robotic.droid2ino.utils.Callback;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseBluetoothConnectionActivity {
    public static float W,H;

    private CanvasView mCanvas;

    //bluetooth
    private final String CODIKIDS_DEVICE="20:15:05:29:53:32";
    private StringBuffer mOutStringBuffer;// String buffer for outgoing messages

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //no title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");

        //creando el canvas e incluyendolo en el layout
        mCanvas=new CanvasView(this);
        setContentView(mCanvas);

        //fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    //canvas
    public class CanvasView extends View{
        private Paint pincel;

        private boolean executed=false;
        private Canvas canvas;
        private Resources res;
        private int mStandardWidth,mStandardHeight;

        private String mScript;
        private Drawable mBackground;
        private Bitmap mBlockSprite;
        private Button mResetButton;
        private Button mBluetoothButton;

        //private Body mReset;
        private Player mPlayer;
        private ArrayList<Body> mBlockList;
        private Enemy mEnemy;
        //constructor
        public CanvasView(Context context){
            super(context);
            res = context.getResources();

            pincel = new Paint();
            pincel.setColor(Color.BLACK);
            pincel.setStrokeWidth(3);
            pincel.setStyle(Paint.Style.STROKE);

            mBlockSprite = BitmapFactory.decodeResource(getResources(), R.drawable.maderacubo);


        }

        //solo se ejecutará una vez
        private void once(){
            W=canvas.getWidth();
            H=canvas.getHeight();


            //obtener tamaños estándares
            mStandardHeight=getHeight()/7;
            mStandardWidth=mStandardHeight;

            //sprites y tiles
            mBackground = res.getDrawable(R.drawable.pasto);
            mBackground.setBounds(0,0,(int)W,(int)H);
            mBlockList = new ArrayList<>();

            for(int row=0;row<7;row++)
                for(int col=0;col<12;col++) {
                    if((col==5 && row>3) || (col==6 && row==4))
                        continue;
                    mBlockList.add(new Body(col * mStandardWidth, row * mStandardHeight, mStandardWidth, mStandardHeight));
                }

            //enemigo
            mEnemy= new Enemy(res.getDrawable(R.drawable.pig),mStandardWidth*6,mStandardHeight*4, mStandardWidth, mStandardHeight);
            //jugador
            mPlayer= new Player(res.getDrawable(R.drawable.player),mStandardWidth*5,mStandardHeight*6, mStandardWidth, mStandardHeight);
            mPlayer.setStandardAmount(mStandardWidth);


            //botón de reset
            mResetButton= new Button(res.getDrawable(R.drawable.reset),0,mStandardHeight*5, mStandardWidth*2, mStandardHeight*2);
            mResetButton.onClick(new Callback() {
                @Override
                public void execute(String[] args) {
                    mPlayer.setCoords(mStandardWidth*5,mStandardHeight*6);
                    mScript="";
                    sendMessage("CodiKids!");
                }
            });

            //botón de bluetooth
            mBluetoothButton= new Button(res.getDrawable(R.drawable.bluetooth),0,0, mStandardWidth*2, mStandardHeight*2);
            mBluetoothButton.onClick(new Callback() {
                @Override
                public void execute(String[] args) {
                    requestDeviceConnection();
                }
            });


            mBluetoothConnection.setCallback(new Callback() {
                @Override
                public void execute(String[] args) {
                    String command=args[0];
                    Log.d("app-command",args[0]);

                    if(command.equals("E"))
                        runScript();
                    else
                        mScript=command+mScript;


                }
            });



            //nueva lista de script
            mScript="";

            executed=true;

            invalidate();


        }


        //lienzo donde dibujar el canvas
        @Override
        protected void onDraw(Canvas canvas) {
            this.canvas=canvas;
            if(!executed) {
                once();
            }else{

            //drawing
                //background
                mBackground.draw(canvas);


                //drawing objects
                for(Body body : mBlockList)
                    canvas.drawBitmap(mBlockSprite, body.getX(), body.getY(),null);

                //enemigo
                mEnemy.draw(canvas);
                //jugador
                mPlayer.draw(canvas);
                //botón
                mResetButton.draw(canvas);
                mBluetoothButton.draw(canvas);
            }

        }

        public Player getPlayer(){return mPlayer;}
        public void redraw(){
            invalidate();
        }

        ArrayList<TimerTask> mTaskList;
        Timer mTimer= new Timer();
        final int mTime=1000;
        public void runScript(){

            mTaskList=new ArrayList<>();


            for(int pos=0;pos<mScript.length();pos++) {
                switch (mScript.charAt(pos)) {
                    case 'U'://arriba
                        mTaskList.add(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                mPlayer.goUp();
                                                mCanvas.redraw();

                                                if(mTaskList.size()>0){
                                                    mTimer.schedule(mTaskList.get(mTaskList.size()-1),mTime);
                                                    mTaskList.remove(mTaskList.size()-1);
                                                }
                                            }
                                        });
                                    }
                                }
                        );

                        break;
                    case 'D'://abajo
                        mTaskList.add(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                mPlayer.goDown();
                                                mCanvas.redraw();


                                                if(mTaskList.size()>0){
                                                    mTimer.schedule(mTaskList.get(mTaskList.size()-1),mTime);
                                                    mTaskList.remove(mTaskList.size()-1);
                                                }
                                            }
                                        });
                                    }
                                }
                        );
                        break;
                    case 'R'://derecha
                        mTaskList.add(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mPlayer.goRight();
                                                mCanvas.redraw();

                                                if(mTaskList.size()>0){
                                                    mTimer.schedule(mTaskList.get(mTaskList.size()-1),mTime);
                                                    mTaskList.remove(mTaskList.size()-1);
                                                }
                                            }
                                        });
                                    }
                                }
                        );

                        break;
                    case 'L'://izquierda
                        mTaskList.add(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mPlayer.goLeft();
                                                mCanvas.redraw();

                                                if(mTaskList.size()>0){
                                                    mTimer.schedule(mTaskList.get(mTaskList.size()-1),mTime);
                                                    mTaskList.remove(mTaskList.size()-1);
                                                }
                                            }
                                        });
                                    }
                                }
                        );

                        break;
                }


            }

            mTimer.schedule(mTaskList.get(mTaskList.size()-1),mTime);
            mTaskList.remove(mTaskList.size()-1);

            mScript="";

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x=event.getX();
            float y=event.getY();
            if(x<0)
                x=0;
            if(x> canvas.getWidth())
                x= canvas.getWidth();
            if(y <0)
                y=0;
            if(y > canvas.getHeight())
                y= canvas.getHeight();

            mResetButton.checkCollision((int)x,(int)y);
            mBluetoothButton.checkCollision((int)x,(int)y);
            redraw();
            return true;
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //bluetooth
    @Override
    public void onNewMessage(String message) {
            Log.d("app-answer",message);
    }

}

