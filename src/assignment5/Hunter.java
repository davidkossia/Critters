/*
 * CRITTERS Hunter.java
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
/*
 * Critter inspired by the anime Hunter x Hunter.
 * Enhancer and Specialist have greater amounts of nen/energy than other types.
 * Specialist has a 25% chance to get an energy boost when entering a fight
 * All types except manipulators will fight regardless of current energy
 * The manipulator will run if energy is below 10% of original
 */

import assignment5.Critter.CritterShape;

public class Hunter extends Critter {
	protected static String [] types = {"Emitter", "Enhancer", "Conjurer", "Transmutter", "Specialist", "Manipulator"};
	private String nen_type;
	private int nen;
	private int dir;
	
	public Hunter() {
		nen_type = types[Critter.getRandomInt(6)];
		if(nen_type.equals(types[1]) || nen_type.equals(types[4]))
		{
			nen = Critter.getRandomInt(50);
		}
		else
			nen = Critter.getRandomInt(20);
		dir = Critter.getRandomInt(8);
	}
	
	public Hunter(String type) {
		super();
		nen_type = type;
		if(nen_type.equals(types[1]) || nen_type.equals(types[4]))
		{
			nen = Critter.getRandomInt(50);
		}
		else
			nen = Critter.getRandomInt(20);
		dir = Critter.getRandomInt(8);
	}
	
	/**
	 * Hunter will fight unless a Manipulator with less
	 * than 10% of starting energy.
	 * @param opponent
	 */
	public boolean fight(String opponent) {
		if(nen_type.equals(types[4])) {
			if(Critter.getRandomInt(4) == 0)
				nen += 15;
		}
		
		if (nen_type.equals(types[4]) && getEnergy() < Params.START_ENERGY/10) {
			if (super.getEnergy() >= Params.RUN_ENERGY_COST)
				run(Critter.getRandomInt(8));
			return false;
		}
		
		if(nen > 1)
			nen -= 2;
		
		return true;
	}
	
	/*
	 * Returns energy with nen boost
	 */
	@Override
	protected int getEnergy() {
		return super.getEnergy() + nen;
	}
	
	/**
	 * Hunter walks in dir set direction.
	 * 1 in 16 chance of reproducing.
	 * Child nen_type based on nen tree from the anime.
	 */
	@Override
	public void doTimeStep() {
		walk(dir);
		nen++;
		if(getEnergy() >= Params.MIN_REPRODUCE_ENERGY && Critter.getRandomInt(100) % 6 == 0) {
			 Hunter kid = new Hunter();
			 int kid_type = Critter.getRandomInt(3);
			 /* Nen type of child determined based off of
			  * Hunter X Hunter Nen tree
			  * 
			  * 0 : Emitter
			  * 1 : Enhancer
			  * 2 : Conjurer
			  * 3 : Transmuter
			  * 4 : Specialist
			  * 5 : Manipulator
			  */
			 if (nen_type.equals(types[0])) {
				if (kid_type == 0)
					kid = new Hunter(types[0]);
				else if (kid_type == 1)
					kid = new Hunter (types[5]);
				else
					kid = new Hunter (types[1]);
			 }
			 
			else if (nen_type.equals(types[1])) {
				if (kid_type == 0)
					kid = new Hunter (types[1]);
				else if (kid_type == 1)
					kid = new Hunter (types[0]);
				else
					kid = new Hunter (types[3]);
			}
			 
			else if (nen_type.equals(types[2])) {
				if (kid_type == 0)
					kid = new Hunter (types[2]);
				else if (kid_type == 1)
					kid = new Hunter (types[3]);
				else
					kid = new Hunter (types[4]);
			}
			 
			else if (nen_type.equals(types[3])) {
				if (kid_type == 0)
					kid = new Hunter (types[3]);
				else if (kid_type == 1)
					kid = new Hunter (types[1]);
				else
					kid = new Hunter (types[2]);
				
			}
			 
			else if (nen_type.equals(types[4])) {
				if (Critter.getRandomInt(2) == 0)
					kid = new Hunter (types[4]);
			}
			 
			else if (nen_type.equals(types[5])) {
				if (kid_type == 0)
					kid = new Hunter (types[5]);
				else if (kid_type == 1)
					kid = new Hunter (types[4]);
				else
					kid = new Hunter (types[0]);
			}
			kid.nen = this.nen/2;
			reproduce(kid, Critter.getRandomInt(8));
		}
	}
	
	/**
	 * returns statistics of all Hunters in the world.
	 * @param hunters
	 */
	public static String runStats(List<Critter> hunters) {
		String stats = "";
		int emitters = 0;
		int enhancers = 0;
		int conjurers = 0;
		int transmuters = 0;
		int specialists = 0;
		int manipulators = 0;
		
		for (Object obj : hunters) {
			Hunter hunter = (Hunter) obj;
			if(hunter.nen_type.equals(types[0]))
				emitters++;
			else if (hunter.nen_type.equals(types[1]))
				enhancers++;
			else if (hunter.nen_type.equals(types[2]))
				conjurers++;
			else if (hunter.nen_type.equals(types[3]))
				transmuters++;
			else if (hunter.nen_type.equals(types[4]))
				specialists++;
			else if (hunter.nen_type.equals(types[5]))
				manipulators++;
		}
		
		stats += Integer.toString(hunters.size()) + " total Hunters   ";
		stats += Double.toString(((double)emitters/hunters.size()) * 100) + "% Emitters  \n";
		stats += Double.toString(((double)enhancers/hunters.size()) * 100) + "% Enhancers  \n";
		stats += Double.toString(((double)conjurers/hunters.size()) * 100) + "% Conjurers  \n";
		stats += Double.toString(((double)transmuters/hunters.size()) * 100) + "% Transmuters  \n";
		stats += Double.toString(((double)specialists/hunters.size()) * 100) + "% Specialists  \n";
		stats += Double.toString(((double)manipulators/hunters.size()) * 100) + "% Manipulators";
		
		return stats;
	}
	
	@Override
	public String toString() {
		return "H";
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.DIAMOND;
	}
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.DEEPPINK; }
	
	@Override
	public javafx.scene.paint.Color viewFillColor() { return javafx.scene.paint.Color.DEEPPINK; }
}
