package com.mygdx.game.utilities;

public enum SIDES {
	TOP(0,1),
	TOPRIGHT(1,1),
	RIGHT(1,0),
	BOTTOMRIGHT(1,-1),
	BOTTOM(0,-1),
	BOTTOMLEFT(-1,-1),
	LEFT(-1,0),
	TOPLEFT(-1,1)
	;
	
	int x,y;
	SIDES(int x, int y)
	{
		this.x=x;
		this.y=y;
		
		
	}
	public int getX() {
		return x;
		
	}
	public int getY() {
		return y;
		
	}
}
