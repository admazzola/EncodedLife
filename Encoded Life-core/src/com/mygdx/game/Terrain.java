package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mygdx.game.utilities.PerlinNoiseGenerator;

public class Terrain {

	public static String TERRAIN_ATLAS_PATH = "../assets/atlases";
	public static String TERRAIN_ATLAS_NAME = "gameatlas";
	
	TiledMap map;
	MapLayers layers ;
	
	short tiles[][][] = new short[2][Statics.MAP_SIZE][Statics.MAP_SIZE];
	TiledMapTileLayer[] tilelayers = new TiledMapTileLayer[2];
	
	static int BASE_LAYER = 0;
	static int TOP_LAYER = 1;
	
	public Terrain() {
		
		
		
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal(TERRAIN_ATLAS_PATH + "/" + TERRAIN_ATLAS_NAME + ".atlas"));
		
		
		
		
		
		
		
		map = new TiledMap();
		layers = map.getLayers();
		
		
		tilelayers[BASE_LAYER] = new TiledMapTileLayer(Statics.MAP_SIZE, Statics.MAP_SIZE, 32, 32);
		tilelayers[TOP_LAYER] = new TiledMapTileLayer(Statics.MAP_SIZE, Statics.MAP_SIZE, 32, 32);
				
		//should use texture packer for this to look up coords
		//Texture terrainmap = new Texture(Gdx.files.internal("terrain/terrain.png"));
		
		generateTiles();
		
		for(int z = 0;z<tilelayers.length;z++){
		for(int x=0;x<Statics.MAP_SIZE;x++){
			for(int y=0;y<Statics.MAP_SIZE;y++){
						
					Cell cell = new Cell(); 
					
					try{
						AtlasRegion region = getTerrainTileRegion(atlas,x,y,z); 									
						cell.setTile(new StaticTiledMapTile(region));
						tilelayers[z].setCell(x, y, cell);	
					
					}catch(Exception e)
					{
						e.printStackTrace();
						
					}
					
					
			}
		}
		
		
		layers.add(tilelayers[z]);
		}
		
		
	
	}

	
	
	
	
	private AtlasRegion getTerrainTileRegion(TextureAtlas atlas, int x, int y, int z) throws Exception{
		
		int terrainTypeId = getTerrainTypeAtLoc(x,y,z);
		
		AtlasRegion parentRegion = atlas.findRegion(TERRAIN_TYPES.values()[ terrainTypeId ].getPath() );
		
		int x_offset = 0;
		int y_offset = 0;		
		
		return new AtlasRegion(parentRegion.getTexture(), parentRegion.getRegionX() + x_offset, parentRegion.getRegionY() + y_offset, 32, 32);
	}



	private short getTerrainTypeAtLoc(int x, int y,int z) {
		return tiles[z][y][x];
	}





	private void generateTiles() {
		
		PerlinNoiseGenerator noise = new PerlinNoiseGenerator();
		noise.generatePerlinNoise(Statics.MAP_SIZE, Statics.MAP_SIZE, 3,15);
		
		
		for(int x=0;x<Statics.MAP_SIZE;x++){
			for(int y=0;y<Statics.MAP_SIZE;y++){
				
				
				
				tiles[0][y][x] = TERRAIN_TYPES.GRASS2.getID(); //the type of terrain (grass etc)
				
				tiles[1][y][x] = (short) (Math.abs(noise.getValue(x, y)*5f));
				
				
			}
		}
		
		
	}

	public TiledMap getMap() {
		return map;
	}
	
	
	enum TERRAIN_TYPES
	{
		GRASS1("grass1"),
		GRASS2("grass2"),
		DARKGRASS("darkgrass"),
		DIRT1("dirt1"),
		DIRT2("dirt2"),
		DIRT3("dirt3"),
		POND("pond"),
		ISLAND("island"),
		SHORTGRASS("shortgrass"),
		TALLGRASS("tallgrass"),
		SAND("sand")
		
		;
		
		String path = "NA";
		TERRAIN_TYPES(String path)
		{
			this.path=path;
		}
		
		public String getPath() {
			// TODO Auto-generated method stub
			return path;
		}

		short getID()
		{
			return (short)this.ordinal();
		}
		
		
	}
	
}
