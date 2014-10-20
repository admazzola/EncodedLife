package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class World extends Stage{

	public World(Viewport viewport) {
		super(viewport);
		
		init();
	}
	
	
	List<Unit> units = new ArrayList<Unit>();
	
	
	private void init() {
		
			
		
		Terrain terrain = new Terrain();
		
		tiledMapRenderer = new OrthogonalTiledMapRenderer(terrain.getMap());
		
		
		
		Unit unit = new Unit();
		units.add(unit);
        addActor(unit.getModel());
        
      
		
	}



	public void update(float tpf) {
		
		for(Unit unit: units)
		{
		unit.update(tpf);
		}
	}
	
	
	TiledMapRenderer tiledMapRenderer;
	
	
	@Override
	public void draw()
	{
		//draw the terrain
		tiledMapRenderer.setView((OrthographicCamera) EncodedLife.camera);
        tiledMapRenderer.render();
		
		//draw the actors
		super.draw();
	}
	

}
