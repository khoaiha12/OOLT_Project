package projectCK.gui.screen;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import projectCK.core.Game;
import projectCK.core.GameManager;
import projectCK.core.GameManagerListener;
import projectCK.core.LocalGameManager;
import projectCK.core.Point;
import projectCK.core.animation.AnimationProvider;
import projectCK.gui.AEIIApplet;
import projectCK.gui.Screen;
import projectCK.gui.animation.SwingAnimatingProvider;

public class GameScreen extends Screen implements GameManagerListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LocalGameManager manager;
    private SwingAnimatingProvider animation_provider;

    private MapCanvas map_canvas;
    private StatusPanel status_panel;
    private ActionPanel action_panel;

    private boolean up_approached = false;
    private boolean down_approached = false;
    private boolean left_approached = false;
    private boolean right_approached = false;

    public GameScreen(Dimension size, AEIIApplet context) {
        super(size, context);
        this.setIgnoreRepaint(true);
    }

    @Override
    public void initComponents() {
        this.setLayout(null);
        int apw = ts * 3 + ts / 4; //action panel width
        int width = getPreferredSize().width;
        int height = getPreferredSize().height;
        //footer
        this.status_panel = new StatusPanel(ts);
        this.status_panel.setBounds(0, height - ts, width - apw, ts);
        this.add(status_panel);
        //main
        Dimension canvas_size = new Dimension(width - apw, height - ts);
        this.map_canvas = new MapCanvas(canvas_size, getContext(), this);
        this.map_canvas.setPreferredSize(canvas_size);
        this.map_canvas.setBounds(0, 0, width - apw, height - ts);
        this.map_canvas.init();
        this.add(map_canvas);
        //thanh ben
        this.action_panel = new ActionPanel(this, ts);
        this.action_panel.setBounds(width - apw, 0, apw, height);
        this.action_panel.initComponents(ts);
        this.add(action_panel);
        this.animation_provider = new SwingAnimatingProvider(this, ts);
        MouseAdapter mouse_adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (getCanvas().isWithinCanvas(e.getX(), e.getY())) {
                    getCanvas().onMousePress(e);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                checkBorderApporach(e);
                if (getCanvas().isWithinCanvas(e.getX(), e.getY())) {
                    getCanvas().onMouseMove(e);
                }
            }
        };
        this.addMouseMotionListener(mouse_adapter);
        this.addMouseListener(mouse_adapter);
    }

    public void setGameManager(LocalGameManager manager) {
        this.manager = manager;
        Game game = manager.getGame();
        this.manager.setStateListener(this);
        this.animation_provider.setGameManager(manager);
        this.map_canvas.setGameManager(manager);
        this.action_panel.setGameManager(manager);
        this.status_panel.setGameManager(manager);
        game.setGameListener(manager);
        game.startTurn();
        this.action_panel.update();
    }

    @Override
    public void onManagerStateChanged(GameManager manager) {
        getActionPanel().update();
        getCanvas().updateActionBar();
    }

    @Override
    public void onMapFocused(int map_x, int map_y) {
        //target viewport location
        @SuppressWarnings("unused")
		Point tlocation = getCanvas().createViewportLocation(map_x, map_y);
        //current viewport location
        @SuppressWarnings("unused")
		Point clocation = getCanvas().getViewportLocation();
    }

    @Override
    public void onGameEventCleared() {
        getActionPanel().update();
    }

    public ActionPanel getActionPanel() {
        return action_panel;
    }

    public MapCanvas getCanvas() {
        return map_canvas;
    }

    public AnimationProvider getAnimationProvicer() {
        return animation_provider;
    }

    @Override
    public void onKeyPress(KeyEvent e) {
        map_canvas.onKeyPress(e);
    }

    @Override
    public void onKeyRelease(KeyEvent e) {
        map_canvas.onKeyRelease(e);
    }

    private void checkBorderApporach(MouseEvent e) {
        left_approached = e.getX() < ts / 4;
        right_approached = e.getX() > getWidth() - ts / 4;
        up_approached = e.getY() < ts / 4;
        down_approached = e.getY() > getHeight() - ts / 4;
    }

    public boolean isUpApproached() {
        return up_approached;
    }

    public boolean isDownApproached() {
        return down_approached;
    }

    public boolean isLeftApproached() {
        return left_approached;
    }

    public boolean isRightApproached() {
        return right_approached;
    }

    @Override
    public void update() {
        if (manager != null) {
            map_canvas.update();
            manager.update();
        }
    }

}
