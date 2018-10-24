package projectCK.gui;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.JDesktopPane;

public class Screen extends JDesktopPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final int ts;
	private final AEIIApplet context;

	public Screen(Dimension size, AEIIApplet context) {
		this.setPreferredSize(size);
		this.context = context;
		this.ts = context.getTileSize();
		this.setOpaque(false);
	}

	public void initComponents() {

	}

	protected final AEIIApplet getContext() {
		return context;
	}

	public void onKeyPress(KeyEvent e) {

	}

	public void onKeyRelease(KeyEvent e) {

	}

	public void update() {

	}

}
