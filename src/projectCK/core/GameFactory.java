package projectCK.core;

import projectCK.core.map.Map;
import projectCK.core.player.Player;

public class GameFactory {

	private final Map map;

	public GameFactory(Map map) {
		this.map = map;
	}

	public Game createBasicGame(Player[] players, int max_population) {
		Game game = new Game(map, players, max_population);
		game.init();
		return game;
	}
	
	public Game createSkirmishGame(Player[] players, int max_population) {
		SkirmishGame game = new SkirmishGame(map, players, max_population);
		game.init();
		return game;
	}

}
