package personal.jason.androidlib.game.engine2d.sprite;

import personal.jason.androidlib.game.engine2d.Position;
import android.content.res.Resources;

/**
 * 支持Fade的精灵类
 * 
 * @author vb.wbw
 * @create 2012.6
 */

public class FadeSprite extends Sprite {

	public FadeSprite(Position position, Resources res, int id)
	{
		super(position,res,id);
	}

	public FadeSprite(Position position, Resources res, int id, boolean isCanBeTouched) {
		super(position,res,id,isCanBeTouched);
	}
	
	private boolean fadeIn=false;
	private boolean fadeOut=false;
	final int alphaInBegin=255;
	final int alphaInEnd=0;
	
	protected int fadeStep=1;     //step for alpha to fade
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(fadeIn){
			if((this.getAlpah()-fadeStep)<=this.alphaInEnd){
				this.setAlpha(alphaInEnd);
				fadeInEnd();
			}else{
				this.setAlpha(this.getAlpah()-fadeStep);
			}
		}
		
		if(fadeOut){
			if((this.getAlpah()+fadeStep)>=this.alphaInBegin){
				this.setAlpha(alphaInBegin);
				fadeOutEnd();
			}else{
				this.setAlpha(this.getAlpah()+fadeStep);
			}
		}
	}
	
	/**
	 * to fadeIn
	 */
	public void fadeIn(){
		fadeOut=false;
		fadeIn=true;
		this.setAlpha(alphaInBegin);
	}
	
	/**
	 * to fadeOut
	 */
	public void fadeOut(){
		fadeIn=false;
	}
	
	/**
	 * call after fadeIn over
	 */
	public void fadeInEnd(){
		fadeIn=false;
		fadeOut=true;
		this.setAlpha(alphaInEnd);
	}
	
	/**
	 * call after fadeOut over
	 */
	public void fadeOutEnd(){
		fadeOut=false;
	}

	public int getFadeStep() {
		return fadeStep;
	}

	public void setFadeStep(int fadeStep) {
		this.fadeStep = fadeStep;
	}
}
