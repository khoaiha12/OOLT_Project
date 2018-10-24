
package projectCK.core.animation;

import projectCK.core.unit.Unit;

public interface AnimationDispatcher {
	
	public void updateAnimation();
	
	public void submitAnimation(Animation animation);
	
	public Animation getCurrentAnimation();
	
	public boolean isAnimating();
	
	public void submitOccupyAnimation();
	
	public void submitRepairAnimation();
	
	public void submitUnitMoveAnimation(Unit unit, int start_x, int start_y, int dest_x, int dest_y);
	
	public void submitTurnStartAnimation(int turn, int income, int team);
	
	public void submitGameOverAnimation(int alliance);
	
}
