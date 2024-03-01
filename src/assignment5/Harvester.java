/*
 * CRITTERS Harvester.java
 * EE422C Project 5 submission by
 * David Kossia
 * sdk927
 * 17365
 * Thibault Tonnel
 * tt26585
 * 17360
 * Slip days used: <1>
 * Spring 2022
 * https://github.com/EE422C/sp-22-assignment-4-sp22-pr4-pair-19.git
 */

package assignment5;

import java.util.List;

import assignment5.Critter.CritterShape;

public class Harvester extends Critter {
	protected static final int MIN_FIGHT_AMMO = 5;
	protected static String [] types = {"Prototype", "Army", "FNIX", "Apocalypse"};
	private int ammo;
	private String type;
	int dir;
	
	public Harvester() {
		type = types[Critter.getRandomInt(4)];
		dir = Critter.getRandomInt(8);
		
		if(type.equals(types[0]))
			ammo = MIN_FIGHT_AMMO + Critter.getRandomInt(20);
		else if(type.equals(types[1]))
			ammo = MIN_FIGHT_AMMO + Critter.getRandomInt(40);
		else if(type.equals(types[2]))
			ammo = MIN_FIGHT_AMMO + Critter.getRandomInt(60);
		else
			ammo = MIN_FIGHT_AMMO + Critter.getRandomInt(80);
	}
	
	public Harvester(String type) {
		this.type = type;
		dir = Critter.getRandomInt(8);
		
		if(type.equals(types[0]))
			ammo = MIN_FIGHT_AMMO + Critter.getRandomInt(20);
		else if(type.equals(types[1]))
			ammo = MIN_FIGHT_AMMO + Critter.getRandomInt(40);
		else if(type.equals(types[2]))
			ammo = MIN_FIGHT_AMMO + Critter.getRandomInt(60);
		else
			ammo = MIN_FIGHT_AMMO + Critter.getRandomInt(80);
	}
	
	/**
	 * Harvester always fights, but when
	 * ammo is less than MIN_FIGHT_AMMO the 
	 * Harvester will always lose. 
	 */
	@Override
	public boolean fight(String oponent) {
		if(Critter.getRandomInt(10) == 0)
			callBackup();
		
		if(ammo < MIN_FIGHT_AMMO)
			return false;
		ammo -= MIN_FIGHT_AMMO;
		return true;
	}
	
	@Override
	public void doTimeStep() {
		walk(dir);
		if(getEnergy() >= Params.MIN_REPRODUCE_ENERGY && Critter.getRandomInt(100) % 4 == 0) {
			Harvester child = new Harvester();
			if(type.equals(types[0]))
				child = new Harvester(types[0]);
			else if(type.equals(types[1]))
				child = new Harvester (types[Critter.getRandomInt(2)]);
			else if(type.equals(types[2]))
				child = new Harvester(types[Critter.getRandomInt(3)]);
			
			child.ammo = this.ammo/2;
			reproduce(child, Critter.getRandomInt(8));
			
		}
		
	}
	
	/**
	 * returns statistics of entire Harvester population.
	 * @param harvesters
	 */
	public static String runStats(List<Critter> harvesters) {
		String stats = "";
		int prototype = 0;
		int army = 0;
		int fnix = 0;
		int apocalypse = 0;
	
		
		for (Object obj : harvesters) {
			Harvester harv = (Harvester) obj;
			if(harv.type.equals(types[0]))
				prototype++;
			else if (harv.type.equals(types[1]))
				army++;
			else if (harv.type.equals(types[2]))
				fnix++;
			else if (harv.type.equals(types[3]))
				apocalypse++;

		}
		
		stats += Integer.toString(harvesters.size()) + " total Harvesters   \n";
		stats += Double.toString(((double)prototype/harvesters.size()) * 100) + "% Prototype  \n";
		stats += Double.toString(((double)army/harvesters.size()) * 100) + "% Army  \n";
		stats += Double.toString(((double)fnix/harvesters.size()) * 100) + "% FNIX  \n";
		stats += Double.toString(((double)apocalypse/harvesters.size()) * 100) + "% Apocalypse";
		
		return stats;
	}
	
	@Override
	public String toString() {
		return "V";
	}
	
	/**
	 * increases ammo by 50%
	 */
	private void callBackup() {
		ammo += ammo/2;
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.SQUARE;
	}
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.TOMATO; }
	
	@Override
	public javafx.scene.paint.Color viewFillColor() { return javafx.scene.paint.Color.TOMATO; }

}
