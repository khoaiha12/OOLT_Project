package projectCK;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import projectCK.core.AEIIException;
import projectCK.gui.AEIIApplet;
import projectCK.gui.util.DialogUtil;

public class Launcher implements Runnable {

	private final int TILE_SIZE;
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	
	private static JFrame main_frame;
	private static AEIIApplet aeii_applet;

	public Launcher(int ts, int width, int height) {
		this.TILE_SIZE = ts;
		this.SCREEN_WIDTH = width;
		this.SCREEN_HEIGHT = height;
	}

	@Override
	public void run() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException |
				InstantiationException |
				IllegalAccessException |
				UnsupportedLookAndFeelException ex) {
		}
		try {
			Configuration.init();
			Language.init();
			String title = Language.getText("LB_TITLE");
			main_frame = new JFrame(title);
			main_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			main_frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					exit();
				}
			});
			main_frame.setResizable(false);

			aeii_applet = new AEIIApplet(TILE_SIZE, SCREEN_WIDTH, SCREEN_HEIGHT);
			aeii_applet.init();
			main_frame.setContentPane(aeii_applet.getContentPane());

			main_frame.pack();
			main_frame.setLocationRelativeTo(null);
			main_frame.setVisible(true);
			aeii_applet.start();
		} catch (IOException ex) {
			DialogUtil.showError(null, ex.getMessage());
		}
	}

	public static AEIIApplet getApplet() {
		return aeii_applet;
	}

	public static JFrame getWindow() {
		return main_frame;
	}

	public static void exit() {
		aeii_applet.stop();
		main_frame.dispose();
		if(aeii_applet.isDebugMode()) {
			System.exit(0);
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			int ts = 48;
			int width = 1000;
			int height = 600;
			validateParam(ts, width, height);
			EventQueue.invokeLater(new Launcher(ts, width, height));
		} catch (Exception ex) {
			System.err.println(ex.getClass().toString() + ": " + ex.getMessage());
		}
	}

	private static void validateParam(int ts, int width, int height) throws Exception {
		if (ts < 0 || ts % 24 != 0) {
			throw new AEIIException("TILE_SIZE");
		}
	}

}
