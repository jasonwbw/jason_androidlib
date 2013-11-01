package personal.jason.androidlib.game.engine2d.sprite;

import personal.jason.androidlib.game.engine2d.Position;

/**
 * position for sprite x,y are the bitmap(scaled)'s width and height
 * 
 * @author wbw
 * @create 2012.6
 *
 */

public class PositionSprite {
	
	private float x;
	private float y;
	
	private float xLength;
	private float yLength; 
	
	/**
	 * the xLength and yLength must be scaled
	 * @param position
	 * @param xLength
	 * @param yLength
	 */
	PositionSprite(Position position, float xLength, float yLength){
		this.xLength=xLength;
		this.yLength=yLength;
		
		x=position.getX();
		y=position.getY();
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public float getxLength() {
		return xLength;
	}

	public void setxLength(float xLength) {
		this.xLength = xLength;
	}

	public float getyLength() {
		return yLength;
	}

	public void setyLength(float yLength) {
		this.yLength = yLength;
	}

	protected boolean isThearea(float x, float y){
		return isThearea( x, y, getxLength(), getyLength());
	}
	
	protected boolean isThearea(float x,float y,float lengthx,float lengthy){	
		return (x>getX()) && (x<(getX()+lengthx)) &&
				(y>getY()) && (y<(getY()+lengthy));
	}
}
