package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
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
		
		
		
		//should use texture packer for this to look up coords
		Texture terrainmap = new Texture(Gdx.files.internal("terrain/terrain.png"));
		TextureRegion region = new TextureRegion(terrainmap, 90, 90, 40, 40); 

		cell.setTile(new StaticTiledMapTile(region));
		
		for(int x=0;x<99;x++){
			for(int y=0;y<99;y++){
					layer1.setCell(x, y, cell);
			}
		}
		
		
		layers.add(layer1);
	
	}

	public TiledMap getMap() {
		return map;
	}
	
	
	
	
}
