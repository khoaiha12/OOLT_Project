package projectCK.gui.animation;

import java.awt.Graphics;
import java.util.HashSet;

import projectCK.core.Point;
import projectCK.core.animation.Animation;
import projectCK.gui.screen.MapCanvas;

public class MapAnimation extends Animation {
	
	private final HashSet<Point> locations = new HashSet<Point>();
	
	public MapAnimation() {
	}

	public MapAnimation(int x, int y) {
		if (x >= 0 && y >= 0) {
			this.addLocation(x, y);
		}
	}
	
	public final void addLocation(int x, int y) {
		this.locations.add(new Point(x, y));
	}
	
	public final boolean hasLocation(int x, int y) {
		return locations.contains(new Point(x, y));
	}
	
	public void paint(Graphics g, MapCanvas canvas) {
	}

}
