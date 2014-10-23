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
import com.mygdx.game.utilities.SIDES;
import com.mygdx.game.utilities.Vector2f;

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
		
		TerrainTileData tiledata = getTerrainDataAtLoc(x,y,z);
		
		
		AtlasRegion parentRegion = atlas.findRegion( tiledata.terrainType.getPath() );
		
		
		
		Vector2f subtileOffset = tiledata.subtileType.getOffset().mult(32);
		
		return new AtlasRegion(parentRegion.getTexture(), parentRegion.getRegionX() + (int)subtileOffset.getX(), parentRegion.getRegionY() + (int) subtileOffset.getY(), 32, 32);
	}



	private TerrainTileData getTerrainDataAtLoc(int x, int y,int z) {
			
		
		boolean adjacentTilesSimilar[] = getAdjacentTilesSimilar(x,y,z);
				
		
		return new TerrainTileData(tiles[z][y][x], SUBTILE_TYPE.getFromAdjacentTileMap( adjacentTilesSimilar ));
	}

	private boolean[] getAdjacentTilesSimilar(int x, int y, int z) {
		
		boolean[] tilesSimilar = new boolean[8];
		
		for(int i=0;i<8;i++)
		{
			if(withinMapBounds(x,y,z)){
			tilesSimilar[i] = tiles[z][y][x] == tiles[z][y+SIDES.values()[i].getY()][x+SIDES.values()[i].getX()]; 	
			}
		}		
		
		return tilesSimilar;
	}

	private boolean withinMapBounds(int x, int y, int z) {
		
		return x > 0 && x < Statics.MAP_SIZE && y > 0 && y < Statics.MAP_SIZE;
	}

	class TerrainTileData
	{
		TERRAIN_TYPES terrainType;
		SUBTILE_TYPE subtileType;	
		
		
		public TerrainTileData(int TerrainTypeId, int subtileTypeId) 
		{
			this.terrainType =  TERRAIN_TYPES.values()[TerrainTypeId];
			this.subtileType =  SUBTILE_TYPE.values()[subtileTypeId];
		}
		
		
		public TerrainTileData(TERRAIN_TYPES terrainType, SUBTILE_TYPE subtileType) 
		{
			this.terrainType=terrainType;
			this.subtileType=subtileType;
		}
		
		public TerrainTileData(int TerrainTypeId, SUBTILE_TYPE subtileType) 
		{
			this.terrainType =  TERRAIN_TYPES.values()[TerrainTypeId];
			this.subtileType=subtileType;
		}
		
		
			
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
	
	
	enum SUBTILE_TYPE
	{
		BASE(1,3),
		BASE1(0,5),
		BASE2(1,5),
		BASE3(2,5),
		TOP(1,2),
		TOPRIGHT(2,2),
		RIGHT(2,3),
		BOTTOMRIGHT(2,4),
		BOTTOM(1,4),
		BOTTOMLEFT(0,4),
		LEFT(0,3),
		TOPLEFT(0,2),
		CORNERTOPRIGHT(2,0),
		CORNERTOPLEFT(1,0),
		CORNERBOTTOMLEFT(1,1),
		CORNERBOTTOMRIGHT(2,1),
		PATCH1(0,0),
		PATCH2(0,1)
		;
		
		int x;
		int y;
		SUBTILE_TYPE(int x, int y)
		{
			this.x=x;
			this.y=y;			
		}
		public static SUBTILE_TYPE getFromAdjacentTileMap(boolean[] same) {
			
			SUBTILE_TYPE type = SUBTILE_TYPE.PATCH1;
			
			//add logic here
			if(same[SIDES.TOP.ordinal()]&&same[SIDES.RIGHT.ordinal()]&&same[SIDES.BOTTOM.ordinal()]&&same[SIDES.LEFT.ordinal()]){
				
				type = SUBTILE_TYPE.BASE;
			
					//corners
					if(!same[SIDES.TOPRIGHT.ordinal()]){type = SUBTILE_TYPE.BOTTOMLEFT;}
					if(!same[SIDES.BOTTOMRIGHT.ordinal()]){type = SUBTILE_TYPE.TOPLEFT;}
					if(!same[SIDES.BOTTOMLEFT.ordinal()]){type = SUBTILE_TYPE.TOPRIGHT;}
					if(!same[SIDES.TOPLEFT.ordinal()]){type = SUBTILE_TYPE.BOTTOMRIGHT;}
					
			}
			
			//cardinals
			if(same[SIDES.TOP.ordinal()]&&same[SIDES.RIGHT.ordinal()]&&same[SIDES.BOTTOM.ordinal()]&&!same[SIDES.LEFT.ordinal()]){type = SUBTILE_TYPE.RIGHT;}
			if(same[SIDES.TOP.ordinal()]&&same[SIDES.BOTTOM.ordinal()]&&same[SIDES.LEFT.ordinal()]&&!same[SIDES.RIGHT.ordinal()]){type = SUBTILE_TYPE.LEFT;}
			if(same[SIDES.RIGHT.ordinal()]&&same[SIDES.BOTTOM.ordinal()]&&same[SIDES.LEFT.ordinal()]&&!same[SIDES.TOP.ordinal()]){type = SUBTILE_TYPE.BOTTOM;}
			if(same[SIDES.RIGHT.ordinal()]&&same[SIDES.TOP.ordinal()]&&same[SIDES.LEFT.ordinal()]&&!same[SIDES.BOTTOM.ordinal()]){type = SUBTILE_TYPE.TOP;}
			

			//diags
			if(same[2]&&same[4]&&!same[0]&&!same[6]&&same[3]){type = SUBTILE_TYPE.CORNERBOTTOMLEFT;}
			if(same[4]&&same[6]&&!same[2]&&!same[0]&&same[5]){type = SUBTILE_TYPE.CORNERBOTTOMLEFT;}
			if(same[0]&&same[2]&&!same[6]&&!same[4]&&same[1]){type = SUBTILE_TYPE.CORNERBOTTOMLEFT;}
			if(same[0]&&same[6]&&!same[2]&&!same[4]&&same[7]){type = SUBTILE_TYPE.CORNERBOTTOMLEFT;}
			
					
			
			
			
			return type;
		}
		public Vector2f getOffset() {			
			return new Vector2f(x,y);
		}
		
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
