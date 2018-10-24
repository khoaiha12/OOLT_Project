package projectCK.gui.screen.internal;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

import projectCK.Language;
import projectCK.core.GameManager;
import projectCK.core.LocalGameManager;
import projectCK.core.unit.Unit;
import projectCK.gui.ResourceManager;
import projectCK.gui.control.ActionButton;
import projectCK.gui.screen.MapCanvas;

public class ActionBar extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int bw;
	private final int bh;
	private LocalGameManager manager;
	private final MapCanvas canvas;

	private final ActionButton btn_buy;
	private final ActionButton btn_standby;
	private final ActionButton btn_attack;
	private final ActionButton btn_move;
	private final ActionButton btn_occupy;

	private int map_x;
	private int map_y;

	public ActionBar(MapCanvas canvas, int ts) {
		this.bw = ts / 24 * 20;
		this.bh = ts / 24 * 21;
		this.canvas = canvas;
		this.setLayout(null);
		this.setOpaque(false);
		
		btn_attack = new ActionButton(2, ts);
		btn_attack.setToolTipText(Language.getText("LB_ATTACK"));
		btn_attack.addActionListener(btn_attack_listener);
		
		btn_move = new ActionButton(4, ts);
		btn_move.setToolTipText(Language.getText("LB_MOVE"));
		btn_move.addActionListener(btn_move_listener);
		
		btn_occupy = new ActionButton(1, ts);
		btn_occupy.setToolTipText(Language.getText("LB_OCCUPY"));
		btn_occupy.addActionListener(btn_occupy_listener);
	
		btn_standby = new ActionButton(5, ts);
		btn_standby.setToolTipText(Language.getText("LB_STANDBY"));
		btn_standby.addActionListener(btn_standby_listener);
		
		btn_buy = new ActionButton(0, ts);
		btn_buy.setToolTipText(Language.getText("LB_BUY"));
		btn_buy.addActionListener(btn_buy_listener);
		
	}

	public void setGameManager(LocalGameManager manager) {
		this.manager = manager;
	}

	public void display(int x, int y) {
		this.map_x = x;
		this.map_y = y;
		this.removeAll();
		if (manager.isAccessibleCastle(x, y)
				&& manager.getState() == GameManager.STATE_SELECT) {
			addButton(btn_buy);
		}
		Unit unit = manager.getSelectedUnit();
		if (manager.getUnitToolkit().isUnitAccessible(unit)
				&& unit.getX() == x && unit.getY() == y) {
			switch (manager.getState()) {
				case GameManager.STATE_SELECT:
					addButton(btn_move);
				case GameManager.STATE_ACTION:
					addButton(btn_attack);
					if (manager.getGame().canOccupy(unit, unit.getX(), unit.getY())) {
						addButton(btn_occupy);
					}
					addButton(btn_standby);
					break;
				default:
				//do nothing
			}
		}
		int btn_count = this.getComponentCount();
		if (btn_count > 0) {
			this.setBounds(0, canvas.getHeight() - bh * 2, btn_count * bw + (btn_count + 1) * bw / 4, bh);
			this.setVisible(true);
		}
	}

	private void addButton(ActionButton button) {
		int count = this.getComponentCount();
		button.setBounds(count * bw + (count + 1) * bw / 4, 0, bw, bh);
		this.add(button);
	}

	@Override
	public void paint(Graphics g) {
		((Graphics2D) g).setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		RoundRectangle2D background = new RoundRectangle2D.Float(0, bh / 2, getWidth(), bh / 2, 8, 8);
		g.setColor(ResourceManager.getAEIIPanelBg());
		((Graphics2D) g).fill(background);
		((Graphics2D) g).setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		super.paint(g);
	}

	private final ActionListener btn_move_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				manager.beginMovePhase();
			}
		}
	};

	private final ActionListener btn_attack_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				manager.beginAttackPhase();
			}
		}
	};

	private final ActionListener btn_occupy_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				Unit unit = manager.getSelectedUnit();
				manager.doOccupy(unit.getX(), unit.getY());
				setVisible(false);
			}
		}
	};

	private final ActionListener btn_standby_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				manager.standbySelectedUnit();
				setVisible(false);
			}
		}
	};

	private final ActionListener btn_buy_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				canvas.showUnitStore(map_x, map_y);
				setVisible(false);
			}
		}
	};

}
