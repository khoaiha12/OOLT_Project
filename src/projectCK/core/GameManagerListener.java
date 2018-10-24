
package projectCK.core;

public interface GameManagerListener {
	
	public void onManagerStateChanged(GameManager manager);
    
    public void onMapFocused(int map_x, int map_y);
    
    public void onGameEventCleared();
	
}
