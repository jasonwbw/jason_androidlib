package personal.jason.androidlib.game.engine2d;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * 界面刷新线程
 * 
 * @author Jason_wbw
 * @create 2012.6
 *
 */

public class GameViewDrawThread extends Thread {

	Screen screen;
	
    private boolean flag = true;    //flag for loop
    private float systemTime;
	
	private SurfaceHolder surfaceHolder;
	
	public GameViewDrawThread(Screen screen){
		this.screen = screen;
		surfaceHolder = screen.getHolder();
	}
	
	public void run() {
		Canvas c;
        while (this.flag) {
        	float deltaTime = (System.nanoTime() - systemTime) / 1000000000.0f;
			systemTime = System.nanoTime();
        	
        	c = null;
            try {
            	//lock the canvas
                c = this.surfaceHolder.lockCanvas(null);
                
                //handle the moment when back clicked
                if(c!=null){
                	synchronized (this.surfaceHolder) {
                		screen.onDraw(c,deltaTime);
                    }
                }
                
            } finally {
                if (c != null) {
                	this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
	}
	
	/**
	 * 设置为false，可以退出循环刷新，针对每次start只能执行一次有效修改
	 * 
	 * @param flag
	 */
	public void setFlag(boolean flag){
		this.flag = flag;
	}
}
