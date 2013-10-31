package personal.jason.androidlib.game.engine2d;

/**
 * Guarantee the length use is right. (picture is control by system . 3 drawable folder)
 * use to scale bitmap and length
 * 保证所有分辨率的适应
 * 
 * @author Jason_wbw
 * @create 2012.6
 */

import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class ScaleFactory {

	public static float x_Scaling = 1;         //with 480*800
	public static float y_Scaling = 1;
	
	public final static int DEFAULT_WIDTH = 480;
	public final static int DEFAULT_HEIGHT = 800;
	
	public final static int DEFAULT_GAME_HEIGHT = 623;		
	
	public static int SCREEN_WIDTH=480;
	public static int SCREEN_HEIGHT=800;
	
	private static boolean isToScale()
	{
		if(x_Scaling != 1 || y_Scaling != 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * get the scale length for x
	 * @param length
	 * @return
	 */
	public static float getScaleLengthX(float length){
		if(isToScale()){
			return length*x_Scaling;
		}
		return length;
	}
	
	/**
	 * get the scale length for y 
	 * @param length
	 * @return
	 */
	public static float getScaleLengthY(float length){
		if(isToScale()){
			return length*y_Scaling;
		}
		return length;
	}
	
	/**
	 * scale bitmap to suit the different screen
	 * @param bitmap
	 * @return
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap)
	{
		if (isToScale())
		{
			Bitmap temp= scaleBitmap(bitmap, x_Scaling, y_Scaling);
			bitmap.recycle();
			return temp;
		}
		
		return bitmap;
	}
	
	public static Bitmap scaleBitmap(Resources res, int id){
		return scaleBitmap(getBitmap(res,id));
	}
	
	public static Bitmap scaleBitmap(Bitmap bitmap, float xScaling){
		if(xScaling==1.0f){
			return bitmap;
		}
		
		Bitmap temp=scaleBitmap(bitmap, xScaling, 1.0f);
		bitmap.recycle();
		return temp;
	}
	
	public static Bitmap scaleBitmap(Bitmap bitmap, float xScaling, float yScaling){
		Bitmap newBitmap = null;

		int bitmapWidth = (int) (bitmap.getWidth() * xScaling);
		int bitmapHeight = (int) (bitmap.getHeight() * yScaling);
		newBitmap = Bitmap.createScaledBitmap(bitmap,
				(int) (bitmapWidth),
				(int) (bitmapHeight), true);
		
		return newBitmap;
	}
	
	// get bitmap in the most optimal way
	private static Bitmap getBitmap(Resources res, int id) {
		Options options = new Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;

		InputStream is = res.openRawResource(id);
		return BitmapFactory.decodeStream(is, null, options);
	}
}
