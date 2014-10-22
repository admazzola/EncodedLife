package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class EncodedLife extends ApplicationAdapter  implements ApplicationListener, GestureListener {
	SpriteBatch batch;
	Texture img;
	
	// GFX http://opengameart.org/content/basic-isometric-tiles-128x128
	//other gfx     http://opengameart.org/content/lpc-animated-water-and-waterfalls  http://opengameart.org/content/tiled-terrains
	//plants http://opengameart.org/content/lpc-plant-repack
	//  http://opengameart.org/content/lpc-terrain-repack
	//giatn atlas http://opengameart.org/content/lpc-tile-atlas
	//units http://opengameart.org/content/10-basic-rpg-enemies

	//http://www.gamefromscratch.com/post/2013/11/27/LibGDX-Tutorial-9-Scene2D-Part-1.aspx
	
	static World world;
	Viewport viewport ;
	static Camera camera;

	static boolean REPACK_TEXTURES = true;
		
	@Override
	public void create () {
		
		 
		
		
		if(REPACK_TEXTURES){
		//pack textures
		Settings settings = new Settings();
        settings.maxWidth = 512;
        settings.maxHeight = 512;
        // TexturePacker.process(inputDir, outputDir, packFileName);
        TexturePacker.process(settings, "../assets/images", Terrain.TERRAIN_ATLAS_PATH, Terrain.TERRAIN_ATLAS_NAME);
		}
		
		
		//set up viewport
		viewport = new ScreenViewport();
		camera = viewport.getCamera();		
			
		viewport.setScreenPosition(0, 0);
		viewport.setScreenSize(800, 480);
		viewport.apply();
		
		
		
		//create the world
		world = new World(viewport);
		
		//listen for inputs
		GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
		
		//batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
	}

	
	
	 @Override
	    public void dispose() {
		 world.dispose();
	}
	 
	 
	 
	 
	@Override
	public void render () {
		
		update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.draw();
        		
		
		//batch.begin();
		//batch.draw(img, 0, 0);
		//batch.end();
	}
	
	
	private void update(float tpf) {
		
		
		
		
		
		world.update(tpf);
		
		camera.update();
	}


	

	  @Override
	    public void resize(int width, int height) {
	        float aspectRatio = (float) width / (float) height;
	        viewport.setScreenSize(width, height);
	        
	        System.out.println("Resizing");
	    }
	  
	  public static World getWorld() {
			return world;
		}



		public static Camera getCamera() {
			return camera;
		}



		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {
			System.out.println("Touch down at " +x +" , "+y);
			return false;
		}



		@Override
		public boolean tap(float x, float y, int count, int button) {
			System.out.println("Tap at " +x +" , "+y);
			return false;
		}



		@Override
		public boolean longPress(float x, float y) {
			// TODO Auto-generated method stub
			return false;
		}



		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			// TODO Auto-generated method stub
			return false;
		}



		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			camera.translate(-deltaX, deltaY, 0);
			return false;
		}



		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
			// TODO Auto-generated method stub
			return false;
		}



		@Override
		public boolean zoom(float initialDistance, float distance) {
			// TODO Auto-generated method stub
			return false;
		}



		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
				Vector2 pointer1, Vector2 pointer2) {
			// TODO Auto-generated method stub
			return false;
		}


}
