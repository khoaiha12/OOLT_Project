package projectCK.gui;

import java.awt.Graphics;
import javax.swing.JPanel;

import projectCK.gui.util.ResourceUtil;

public class AEIIPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AEIIPanel() {
		this.setOpaque(false);
	}

	@Override
	public void paint(Graphics g) {
		paintBackground(g);
		super.paint(g);
		ResourceUtil.paintBorder(g, 0, 0, getWidth(), getHeight());
	}
	
	protected void paintBackground(Graphics g) {
		g.setColor(ResourceManager.getAEIIPanelBg());
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}
