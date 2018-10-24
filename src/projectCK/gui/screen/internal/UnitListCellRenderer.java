package projectCK.gui.screen.internal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import projectCK.Language;
import projectCK.core.GameManager;
import projectCK.gui.ResourceManager;

public class UnitListCellRenderer implements ListCellRenderer<Object> {
	
	private final int ts;
	private final GameManager manager;
	
	public UnitListCellRenderer(GameManager manager, int ts) {
		this.ts = ts;
		this.manager = manager;
	}

	@Override
	public Component getListCellRendererComponent(
			
		@SuppressWarnings("rawtypes") 
		JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Integer unit_index = (Integer) value;
		UnitLabel label = new UnitLabel(unit_index, list.getWidth());
		label.setSelected(isSelected);
		return label;
	}

	private class UnitLabel extends JLabel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final int index;
		private boolean selected;

		public UnitLabel(int index, int width) {
			this.index = index;
			this.setPreferredSize(new Dimension(width - 18, ts / 2 * 3));
			this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}

		public void setSelected(boolean b) {
			this.selected = b;
		}

		@Override
		public void paint(Graphics g) {
			if (selected) {
				g.setColor(Color.GRAY);
			} else {
				g.setColor(Color.DARK_GRAY);
			}
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			int team = manager.getGame().getCurrentTeam();
			g.drawImage(ResourceManager.getUnitImage(team, index), ts / 4 - ts / 24, ts / 4 - ts / 24, this);

			((Graphics2D) g).setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			String unit_name = Language.getText("LB_UNIT_NAME_" + index + "_0");
			g.setColor(Color.WHITE);
			g.setFont(ResourceManager.getLabelFont());
			FontMetrics fm = g.getFontMetrics();
			g.drawString(unit_name,
					fm.stringWidth(" ") + ts / 2 * 3,
					(getHeight() - fm.getHeight()) / 2 + fm.getAscent());
			((Graphics2D) g).setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}

	}

}
