package com.mygdx.game.utilities;

public class Vector2f {
	
	float x,y;
	

	public Vector2f(float f, float g) {
		this.x=f;
		this.y=g;
		
	}

	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}


	public Vector2f mult(int scalar) {
		
		return new Vector2f(x*scalar,y*scalar);
	}
	
	
}
