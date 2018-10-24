package projectCK.gui.animation;

import projectCK.core.unit.Unit;
import projectCK.core.unit.UnitFactory;

public class UnitAnimation extends MapAnimation {

	private final Unit unit;

	public UnitAnimation(Unit unit, int x, int y) {
		super(x, y);
		if (unit != null) {
			this.unit = UnitFactory.cloneUnit(unit);
		} else {
			this.unit = null;
		}
	}

	public Unit getUnit() {
		return unit;
	}

}
