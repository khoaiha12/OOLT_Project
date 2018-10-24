package projectCK.core.event;

import projectCK.core.GameListener;
import projectCK.core.unit.Unit;

public class UnitActionFinishEvent implements GameEvent {

	private final Unit unit;

	public UnitActionFinishEvent(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public void execute(GameListener listener) {
		listener.onUnitActionFinish(unit);
	}

}
