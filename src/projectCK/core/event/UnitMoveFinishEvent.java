
package projectCK.core.event;

import projectCK.core.GameListener;
import projectCK.core.unit.Unit;

public class UnitMoveFinishEvent implements GameEvent {
	
	private final Unit unit;
	private final int start_x;
	private final int start_y;
	
	public UnitMoveFinishEvent(Unit unit, int start_x, int start_y) {
		this.unit = unit;
		this.start_x = start_x;
		this.start_y = start_y;
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public void execute(GameListener listener) {
		listener.onUnitMoveFinish(unit, start_x, start_y);
	}
	
}
