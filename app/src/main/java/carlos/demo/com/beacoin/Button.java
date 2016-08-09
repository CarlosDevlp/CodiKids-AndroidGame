package carlos.demo.com.beacoin;

import android.graphics.drawable.Drawable;

import com.bq.robotic.droid2ino.utils.Callback;

/**
 * Created by Carlos on 28/07/2016.
 */
public class Button extends Player {

    private Callback mCallback;
    public Button(Drawable sprite, int x, int y, int width, int height){
        super(sprite,x, y, width,  height);
    }

    public void checkCollision(int otherX,int otherY){
        if(otherX>=mX && otherX<=mX+mWidth && otherY>=mY && otherY<=mY+mHeight )
            mCallback.execute(null);
    }

    public void onClick(Callback callback){
        mCallback=callback;
    }
}
