/*
 * CRITTERS Runner.java
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

public class Runner extends Critter {
    protected static final int MIN_FIGHT_AMMO = 5;
    protected static String [] types = {"Prototype", "Army", "FNIX", "Apocalypse"};
    private int ammo;
    private String type;
    private int speed;
    int dir;

    public Runner() {
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

        if(type.equals(types[0]))
            speed = 2;
        else if(type.equals(types[1]))
            ammo = 3;
        else if(type.equals(types[2]))
            ammo = 4;
        else
            ammo = 5;
    }

    public Runner(String type) {
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
     * Runner always fights, but when
     * ammo is less than MIN_FIGHT_AMMO the
     * Runner will run away.
     */
    @Override
    public boolean fight(String oponent) {
        if(Critter.getRandomInt(10) == 0)
            callBackup();

        if(ammo < MIN_FIGHT_AMMO) {
            runAway();
            return false;
        }
        return true;
    }

    @Override
    public void doTimeStep() {
        walk(dir);
        if(getEnergy() >= Params.MIN_REPRODUCE_ENERGY && Critter.getRandomInt(100) % 4 == 0) {
            Runner child = new Runner();
            if(type.equals(types[0]))
                child = new Runner(types[0]);
            else if(type.equals(types[1]))
                child = new Runner (types[Critter.getRandomInt(2)]);
            else if(type.equals(types[2]))
                child = new Runner(types[Critter.getRandomInt(3)]);

            child.ammo = this.ammo/2;
            reproduce(child, Critter.getRandomInt(8));

        }

    }

    /**
     * returns statistics of entire Runner runner.
     * @param runners
     */
    public static String runStats(List<Critter> runners) {
    	String stats = "";
        int prototype = 0;
        int army = 0;
        int fnix = 0;
        int apocalypse = 0;


        for (Object obj : runners) {
            Runner runn = (Runner) obj;
            if(runn.type.equals(types[0]))
                prototype++;
            else if (runn.type.equals(types[1]))
                army++;
            else if (runn.type.equals(types[2]))
                fnix++;
            else if (runn.type.equals(types[3]))
                apocalypse++;

        }

        stats += Integer.toString(runners.size()) + " total Runners   \n";
        stats += Double.toString(((double)prototype/runners.size()) * 100) + "% Prototype  \n";
        stats += Double.toString(((double)army/runners.size()) * 100) + "% Army  \n";
        stats += Double.toString(((double)fnix/runners.size()) * 100) + "% FNIX  \n";
        stats += Double.toString(((double)apocalypse/runners.size()) * 100) + "% Apocalypse";
        
        return stats;
    }

    @Override
    public String toString() {
        return "R";
    }

    /**
     * increases ammo by 50%
     */
    private void callBackup() {
        ammo += ammo/2;
    }

    private void  runAway(){

        move(dir, speed);

}

	@Override
	public CritterShape viewShape() {
		return CritterShape.TRIANGLE;
	}
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.BLACK; }
	
	@Override
	public javafx.scene.paint.Color viewFillColor() { return javafx.scene.paint.Color.BLACK; }
}
