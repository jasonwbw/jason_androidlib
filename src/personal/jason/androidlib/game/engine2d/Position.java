package personal.jason.androidlib.game.engine2d;

/**
 * position on page
 * the x y return have to be scaled
 * 
 * @author Jason_wbw
 * @create 2012.6
 */

public class Position {

	private float x;
	private float y;
	
	public Position(float x, float y){
		this.x=x;
		this.y=y;
	}
	
	public float getX() {
		return ScaleFactory.getScaleLengthX(x);
	}

	public float getY() {
		return ScaleFactory.getScaleLengthY(y);
	}
}
