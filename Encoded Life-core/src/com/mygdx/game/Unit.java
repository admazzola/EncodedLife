package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.chromosomes.Chromosome;

public class Unit {
	List<Chromosome> DNA = new ArrayList<Chromosome>();  //all based around energy!
	
	UnitModel model;
	
	
	public Unit()
	{
		model = new UnitModel();
		
		
		
	}
	
	

	public Actor getModel() {
		return model;
	}
	
	
	
	
	
	
	
	
}
