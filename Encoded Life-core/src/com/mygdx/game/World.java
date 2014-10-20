package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class World extends Stage{

	public World(Viewport viewport) {
		super(viewport);
		
		init();
	}
	
	
	
	private void init() {
		
		
		Terrain terrain = new Terrain();
		
		
		Unit unit = new Unit();

        addActor(unit.getModel());
        
      
		
	}



	public void update() {
	
		
	}
	

}
