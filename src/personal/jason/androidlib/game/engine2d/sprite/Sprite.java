package personal.jason.androidlib.game.engine2d.sprite;

import personal.jason.androidlib.game.engine2d.Position;
import personal.jason.androidlib.game.engine2d.ScaleFactory;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Sprite , on Screen
 * 
 * @author vb.wbw
 * @create 2012.6
 */

public class Sprite
{
	protected PositionSprite position;
	
	protected Bitmap bitmap;
	public boolean isCanBeTouched = false;
	
	protected Resources res;
	
	protected boolean isVisible = true;

	protected Paint paint = new Paint();
	protected Matrix matrix = new Matrix();

	public Sprite(Position position, Resources res, int id)
	{
		bitmap = ScaleFactory.scaleBitmap(res, id);
		this.position=new PositionSprite(position, bitmap.getWidth(), bitmap.getHeight());
		onCreate();
	}

	public Sprite(Position position, Resources res, int id, boolean isCanBeTouched) {
		this(position, res, id);
		this.isCanBeTouched = isCanBeTouched;
	}

	public void onCreate() {

	}

	/**
	 * draw the sprite. default is draw bitmap
	 * @param c
	 */
	public void present(Canvas c) {
		if (isVisible)
		{
			c.drawBitmap(bitmap, matrix, paint);
		}
	}
	
	/**
	 * update the matrix and paint ,or others
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		matrix.reset();
		matrix.postTranslate(position.getX(), position.getY());
	}

	public void recycle() {
		if (bitmap != null)
			bitmap.recycle();
	}

	public boolean contains(float x, float y) {
		if (position.isThearea(x, y)) {
			return true;
		} else {
			return false;
		}
	}

	public int getAlpah() {
		return paint.getAlpha();
	}

	public void setAlpha(int alpha) {
		paint.setAlpha(alpha);
	}

	public boolean isVisible() {
		return isVisible;
	}
	
	public void Act()
	{
		
	}
	
	public void NextAction()
	{
		
	}
	
	public void End()
	{
		
	}
	
	public void reset(boolean isVisible)
	{
		
	}
	
	//------------------------------------------------------------
	// touched
	//------------------------------------------------------------
	
    public void onAreaTouched(MotionEvent event) {}
	
	public void onAreaScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY){}
	
	public void onAreaDoubleTouched(MotionEvent event){}
	
	public void onAreaFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY){}
}