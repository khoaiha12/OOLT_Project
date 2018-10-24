package projectCK.gui.screen.internal;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JPanel;

import projectCK.Language;
import projectCK.Launcher;
import projectCK.core.creator.GameCreator;
import projectCK.core.creator.SkirmishGameCreator;
import projectCK.core.map.MapProvider;
import projectCK.gui.AEIIApplet;
import projectCK.gui.AEIIPanel;
import projectCK.gui.Screen;
import projectCK.gui.control.AEIIButton;

public class MainMenu extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ID_WELCOME_MENU = "welcome_menu";
	public static final String ID_SINGLE_PLAYER_MENU = "single_player_menu";
	
	@SuppressWarnings("unused")
	private static final File map_dir = new File("map/");

	private final AEIIApplet context;
	private final CardLayout menu_container;
	
	//welcome menu
	private final AEIIButton btn_start
			= new AEIIButton(Language.getText("LB_START"));
	private final AEIIButton btn_exit
			= new AEIIButton(Language.getText("LB_EXIT"));

	public MainMenu(AEIIApplet context) {
		this.context = context;
		this.menu_container = new CardLayout();
	}

	public void initComponents(int ts, Screen screen) {
		int menu_width = ts * 4;
		int menu_height = ts / 2 * 3 + ts / 4 * 2;
		int menu_x = ts*8 + ts/2;
		int menu_y = ts * 5;
		this.setBounds(menu_x, menu_y, menu_width, menu_height);
		this.setLayout(menu_container);
		JPanel welcome_menu = new AEIIPanel();
		welcome_menu.setLayout(null);
		btn_start.setBounds(ts / 4, ts / 4, ts * 4 - ts / 2, ts / 2);
		btn_start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameCreator game_creator = new SkirmishGameCreator();
				MapProvider map_provider = getContext().getLocalMapProvider();
				getContext().gotoGameCreateScreen(game_creator, map_provider);
			}
		});
		welcome_menu.add(btn_start);
		btn_exit.setBounds(ts / 4, ts / 4 * 2 + ts / 2, ts * 4 - ts / 2, ts / 2);
		btn_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Launcher.exit();
			}
		});
		welcome_menu.add(btn_exit);
		this.add(welcome_menu, ID_WELCOME_MENU);
	}
	
	public AEIIApplet getContext() {
		return context;
	}

	public void showMenu(String menu_id) {
		menu_container.show(this, menu_id);
		this.revalidate();
	}

}
