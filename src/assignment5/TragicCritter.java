package assignment5;

public class TragicCritter extends Critter {

	@Override
	public String toString() { return "T"; }

	public TragicCritter() {
		Params.LOOK_ENERGY_COST = 0;
		Params.WALK_ENERGY_COST = 0;
		Params.RUN_ENERGY_COST = 0;
		Params.REFRESH_CLOVER_COUNT = 0;
	}

	public boolean fight(String not_used) { return true; }

	@Override
	public void doTimeStep() {
		boolean moveFlag = true;
		/* Move to where its kin isn't */
		if(moveFlag) {
			for (int dir = 0; dir < 8; dir += 2) {
				if(this.look(dir, false) == null
					&& this.look(dir, true) == null
					&& this.look(dir == 0 ? 7 : dir - 1, false) == null
					&& this.look((dir + 1) % 8, false) == null) 
				{
					walk(dir);
					return;
				}
			}
		}
	}

	public static String runStats(java.util.List<Critter> avoidingCritters) {
		if(avoidingCritters.size() >= 2) {
			return "Still can't get to each other";
		}
		else {
			return "Collided, sadly";
		}
	}
	@Override
	public CritterShape viewShape() { return CritterShape.STAR; }

	@Override
	public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.CRIMSON; }
	
	@Override
	public javafx.scene.paint.Color viewFillColor() { return javafx.scene.paint.Color.CRIMSON; }

}
