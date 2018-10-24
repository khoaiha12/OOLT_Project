package projectCK.gui.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import projectCK.Language;
import projectCK.core.Game;
import projectCK.core.creator.GameCreator;
import projectCK.core.creator.GameCreatorListener;
import projectCK.core.map.Map;
import projectCK.core.map.MapProvider;
import projectCK.gui.AEIIApplet;
import projectCK.gui.ResourceManager;
import projectCK.gui.Screen;
import projectCK.gui.control.AEIIButton;
import projectCK.gui.screen.internal.MapListCellRenderer;
import projectCK.gui.util.ResourceUtil;

public class GameCreateScreen extends Screen implements GameCreatorListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final AEIIButton btn_back = new AEIIButton(Language.getText("LB_BACK"));
	private final AEIIButton btn_play = new AEIIButton(Language.getText("LB_PLAY"));
	@SuppressWarnings("rawtypes")
	private final JList map_list = new JList();

	private GameCreator game_creator;
	private MapProvider map_provider;

	public GameCreateScreen(Dimension size, AEIIApplet context) {
		super(size, context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initComponents() {
		this.setLayout(null);
		int width = getPreferredSize().width;
		int height = getPreferredSize().height;
		//menu to choose map
		btn_back.setBounds(width/2, height-ts/2, width/4, ts/2);
		btn_back.addActionListener(btn_back_listener);
		
		this.add(btn_back);
		btn_play.setBounds(btn_back.getWidth()+ width/2, height-ts/2, width/4, ts/2);
		btn_play.addActionListener(btn_play_listener);
		
		this.add(btn_play);
		//list map 
		map_list.setFocusable(false);
		map_list.setBackground(Color.DARK_GRAY);
		map_list.setCellRenderer(new MapListCellRenderer(ts));
		map_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		map_list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = map_list.getSelectedIndex();
				Map map = map_provider.getMap(index);
				game_creator.setMap(map);
			}
		});
		JScrollPane sp_map_list = new JScrollPane(map_list);
		sp_map_list.setBounds(0, 0, width, height-ts/2);
		sp_map_list.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(sp_map_list);
	}

	public void setGameCreator(GameCreator creator) {
		creator.setGameCreatorListener(this);
		this.game_creator = creator;
	}

	public void setMapProvider(MapProvider provider) {
		this.map_provider = provider;
	}

	@SuppressWarnings("unchecked")
	public void reloadMaps() {
		String[] maps = new String[map_provider.getMapCount()];
		for (int index = 0; index < maps.length; index++) {
			maps[index] = map_provider.getMapName(index);
			
		}
		map_list.setListData(maps);
		if(maps.length > 0) {
			map_list.setSelectedIndex(0);
		}
	}

	@Override
	public void onMapChanged(Map map) {
		updateButtons();
	}

	private void updateButtons() {
		btn_play.setEnabled(game_creator.canCreate());
	}

	@Override
	public void paint(Graphics g) {
		paintBackground(g);
		super.paint(g);
		ResourceUtil.paintBorder(g, 0, 0, getWidth(), getHeight());
	}

	private void paintBackground(Graphics g) {
		g.setColor(ResourceManager.getAEIIPanelBg());
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private final ActionListener btn_back_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			getContext().gotoMainMenuScreen();
		}
	};

	private final ActionListener btn_play_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Game game = game_creator.create();
			getContext().gotoGameScreen(game, AEIIApplet.MODE_LOCAL);
		}
	};

}
