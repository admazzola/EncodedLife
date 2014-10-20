package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class UnitModel extends Actor{

	
	
	
	public UnitModel()
	{
		this.setPosition(33, 22);
		
	}
	
	 Texture texture = new Texture(Gdx.files.internal("terrain/terrain.png"));
     @Override
     public void draw(Batch batch, float alpha){
         batch.draw(texture,this.getX(),this.getY());
     }
	
}
