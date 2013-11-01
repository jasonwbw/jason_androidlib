package personal.jason.androidlib.game.engine2d;

import java.util.ArrayList;
import java.util.List;

import personal.jason.androidlib.game.engine2d.sprite.Sprite;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * 承载精灵的屏幕类，控制所有的精灵
 * 
 * @author Jason_wbw
 * @create 2012.6
 */

public class Screen extends SurfaceView  implements Callback{
	
	protected GameViewDrawThread drawThread;

	protected List<Sprite> sprites = new ArrayList<Sprite>();
	
	public Screen(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		getHolder().addCallback(this);
		drawThread = new GameViewDrawThread(this);
	}
	
	/**
	 * 添加一个精灵到界面中
	 * @param sprite
	 */
	public void addSprite(Sprite sprite){
		sprites.add(sprite);
	}

	/**
	 * 重绘界面并重绘所有精灵
	 * @param canvas
	 * @param deltaTime
	 */
	protected void onDraw(Canvas canvas,float deltaTime){
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);
		
		for(Sprite sprite:sprites){
			sprite.update(deltaTime);
			sprite.present(canvas);
		}
	}
	
	public boolean onTouchEvent(MotionEvent event){
		
		for(Sprite sprite:sprites){
			if(sprite.isCanBeTouched && sprite.contains(event.getX(), event.getY())){
				sprite.onAreaTouched(event);
			}
		}
		
		return true;
	}
	
	/**
	 * 退出界面时回收所有的资源
	 */
	public void recyle(){
		for(Sprite sprite:sprites){
			if(sprite!=null){
				sprite.recycle();
			}
		}
	}
	
	// --------------------------------------------------------------------------
    // surface interface
	// --------------------------------------------------------------------------
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		drawThread.setFlag(true);
		drawThread.start();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		boolean retry = true;
		drawThread.setFlag(false);
        while (retry) {
            try {
            	drawThread.join();
                retry = false;
            } 
            catch (Exception e) {
            	e.printStackTrace();
            }
        }
	}

}
