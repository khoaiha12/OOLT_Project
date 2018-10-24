
package projectCK.core.creator;

import projectCK.core.Game;
import projectCK.core.map.Map;

public interface GameCreator {
	
	public boolean canChangeProperty();
	
	public void setGameCreatorListener(GameCreatorListener listener);
	
	public Map getMap();
	
	public void setMap(Map map);
	
	public void setMaxPopulation(int population);
	
	public void setStartGold(int gold);
	
	public boolean canCreate();
	
	public Game create();
	
}
