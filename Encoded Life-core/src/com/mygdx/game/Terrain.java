package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class Terrain {

	
	TiledMap map;
	MapLayers layers ;
	
	public Terrain() {
		
		map = new TiledMap();
		layers = map.getLayers();
		TiledMapTileLayer layer1 = new TiledMapTileLayer(Statics.MAP_SIZE, Statics.MAP_SIZE, 40, 40);
		Cell cell = new Cell();
		
		//should use texture packer for this
		TextureRegion region = new TextureRegion(Gdx.files.internal("terrain/terrain.png"), 90, 90, 40, 40); 

		cell.setTile(new StaticTiledMapTile(region));
		layer1.setCell(x, y, cell);

		layers.addLayer(layer1);
	
	
	}

	public TiledMap getMap() {
		return map;
	}
	
	
	
	
}
