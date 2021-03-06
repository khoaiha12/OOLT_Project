package projectCK.gui.screen.internal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import projectCK.gui.ResourceManager;

@SuppressWarnings("rawtypes")
public class MapListCellRenderer implements ListCellRenderer {

	private final int ts;

	public MapListCellRenderer(int ts) {
		this.ts = ts;
	}

	@Override
	public Component getListCellRendererComponent(
			JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		return new MapLabel(list.getWidth(), " "+(String)value, isSelected);
	}

	private class MapLabel extends JLabel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final boolean selected;

		public MapLabel(int width, String map_name, boolean selected) {
			super(map_name);
			this.selected = selected;
			this.setPreferredSize(new Dimension(width - 18, ts / 2));
			this.setForeground(Color.WHITE);
			this.setFont(ResourceManager.getLabelFont());
		}
		
		@Override
		public void paint(Graphics g) {
			if(selected) {
				g.setColor(Color.GRAY);
			} else {
				g.setColor(Color.DARK_GRAY);
			}
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			super.paint(g);
		}
		
	}

}
