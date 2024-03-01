/* CRITTERS Critter.java
 * 
 */
package assignment5;

import java.util.Random;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Painter {

	static int size = 100;

	/*
	 * Paint the grid lines in orange.  The purpose is two-fold -- to indicate boundaries of 
	 * icons, and as place-holders for empty cells.  Without placeholders, grid may not display properly.
	 */
	private static void paintGridLines(GridPane grid) {
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				Shape s = new Rectangle(size, size);
				s.setFill(null);
				s.setStroke(Color.ORANGE);
				grid.add(s, i, j);
			}
		}
	}

	
	/* 
	 * Paints the icon shapes on a grid. 
	 */
	public static void paint(GridPane grid) {
		grid.getChildren().clear(); // clear the grid 
		paintGridLines(grid);		// paint the borders
		for (int i = 0; i <=2 ; i++) {
			Shape s = getIcon(i);
			grid.add(s, i, i); // add along the diagonal
		}
	}

	/* 
	 * Returns a square or a circle depending on the shapeIndex parameter
	 * 
	 */
	static Shape getIcon(int shapeIndex) {
		Shape s = null;
		
		switch(shapeIndex) {
		case 0: s = new Rectangle(size, size);
			s.setFill(Color.RED); break;
		case 1: s = new Circle(size/2);
			s.setFill(Color.GREEN); break;
		default: s = new Circle(size/2);
		s.setFill(Color.PINK);
		}
		// set the outline
		s.setStroke(Color.BLUE);
		return s;
	}
	
	/* 
	 * Paints the icon shapes on a grid. 
	 */
	public static void paintRandom(GridPane grid) {
		Random rand = new Random();
		grid.getChildren().clear(); // clear the grid 
		paintGridLines(grid);		// paint the borders
		for (int i = 0; i <= 2 ; i++) {
			Shape s = getIcon(i);
			grid.add(s, rand.nextInt(3), rand.nextInt(3)); 
		}
	}
}
