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
	
	

	public static String TERRAIN_ATLAS_PATH = "com/mygdx/game/assets/atlases";
	public static String TERRAIN_ATLAS_NAME = "gameatlas";
	
	TiledMap map;
	MapLayers layers ;
	
	
	short biomemap[][] = new short[Statics.MAP_SIZE][Statics.MAP_SIZE];
	short tiles[][][] = new short[3][Statics.MAP_SIZE][Statics.MAP_SIZE];
	TiledMapTileLayer[] tilelayers = new TiledMapTileLayer[3];
	
	static int BASE_LAYER = 0;
	static int TOP_LAYER = 1;
	static int AUX_CORNER_LAYER= 2;
	
	int TILE_SIZE = 32;
	
	public Terrain() {
		
		
		
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal(TERRAIN_ATLAS_PATH + "/" + TERRAIN_ATLAS_NAME + ".atlas"));
		
		
		
		
		
		
		
		map = new TiledMap();
		layers = map.getLayers();
		
		
		tilelayers[BASE_LAYER] = new TiledMapTileLayer(Statics.MAP_SIZE, Statics.MAP_SIZE, TILE_SIZE, TILE_SIZE);
		tilelayers[TOP_LAYER] = new TiledMapTileLayer(Statics.MAP_SIZE, Statics.MAP_SIZE, TILE_SIZE, TILE_SIZE);
		tilelayers[AUX_CORNER_LAYER] = new TiledMapTileLayer(Statics.MAP_SIZE, Statics.MAP_SIZE, TILE_SIZE, TILE_SIZE); //fixes glitches
				
		//should use texture packer for this to look up coords
		//Texture terrainmap = new Texture(Gdx.files.internal("terrain/terrain.png"));
		
		generateTiles();
		
		AtlasRegion region;
		
		for(int z = BASE_LAYER;z<=AUX_CORNER_LAYER;z++){
		for(int x=0;x<Statics.MAP_SIZE;x++){
			for(int y=0;y<Statics.MAP_SIZE;y++){
						
					Cell cell = new Cell(); 
					region = null;
					
					
						 region = getTerrainTileRegion(atlas,x,y,z); 	
						
						if(region!=null)
						{
						cell.setTile(new StaticTiledMapTile(region));
						tilelayers[z].setCell(x, y, cell);	
						}
						
			}
		}
		
		
		layers.add(tilelayers[z]);
		}
		
		
	
	}

	
	
	
	
	private AtlasRegion getTerrainTileRegion(TextureAtlas atlas, int x, int y, int z) {
		
		TerrainTileData tiledata = getTerrainDataAtLoc(x,y,z);
		
		
		AtlasRegion parentRegion = atlas.findRegion( tiledata.terrainType.getPath() );
		
		if(tiledata.subtileType == SUBTILE_TYPE.NONE || tiledata.terrainType == TERRAIN_TYPES.NONE)
		{					
			return null;
		}
		
		Vector2f subtileOffset = tiledata.subtileType.getOffset().mult(32);
		
		return new AtlasRegion(parentRegion.getTexture(), parentRegion.getRegionX() + (int)subtileOffset.getX(), parentRegion.getRegionY() + (int) subtileOffset.getY(), 32, 32);
	}



	private TerrainTileData getTerrainDataAtLoc(int x, int y,int z) {
			
		
		boolean[] adjacentTilesSimilar = getAdjacentTilesSimilar(x,y,z);
		
		SUBTILE_TYPE type  = SUBTILE_TYPE.getFromAdjacentTileMap(x,y, adjacentTilesSimilar );
		
		
				
		if(z == AUX_CORNER_LAYER)
		{
			adjacentTilesSimilar = getAdjacentTilesSimilar(x,y,TOP_LAYER);
			type = SUBTILE_TYPE.NONE;
			SUBTILE_TYPE top_subtile = getTerrainDataAtLoc(x,y,TOP_LAYER).subtileType;
			if(top_subtile == SUBTILE_TYPE.TOPLEFT || 
					top_subtile == SUBTILE_TYPE.BOTTOMLEFT ||
					top_subtile == SUBTILE_TYPE.TOPRIGHT || 
					top_subtile == SUBTILE_TYPE.BOTTOMRIGHT){
				
				type = SUBTILE_TYPE.getAuxTile(adjacentTilesSimilar);
			}
			
			
			
			
			return new TerrainTileData(tiles[TOP_LAYER][y][x],type);
		}
		
		

		
		return new TerrainTileData(tiles[z][y][x],type);
	}

	private boolean[] getAdjacentTilesSimilar(int x, int y, int z) {
		
		boolean[] tilesSimilar = new boolean[8];
		
		for(int i=0;i<8;i++)
		{
			if(withinMapBounds(x,y,z) && withinMapBounds(x+SIDES.values()[i].getX(),y+SIDES.values()[i].getY(),z)){
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


	static PerlinNoiseGenerator biomenoise = new PerlinNoiseGenerator((long) (Math.random()*10000));
	static PerlinNoiseGenerator tilenoise = new PerlinNoiseGenerator((long) (Math.random()*10000));
	static PerlinNoiseGenerator patchvarietynoise = new PerlinNoiseGenerator((long) (Math.random()*10000));
	
	private void generateTiles() {
				
		//biomemap
		
		patchvarietynoise.generatePerlinNoise(Statics.MAP_SIZE, Statics.MAP_SIZE, 3,25,0.2f);
		
		biomenoise.generatePerlinNoise(Statics.MAP_SIZE, Statics.MAP_SIZE, 7,25,0.05f);
		
		tilenoise.generatePerlinNoise(Statics.MAP_SIZE, Statics.MAP_SIZE, 5,25,0.05f);
				
		for(int x=0;x<Statics.MAP_SIZE;x++){
			for(int y=0;y<Statics.MAP_SIZE;y++){
							
				
				tiles[0][y][x] =  getBiomeTypeAtLocation(x,y).getBaseTerrainType().getID(); //the type of terrain (grass etc)
				
				tiles[1][y][x] = getTileTypeAtLocation(x,y).getID();  
				
				
				
			}
		}
		
		
		
		
		
		
		
	}

	private TERRAIN_TYPES getTileTypeAtLocation(int x, int y) {

		BIOME_TYPES biomeType = getBiomeTypeAtLocation(x,y); 
		
		float tileValue = Math.abs(tilenoise.getValue(x, y));
				
		TERRAIN_TYPES type = biomeType.getTerrainTypeFromNoiseValue(tileValue);
		
		if(type == TERRAIN_TYPES.NONE)
		{
			if(patchvarietynoise.getValue(x, y) < 0.3f)
			{
				type = biomeType.getBaseTerrainType();
			}			
		}
		
		
		return  type;
	}





	private BIOME_TYPES getBiomeTypeAtLocation(int x, int y) {
		float value = biomenoise.getValue(x, y)*5;
		
		if(value >= BIOME_TYPES.values().length)
		{
			value = BIOME_TYPES.values().length - 1;
		}
		
		
		return BIOME_TYPES.values()[(int) value];
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
		
		CORNERTOPRIGHT(2,0),
		CORNERTOPLEFT(1,0),
		CORNERBOTTOMLEFT(1,1),
		CORNERBOTTOMRIGHT(2,1),
		
		TOP(1,2),
		RIGHT(2,3),
		BOTTOM(1,4),
		LEFT(0,3),
		
		TOPRIGHT(2,2),		
		BOTTOMRIGHT(2,4),		
		BOTTOMLEFT(0,4),		
		TOPLEFT(0,2),
		
		NONE(0,0),
		PATCH1(0,0),
		PATCH2(0,1),
		
		;
		
		int x;
		int y;
		SUBTILE_TYPE(int x, int y)
		{
			this.x=x;
			this.y=y;			
		}
		
		

		
		private static SUBTILE_TYPE[] getAuxValues() {
			
			//checks in opposite order
			return new SUBTILE_TYPE[]{TOPLEFT,BOTTOMLEFT,BOTTOMRIGHT,TOPRIGHT};
		}


		
		
		
		public boolean[] RequiredNeighbors() {
			boolean[] valids = new boolean[]{true,true,true,true,true,true,true,true};
			
			
			switch(this)
			{
			//bases need all 8 matches
			case BASE: case BASE1: case BASE2: case BASE3: return valids;
			
			//big corners need 7 matches
			case CORNERBOTTOMLEFT: valids[SIDES.TOPRIGHT.ordinal()] = false;  return valids;
			case CORNERBOTTOMRIGHT: valids[SIDES.TOPLEFT.ordinal()] = false;  return valids;
			case CORNERTOPLEFT: valids[SIDES.BOTTOMRIGHT.ordinal()] = false;  return valids;
			case CORNERTOPRIGHT: valids[SIDES.BOTTOMLEFT.ordinal()] = false;  return valids;
			
			 			 
			//sides need 5 matches
			case LEFT: valids[SIDES.TOPLEFT.ordinal()] = false;  valids[SIDES.LEFT.ordinal()] = false;  valids[SIDES.BOTTOMLEFT.ordinal()] = false; return valids;
			case RIGHT: valids[SIDES.TOPRIGHT.ordinal()] = false;  valids[SIDES.RIGHT.ordinal()] = false;  valids[SIDES.BOTTOMRIGHT.ordinal()] = false; return valids;
			case BOTTOM: valids[SIDES.BOTTOMRIGHT.ordinal()] = false;  valids[SIDES.BOTTOM.ordinal()] = false;  valids[SIDES.BOTTOMLEFT.ordinal()] = false; return valids;
			case TOP: valids[SIDES.TOPRIGHT.ordinal()] = false;  valids[SIDES.TOP.ordinal()] = false;  valids[SIDES.TOPLEFT.ordinal()] = false; return valids;
			
			
			default: break;
			
			}
			
			valids = new boolean[8];
			
			switch(this)
			{
			
			//small corners need 3 matches
			case TOPRIGHT: valids[SIDES.BOTTOMLEFT.ordinal()] = true; valids[SIDES.BOTTOM.ordinal()] = true; valids[SIDES.LEFT.ordinal()] = true; return valids;
			case BOTTOMLEFT: valids[SIDES.TOPRIGHT.ordinal()] = true; valids[SIDES.TOP.ordinal()] = true; valids[SIDES.RIGHT.ordinal()] = true;  return valids;
			case BOTTOMRIGHT: valids[SIDES.TOPLEFT.ordinal()] = true; valids[SIDES.TOP.ordinal()] = true; valids[SIDES.LEFT.ordinal()] = true; return valids;
			case TOPLEFT: valids[SIDES.BOTTOMRIGHT.ordinal()] = true; valids[SIDES.BOTTOM.ordinal()] = true; valids[SIDES.RIGHT.ordinal()] = true; return valids;
			
			
			}
			
			
			
			//patches need nothing
			return new boolean[8];
			
		}
		
		
		
		public static SUBTILE_TYPE getFromAdjacentTileMap(int x, int y,boolean[] sames ) {
			
			SUBTILE_TYPE type = checkGroupSubtileTypes(sames);
			
			
			
			return type;
			
		//	return type;
		}
		
		
		
		
		public static SUBTILE_TYPE checkGroupSubtileTypes(boolean[] sames ) {
			
			tiletypeloop:
			for(SUBTILE_TYPE type : SUBTILE_TYPE.values())
			{
				for(int side =0; side < sames.length; side++)
					{
						if(type.RequiredNeighbors()[side])
						{
							//if this side is required, it must actually exist. otherwise, move to the next tile type
							if(sames[side] == false)
							{
								continue tiletypeloop; 
							}
							
						}				
					}
				//if all the side requirements were met, this is the right tiletype. requirements continue to get easier with each successive tiletype.
				return type;
				
			}	
			
			return SUBTILE_TYPE.NONE;
			
		//	return type;
		}
		
		
		
		public static SUBTILE_TYPE getAuxTile(boolean[] sames ) {
			

			tiletypeloop:
				for(SUBTILE_TYPE type : SUBTILE_TYPE.getAuxValues()) //draw the corners in the opposite order, on top
				{
					for(int side =0; side < sames.length; side++)
						{
							if(type.RequiredNeighbors()[side])
							{
								//if this side is required, it must actually exist. otherwise, move to the next tile type
								if(sames[side] == false)
								{
									continue tiletypeloop; 
								}
								
							}				
						}
					//if all the side requirements were met, this is the right tiletype. requirements continue to get easier with each successive tiletype.
					return type;
					
				}	
				
				return SUBTILE_TYPE.NONE;
				
		}
		
		
		
		

		public Vector2f getOffset() {			
			return new Vector2f(x,y);
		}
		
	}
	
	

	enum BIOME_TYPES
	{
		PLAINS(new BiomeTileRoll[]{new BiomeTileRoll(TERRAIN_TYPES.GRASS1, 0.30f),new BiomeTileRoll(TERRAIN_TYPES.SHORTGRASS, 0.10f) ,new BiomeTileRoll(TERRAIN_TYPES.GRASS2, 0.20f),new BiomeTileRoll(TERRAIN_TYPES.WHEAT, 0.10f)   }),
		GRASS(new BiomeTileRoll[]{new BiomeTileRoll(TERRAIN_TYPES.GRASS1, 0.30f),new BiomeTileRoll(TERRAIN_TYPES.SHORTGRASS, 0.10f) ,new BiomeTileRoll(TERRAIN_TYPES.TALLGRASS, 0.10f)   }),
		DUNES(new BiomeTileRoll[]{new BiomeTileRoll(TERRAIN_TYPES.SAND, 0.40f),new BiomeTileRoll(TERRAIN_TYPES.DIRT1, 0.20f) ,new BiomeTileRoll(TERRAIN_TYPES.SANDPIT, 0.10f)   }),
		BEACH(new BiomeTileRoll[]{new BiomeTileRoll(TERRAIN_TYPES.GRASS1, 0.30f),new BiomeTileRoll(TERRAIN_TYPES.SHORTGRASS, 0.10f) ,new BiomeTileRoll(TERRAIN_TYPES.TALLGRASS, 0.10f)   }),
		FOREST(new BiomeTileRoll[]{new BiomeTileRoll(TERRAIN_TYPES.GRASS2, 0.30f),new BiomeTileRoll(TERRAIN_TYPES.SHORTGRASS, 0.10f) ,new BiomeTileRoll(TERRAIN_TYPES.DARKGRASS, 0.10f)   }),
		JUNGLE(new BiomeTileRoll[]{new BiomeTileRoll(TERRAIN_TYPES.GRASS1, 0.30f),new BiomeTileRoll(TERRAIN_TYPES.SHORTGRASS, 0.10f) ,new BiomeTileRoll(TERRAIN_TYPES.TALLGRASS, 0.20f)   }),
		SWAMP(new BiomeTileRoll[]{new BiomeTileRoll(TERRAIN_TYPES.DARKGRASS, 0.30f),new BiomeTileRoll(TERRAIN_TYPES.POND, 0.20f) ,new BiomeTileRoll(TERRAIN_TYPES.SHORTGRASS, 0.20f)   })
		;
		
		float runningOffset;
		BiomeTileRoll[] rolls;
		
		BIOME_TYPES(BiomeTileRoll[] rolls)
		{
			
			for(int i=0; i < rolls.length; i++ )
			{
				rolls[i].offset = runningOffset; 
				runningOffset += rolls[i].chance;
			}
			
			this.rolls=rolls;
			
		}
		
		
		public TERRAIN_TYPES getBaseTerrainType(){			
			return rolls[0].type;
		}
		
		
		public TERRAIN_TYPES getTerrainTypeFromNoiseValue(float val)
		{
			for(BiomeTileRoll roll : rolls )
			{
				if(val > roll.offset && val < roll.offset+roll.chance  )
				{
					
					
					
					return roll.type;
				}
			}
			
			
			return TERRAIN_TYPES.NONE;
			
		}
		
		
	}
	
	static class BiomeTileRoll
	{
		TERRAIN_TYPES type;
		float chance;
		float offset;
		
		 BiomeTileRoll(TERRAIN_TYPES type,float chance)
		{
			this.type=type;
			this.chance=chance;
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
		SAND("sand"),
		SANDPIT("sandpit"),
		WHEAT("wheat"),
		NONE(""),
		PATCHES(""),
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
