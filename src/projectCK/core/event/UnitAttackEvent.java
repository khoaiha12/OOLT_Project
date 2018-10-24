package projectCK.core.event;

import projectCK.core.Game;
import projectCK.core.GameListener;
import projectCK.core.unit.Unit;
import projectCK.core.unit.UnitToolkit;

public class UnitAttackEvent implements GameEvent {

	private final Game game;
	private final Unit attacker;
	private final Unit defender;

	public UnitAttackEvent(Game game, Unit attacker, Unit defender) {
		this.game = game;
		this.attacker = attacker;
		this.defender = defender;
	}

	protected Game getGame() {
		return game;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public void execute(GameListener listener) {
		int attack_damage = UnitToolkit.getDamage(attacker, defender, getGame().getMap());
		doDamage(attacker, defender, attack_damage, listener);
		if (defender.getCurrentHp() > 0) {
			if (UnitToolkit.canCounter(defender, attacker)) {
				int counter_damage = UnitToolkit.getDamage(defender, attacker, getGame().getMap());
				doDamage(defender, attacker, counter_damage, listener);
		} 
		}
	}

	private void doDamage(Unit attacker, Unit defender, int damage, GameListener listener) {
		damage = defender.getCurrentHp() > damage ? damage : defender.getCurrentHp();
		defender.setCurrentHp(defender.getCurrentHp() - damage);
		listener.onUnitAttack(attacker, defender, damage);
		if (defender.getCurrentHp() <= 0) {
			new UnitDestroyEvent(getGame(), defender).execute(listener);
		}
	}


	}
