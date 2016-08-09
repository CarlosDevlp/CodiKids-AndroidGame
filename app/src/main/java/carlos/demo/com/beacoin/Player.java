package carlos.demo.com.beacoin;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * Created by Carlos on 28/07/2016.
 */
public class Player extends Body {

    private Drawable mSprite;
    private int mStandardAmount;
    public Player(Drawable sprite,int x, int y, int width, int height){
        super(x, y, width, height);
        mSprite=sprite;
    }

    public void draw(Canvas canvas){
        mSprite.setBounds(mX,mY,mX+mWidth,mY+mHeight);
        mSprite.draw(canvas);
    }

    public void setStandardAmount(int amount){
        mStandardAmount=amount;
    }

    public void goUp(){
        mY-=mStandardAmount;
    }
    public void goDown(){
        mY+=mStandardAmount;
    }
    public void goLeft(){
        mX-=mStandardAmount;
    }
    public void goRight(){
        mX+=mStandardAmount;
    }
}
