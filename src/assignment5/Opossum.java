/*
 * CRITTERS Opossum.java
 * EE422C Project 5 submission by
 * David Kossia
 * sdk927
 * 17365
 * Thibault Tonnel
 * tt26585
 * 17375
 * Slip days used: <1>
 * Spring 2022
 * https://github.com/EE422C/sp-22-assignment-4-sp22-pr4-pair-19.git
 */

package assignment5;

import java.util.List;

import assignment5.Critter.CritterShape;

public class Opossum extends Critter {
    protected static String [] types = {"Brown", "Short-Tailed", "American"};
    private String type;
    private int flee_chance;
    int dir;

    public Opossum() {
        type = types[Critter.getRandomInt(3)];
        dir = Critter.getRandomInt(8);

        if(type.equals(types[0]))
            flee_chance = 4;

        else if(type.equals(types[1]))
            flee_chance = 3;
        else
            flee_chance = 2;

    }

    public Opossum(String type) {
        this.type = type;
        dir = Critter.getRandomInt(8);

        if(type.equals(types[0]))
            flee_chance = 4;

        else if(type.equals(types[1]))
            flee_chance = 3;
        else
            flee_chance = 2;
    }

    /**
     * Opossums never fight, but they can run away depending on their flee chance.
     */
    @Override
    public boolean fight(String oponent) {
        int rand = Critter.getRandomInt(10);

        if(rand <= flee_chance){
            run(dir);
        }


        return false;
    }

    @Override
    public void doTimeStep() {
        walk(dir);
        if(getEnergy() >= Params.MIN_REPRODUCE_ENERGY && Critter.getRandomInt(100) % 4 == 0) {
            Opossum child = new Opossum();
            if(type.equals(types[0]))
                child = new Opossum(types[0]);
            else if(type.equals(types[1]))
                child = new Opossum (types[Critter.getRandomInt(1)]);
            else
                child = new Opossum(types[Critter.getRandomInt(2)]);

            reproduce(child, Critter.getRandomInt(8));

        }

    }

    /**
     * returns statistics of entire Opossum population.
     * @param opossums
     */
    public static String runStats(List<Critter> opossums) {
    	String stats = "";
        int brown = 0;
        int shortt = 0;
        int american = 0;


        for (Object obj : opossums) {
            Opossum Op = (Opossum) obj;
            if(Op.type.equals(types[0]))
                brown++;
            else if (Op.type.equals(types[1]))
                shortt++;
            else if (Op.type.equals(types[2]))
                american++;

        }

        stats += Integer.toString(opossums.size()) + " total Opossums   \n";
        stats += Double.toString(((double)brown/opossums.size()) * 100) + "% Brown  \n";
        stats += Double.toString(((double)shortt/opossums.size()) * 100) + "% Short-Tailed  \n";
        stats += Double.toString(((double)american/opossums.size()) * 100) + "% American";
        
        return stats;
    }

    @Override
    public String toString() {
        return "O";
    }

    @Override
	public CritterShape viewShape() {
		return CritterShape.CIRCLE;
	}
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.GREY; }
	
	@Override
	public javafx.scene.paint.Color viewFillColor() { return javafx.scene.paint.Color.GREY; }


}
