package projectCK.gui.sprite;

import java.awt.Graphics;

import projectCK.core.map.Tile;
import projectCK.core.map.TileRepository;
import projectCK.gui.ResourceManager;

public class TilePainter {

	private static int current_frame_index = 0;

	private TilePainter() {
	}

	public static void updateFrame() {
		current_frame_index = current_frame_index == 0 ? 1 : 0;
	}

	public static void paint(Graphics g, int index, int x, int y, int ts) {
		Tile tile = TileRepository.getTile(index);
		if (tile.isAnimated()) {
			if (current_frame_index == 0) {
				g.drawImage(
						ResourceManager.getTileImage(index), x, y, ts, ts, null);
			} else {
				g.drawImage(
						ResourceManager.getTileImage(tile.getAnimationTileIndex()), x, y, ts, ts, null);
			}
		} else {
			g.drawImage(
					ResourceManager.getTileImage(index), x, y, ts, ts, null);
		}
	}

}
