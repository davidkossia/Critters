/*
 * CRITTERS Critter.java
 * EE422C Project 5 submission by
 * David Kossia
 * sdk927
 * 17365
 * Thibault Tonnel
 * tt26585
 * 17360
 * Slip days used: <1>
 * Spring 2022
 */

package assignment5;

import java.util.*;
import javafx.*;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

//import assignment4.Critter;
//import assignment4.InvalidCritterException;
//import assignment4.Params;

/*
 * See the PDF for descriptions of the methods and fields in this
 * class.
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {

    /* START --- NEW FOR PROJECT 5 */
    public enum CritterShape {
        CIRCLE,
        SQUARE,
        TRIANGLE,
        DIAMOND,
        STAR
    }

    /* the default color is white, which I hope makes critters invisible by default
     * If you change the background color of your View component, then update the default
     * color to be the same as you background
     *
     * critters must override at least one of the following three methods, it is not
     * proper for critters to remain invisible in the view
     *
     * If a critter only overrides the outline color, then it will look like a non-filled
     * shape, at least, that's the intent. You can edit these default methods however you
     * need to, but please preserve that intent as you implement them.
     */
    public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.THISTLE;
    }

    public javafx.scene.paint.Color viewOutlineColor() {
        return viewColor();
    }

    public javafx.scene.paint.Color viewFillColor() {
        return viewColor();
    }

    public abstract CritterShape viewShape();

    protected final String look(int direction, boolean steps) {
    	int dist;
    	int x = this.x_coord;
    	int y = this.y_coord;
    	
    	this.energy -= Params.LOOK_ENERGY_COST;
    	
    	if(steps == true)
    		dist = 2;
    	else
    		dist = 1;
    	
    	switch(direction) {
    	
    	//east
    	case 0:
    		x = x + dist;
    	
    	//northeast
    	case 1:
    		x += dist;
    		y += dist;
    	
    	//north
    	case 2:
    		y += dist;
    		
    	//northwest
    	case 3:
    		x -= dist;
    		y += dist;
    	
    	//west
    	case 4:
    		x -= dist;
    	
    	//southwest
    	case 5:
    		x -= dist;
    		y -= dist;
    	
    	//south
    	case 6:
    		y -= dist;
    	
    	//southeast
    	case 7:
    		x += dist;
    		y -= dist;
    		
    	default:
    		break;
    	}
    	

    	if(x > Params.WORLD_WIDTH -1)
    		x -= Params.WORLD_WIDTH;
    	
    	else if(x < 0)
    		x += Params.WORLD_WIDTH;
    	
    	if (y > Params.WORLD_HEIGHT - 1)
    		y -= Params.WORLD_HEIGHT;
    	
    	else if (y < 0)
    		y += Params.WORLD_HEIGHT;
    	
    	for (Critter c : population) {
    		if (sameSpot(x, y, c))
    			return c.toString();
    	}
        return "";
    }
    
    private static boolean sameSpot( int x, int y, Critter c) {
    	if (x == c.x_coord && y == c.y_coord)
    		return true;
    	else
    		return false;
    }

    public static String runStats(List<Critter> critters) {
        String str = "" + critters.size() + " critters:  ";
        Map<String, Integer> cmap = new HashMap<String, Integer>();
        int i = 0;
        while (i < critters.size()) {
            Critter c = critters.get(i);
            String cstr = c.toString();
            cmap.put(cstr, cmap.getOrDefault(cstr, 0) + 1);
            i++;
        }
        String format = "";
        Iterator<String> iterator = cmap.keySet().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            str += format + s + ":" + cmap.get(s);
            format = ", ";
        }
        return (str + "\n");
    }

    public static void displayWorld(Object pane) {
        GridPane grid = (GridPane) pane;
        Node node = grid.getChildren().get(0);
        grid.getChildren().clear();
        grid.getChildren().add(0,node);

        List<Critter> population = Critter.getPop();
        for (int i = 0; i < population.size(); i++) {
            Critter c = population.get(i);
            switch (c.viewShape()) {
                case CIRCLE:
                    Circle circ = new Circle();
                    circ.setRadius(Main.circle.getRadius());
                    circ.setFill(c.viewFillColor());
                    circ.setStroke(c.viewOutlineColor());
                    Main.red_line.add(circ, c.x_coord, c.y_coord);
                    break;
                default:
                    Polygon poly = new Polygon();
                    poly.setFill(c.viewFillColor());
                    poly.setStroke(c.viewFillColor());
                    switch (c.viewShape()) {
                        case SQUARE:
                            poly.getPoints().addAll(Main.square.getPoints());
                            break;
                        case TRIANGLE:
                            poly.getPoints().addAll(Main.triangle.getPoints());
                            break;
                        case DIAMOND:
                            poly.getPoints().addAll(Main.diamond.getPoints());
                            break;
                        default:
                            poly.getPoints().addAll(Main.star.getPoints());
                            break;
                    }

                    Main.red_line.add(poly, c.x_coord, c.y_coord);
                    break;
            }
        }

    }
    public static List<Critter> getPop(){
        return population;
    }
  
    

	/* END --- NEW FOR PROJECT 5
			rest is unchanged from Project 4 */

    private int energy = 0;
    
    private boolean moveflag = false;

    private int x_coord;
    private int y_coord;
    
    private static Critter[][] critGrid = new Critter[Params.WORLD_HEIGHT][Params.WORLD_WIDTH];
    private static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();

    /* Gets the package name.  This assumes that Critter and its
     * subclasses are all in the same package. */
    private static String myPackage;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static Random rand = new Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new Random(new_seed);
    }

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the qualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown.
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void createCritter(String critter_class_name)
            throws InvalidCritterException {
    	try{
            Critter new_critter = (Critter) Class.forName(myPackage + "." + critter_class_name).newInstance();
            new_critter.energy = Params.START_ENERGY;
            new_critter.x_coord = getRandomInt(Params.WORLD_WIDTH);
            new_critter.y_coord = getRandomInt(Params.WORLD_HEIGHT);
            population.add(new_critter);
            critGrid[((Critter)new_critter).y_coord][((Critter)new_critter).x_coord] = (Critter)new_critter;

        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException e){
            throw new InvalidCritterException(critter_class_name);
        }
    }

    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.
     *                           Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name)
            throws InvalidCritterException {
    	try{
            Critter new_critter = (Critter) Class.forName(myPackage + "." + critter_class_name).newInstance();
            new_critter.energy = Params.START_ENERGY;
            new_critter.x_coord = getRandomInt(Params.WORLD_WIDTH);
            new_critter.y_coord = getRandomInt(Params.WORLD_HEIGHT);
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException e){
            throw new InvalidCritterException(critter_class_name);
        }
        try {
            Critter my_critter = (Critter) Class.forName(myPackage + "." + critter_class_name).newInstance();
            ArrayList<Critter> critter_list = new ArrayList<>();
            for (int i = 0; i < population.size(); i++) {
                Critter c = population.get(i);
                if (my_critter.getClass().isInstance(c)) critter_list.add(c);
            }
            return critter_list;
        }
        catch(InstantiationException|IllegalAccessException|ClassNotFoundException e){
            throw new InvalidCritterException(critter_class_name);
        }
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
    	
    	population.clear();
    	babies.clear();
    	for(int i = 0; i < critGrid.length; i++) {
    		for(int j = 0; j < critGrid[0].length; j++)
            critGrid[i][j] = null;
        }
    }
    /**
     * Simulates one time step for each Critter
     */
    public static void worldTimeStep() {
    	for(int i = 0; i < population.size();i++){
            population.get(i).moveflag = false;
            population.get(i).doTimeStep();
            population.get(i).energy -= Params.REST_ENERGY_COST;
            if(population.get(i).energy <= 0){
                population.remove(i);
                i--;
            }
        }
        spawnClovers();
        doEncounters();
        population.addAll(babies);
        babies.clear();
        for(Critter c : population)
        	critGrid[c.y_coord][c.x_coord] = c;
    }
    
    /**
     * Generates RERESH_CLOVER_COUNT Clovers at random
     * places on the map
     */
    public static void spawnClovers() {
        int i = 0;
        while (i < Params.REFRESH_CLOVER_COUNT) {
            try {
                createCritter("Clover");
            } catch (InvalidCritterException e) {
                e.printStackTrace();
            }
            i++;
        }
    }
    
    /**
     * Handles encounters between Critters in the same space.
     * 
     * If Critter A and B are in the same space, they fight via
     * a dice roll, whichever Critter wins get's half of the losing
     * Critter's energy.
     */
    public static void doEncounters(){
        boolean flag_A, flag_B;
        int i = 0;
        while (i < population.size()-1) {
            int j = i+1;
            while (j < population.size()) {
                Critter critterA = population.get(i);
                Critter critterB = population.get(j);
                if((critterA.x_coord == critterB.x_coord)
                        && (critterA.y_coord == critterB.y_coord)){ //same coords, trigger encounter
                    boolean winflagA = false;
                    flag_A = critterA.fight(critterB.toString());
                    flag_B = critterB.fight(critterA.toString());
                    //critters decision
                    if((critterA.energy > 0 && critterB.energy > 0)
                            && (critterB.x_coord == critterA.x_coord)
                            && (critterA.y_coord == critterB.y_coord)){
                        int dice_roll_A, dice_roll_B;
                        if(!flag_A) dice_roll_A = 0; //energy set to 0 since A opted not to fight
                        else
                            dice_roll_A = getRandomInt(critterA.energy); //dice roll
                        if(!flag_B)
                            dice_roll_B = 0; //B's energy set to 0
                        else
                            dice_roll_B = getRandomInt(critterB.energy); //dice roll

                        if (dice_roll_A >= dice_roll_B) winflagA = true; //flag determines winner
                        //arbitrary winner is A if energy is equal
                            else winflagA = false;

                        if(winflagA) { //A won
                            critterA.energy += (critterB.energy / 2);
                            population.remove(j);
                            j--;
                        }if(!winflagA){ //A lost
                            critterB.energy += (critterA.energy / 2);
                            population.remove(i);
                            i--;
                            j = population.size();
                        }
                    }
                    if(!(critterA.energy > 0)){ //double check for escape
                        population.remove(i);
                        i--;
                        j = population.size();
                    }
                    if(!(critterB.energy > 0)){
                        population.remove(j);
                        j--;
                    }
                }j++;
            }i++;
        }
    }
    
    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }

    protected int getEnergy() {
        return energy;
    }

    /**
     * Moves a Critter one unit in direction
     * @param direction
     */
    protected final void walk(int direction) {
        if (!moveflag) {
            moveflag = true;
            energy -= Params.WALK_ENERGY_COST;
            move(direction, 1);
            return;
        }else
            return;
    }

    /**
     * Moves a Critter two units in direction
     * @param direction
     */
    protected final void run(int direction) {
        if (!moveflag) {
            moveflag = true;
            energy -= Params.RUN_ENERGY_COST;
            move(direction, 2);
            return;
        }else
            return;
    }

    /**
     * Spawns offspring adjacent to parent Critter
     * in direction.
     * @param offspring
     * @param direction
     */
    protected final void reproduce(Critter offspring, int direction) {
        if(energy < Params.MIN_REPRODUCE_ENERGY) return; //can't reproduce
        offspring.energy = (int)Math.floor(energy/2);
        energy = (int)Math.ceil(energy/2);
        offspring.x_coord = x_coord;
        offspring.y_coord = y_coord;
        offspring.move(direction, 1);
        babies.add(offspring);
    }
    
    /**
     * Moves a Critter dist units in direction
     * @param direction
     * @param dist
     */
    protected final void move(int direction, int dist){
        //direction input 0-7, start right, move counter-clockwise

        if(direction == 0)
            x_coord += dist; //right
        else if(direction == 1){
            x_coord += dist;
            y_coord -= dist; //up-right
        }
        else if(direction == 2)
            y_coord -= dist; //up
        else if(direction == 3){
            x_coord -= dist;
            y_coord -= dist; //up-left
        }
        else if(direction == 4)
            x_coord-= dist; //left
        else if(direction == 5){
            x_coord -= dist;
            y_coord += dist; //down-left
        }
        else if(direction == 6)
            y_coord += dist; //down
        else if(direction == 7){
            x_coord += dist;
            y_coord += dist; //down-right
        }
        //"wrap around"
        if(x_coord < 0)
            x_coord += Params.WORLD_WIDTH - 1;
        if(y_coord < 0)
            y_coord += Params.WORLD_HEIGHT - 1;
        if(x_coord > Params.WORLD_WIDTH - 1)
            x_coord -= Params.WORLD_WIDTH - 1;
        if(y_coord > Params.WORLD_HEIGHT - 1)
            y_coord -= Params.WORLD_HEIGHT - 1;
    }
    
    
    
    

    

    /**
     * The TestCritter class allows some critters to "cheat". If you
     * want to create tests of your Critter model, you can create
     * subclasses of this class and then use the setter functions
     * contained here.
     * <p>
     * NOTE: you must make sure that the setter functions work with
     * your implementation of Critter. That means, if you're recording
     * the positions of your critters using some sort of external grid
     * or some other data structure in addition to the x_coord and
     * y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you
         * are not using the population ArrayList that has been
         * provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }

        /**
         * This method getBabies has to be modified by you if you are
         * not using the babies ArrayList that has been provided in
         * the starter code.  In any case, it has to be implemented
         * for grading tests to work.  Babies should be added to the
         * general population at either the beginning OR the end of
         * every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }
    
    
}