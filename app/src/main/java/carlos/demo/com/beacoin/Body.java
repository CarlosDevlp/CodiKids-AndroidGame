package carlos.demo.com.beacoin;


/**
 * Created by Carlos on 28/07/2016.
 */
public class Body {

    //private Drawable mSprite;
    protected int mX,mY;
    protected int mWidth,mHeight;
    public Body(int x, int y, int width, int height){
        mX=x;
        mY=y;
        mWidth=width;
        mHeight=height;
    }

    //getters

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    //otros métodos
    public void setCoords(int x,int y){
        mX=x;
        mY=y;
    }



}
