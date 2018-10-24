
package projectCK.core.event;

import projectCK.core.GameListener;

public interface GameEvent {
	
	public boolean canExecute();
	
	public void execute(GameListener listener);
	
}
