package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Camera;

public class EncodedLife extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	// GFX http://opengameart.org/content/basic-isometric-tiles-128x128
	//other gfx     http://opengameart.org/content/lpc-animated-water-and-waterfalls  http://opengameart.org/content/tiled-terrains
	//plants http://opengameart.org/content/lpc-plant-repack
	//  http://opengameart.org/content/lpc-terrain-repack
	//giatn atlas http://opengameart.org/content/lpc-tile-atlas
	//units http://opengameart.org/content/10-basic-rpg-enemies

	//http://www.gamefromscratch.com/post/2013/11/27/LibGDX-Tutorial-9-Scene2D-Part-1.aspx
	
	private World world;
	Viewport viewport ;
	private Camera camera;

	
	@Override
	public void create () {
		
		
		
		viewport = new ScreenViewport();
		camera = viewport.getCamera();
		
			
		viewport.setScreenPosition(0, 0);
		viewport.setScreenSize(800, 480);
		viewport.apply();

		world = new World(viewport);
		
		
		//batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
	}

	
	
	 @Override
	    public void dispose() {
		 world.dispose();
	}
	 
	@Override
	public void render () {
		
		update();
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.draw();
        		
		
		//batch.begin();
		//batch.draw(img, 0, 0);
		//batch.end();
	}
	
	
	private void update() {
		camera.translate(-3, 0, 0);		
		camera.update();
		
		
		
		world.update();
	}


	

	  @Override
	    public void resize(int width, int height) {
	        float aspectRatio = (float) width / (float) height;
	        viewport.setScreenSize(width, height);
	        
	        System.out.println("Resizing");
	    }
}
