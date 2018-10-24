
package projectCK.core.animation;

import projectCK.core.unit.Unit;

public interface AnimationProvider {
	
	public Animation getOccupiedMessageAnimation();
	
	public Animation getRepairedMessageAnimation();
	
	public Animation getUnitMoveAnimation(Unit unit, int start_x, int start_y, int dest_x, int dest_y);
	
	public Animation getTurnStartAnimation(int turn, int income, int team);
    
    public Animation getGameOverAnimation(int allience);
	
}
