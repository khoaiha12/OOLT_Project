package projectCK.gui.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;

import projectCK.core.Game;
import projectCK.core.GameManager;
import projectCK.core.LocalGameManager;
import projectCK.core.Point;
import projectCK.core.animation.Animation;
import projectCK.core.map.Tile;
import projectCK.core.map.TileRepository;
import projectCK.core.map.Tomb;
import projectCK.core.unit.Unit;
import projectCK.gui.AEIIApplet;
import projectCK.gui.ResourceManager;
import projectCK.gui.Screen;
import projectCK.gui.animation.MapAnimation;
import projectCK.gui.animation.UnitAnimation;
import projectCK.gui.screen.internal.ActionBar;
import projectCK.gui.screen.internal.UnitStore;
import projectCK.gui.sprite.TilePainter;
import projectCK.gui.sprite.UnitPainter;

public class MapCanvas extends Screen {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LocalGameManager manager;
    private final GameScreen game_screen;
    private final int SPRITE_INTERVAL = 8;

    private final ActionBar action_bar;
    private final UnitStore unit_store;

    private Rectangle viewport;

    private int current_delay;

    private int mouse_x;
    private int mouse_y;

    public MapCanvas(Dimension size, AEIIApplet context, GameScreen game_screen) {
        super(size, context);
        this.game_screen = game_screen;
        this.setLayout(null);
        this.setOpaque(false);
        action_bar = new ActionBar(this, ts);
        unit_store = new UnitStore(this, ts);
    }

    public void init() {
        this.add(action_bar);
        this.add(unit_store);
        unit_store.setVisible(false);
        viewport = new Rectangle(0, 0, getWidth(), getHeight());
    }

    public void setGameManager(LocalGameManager manager) {
        this.manager = manager;
        this.action_bar.setGameManager(manager);
        this.unit_store.setGameManager(manager);
        locateViewport(0, 0);
        mouse_x = 0;
        mouse_y = 0;
        current_delay = 0;
        this.action_bar.setVisible(false);
    }

    public boolean isOperatable() {
        if (manager != null) {
            return getGame().isLocalPlayer()
                    && !manager.isGameOver()
                    && !manager.isProcessing();
        } else {
            return false;
        }
    }

    public boolean isAnimating() {
        return manager.isAnimating();
    }

    public void closeAllInternalWindows() {
        if (unit_store.isVisible()) {
            closeUnitStore();
        }
        game_screen.getActionPanel().update();
    }
    
    private boolean isOnUnitAnimation(int x, int y) {
        if (isAnimating()) {
            Animation current_animation = manager.getCurrentAnimation();
            if (current_animation instanceof UnitAnimation) {
                return ((UnitAnimation) current_animation).hasLocation(x, y);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean needTombDisplay(int x, int y) {
        Animation current_animation = manager.getCurrentAnimation();
        if (current_animation == null || !(current_animation instanceof MapAnimation)) {
            return true;
        } else {
            if (((MapAnimation) current_animation).hasLocation(x, y)) {
                return false;
            } else {
                return true;
            }

        }
    }

    public void closeUnitStore() {
        unit_store.setVisible(false);
        game_screen.getActionPanel().update();
    }

    public void showUnitStore(int map_x, int map_y) {
        int store_width = unit_store.getPreferredSize().width;
        int store_height = unit_store.getPreferredSize().height;
        unit_store.setBounds(
                (getWidth() - store_width) / 2,
                (getHeight() - store_height) / 2,
                store_width, store_height);
        unit_store.display(map_x, map_y);
        game_screen.getActionPanel().update();
    }
    
    protected void onMousePress(MouseEvent e) {
        synchronized (getContext().getUpdateLock()) {
            if (isOperatable()) {
                int click_x = getCursorXOnMap();
                int click_y = getCursorYOnMap();
                if (e.getButton() == MouseEvent.BUTTON1) {
                    switch (manager.getState()) {
//                        case GameManager.STATE_PREVIEW:
//                            manager.cancelPreviewPhase();
                        case GameManager.STATE_SELECT:
                            doSelect(click_x, click_y);
                            break;
                        case GameManager.STATE_MOVE:
                        case GameManager.STATE_RMOVE:
                            manager.moveSelectedUnit(click_x, click_y);
                            break;
                        case GameManager.STATE_ATTACK:
                            manager.doAttack(click_x, click_y);
                            break;
                        default:
                        //do nothing
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    switch (manager.getState()) {
//                        case GameManager.STATE_PREVIEW:
                        case GameManager.STATE_SELECT:
//                            if (getGame().getMap().getUnit(click_x, click_y) != null) {
//                                manager.beginPreviewPhase(click_x, click_y);
//                            } else {
//                                manager.cancelPreviewPhase();
//                                action_bar.setVisible(false);
//                            }
                            break;
                        case GameManager.STATE_MOVE:
                            manager.cancelMovePhase();
                            break;
                        case GameManager.STATE_ACTION:
                            manager.reverseMove();
                            break;
                        case GameManager.STATE_ATTACK:
//                        case GameManager.STATE_SUMMON:
//                        case GameManager.STATE_HEAL:
                            manager.cancelActionPhase();
                            break;
                        default:
                        //do nothing
                    }
                }
                game_screen.getActionPanel().update();
            }
        }
    }

    protected void onMouseMove(MouseEvent e) {
        mouse_x = e.getX();
        mouse_y = e.getY();
    }

    private void doSelect(int x, int y) {
        Unit unit = manager.getUnit(x, y);
        int target_team = -1;
        int current_team = getGame().getCurrentTeam();
        if (unit != null) {
            manager.selectUnit(x, y);
            target_team = unit.getTeam();
        }
        if (manager.getUnitToolkit().isUnitAccessible(unit)
                || (manager.isAccessibleCastle(x, y) && !getGame().isEnemy(current_team, target_team))) {
            action_bar.display(x, y);
        } else {
            action_bar.setVisible(false);
        }
    }

    public boolean isWithinPaintArea(int sx, int sy) {
        return -ts < sx && sx < viewport.width && -ts < sy && sy < viewport.height;
    }

    public boolean isWithinCanvas(int sx, int sy) {
        return 0 < sx && sx < viewport.width && 0 < sy && sy < viewport.height;
    }

    public int getCursorXOnMap() {
        int map_width = manager.getGame().getMap().getWidth();
        int cursor_x = (mouse_x + viewport.x) / ts;
        if (cursor_x >= map_width) {
            return map_width - 1;
        }
        if (cursor_x < 0) {
            return 0;
        }
        return cursor_x;
    }

    public int getCursorYOnMap() {
        int map_height = manager.getGame().getMap().getHeight();
        int cursor_y = (mouse_y + viewport.y) / ts;
        if (cursor_y >= map_height) {
            return map_height - 1;
        }
        if (cursor_y < 0) {
            return 0;
        }
        return cursor_y;
    }

    public int getXOnCanvas(int map_x) {
        int sx = viewport.x / ts;
        sx = sx > 0 ? sx : 0;
        int x_offset = sx * ts - viewport.x;
        return (map_x - sx) * ts + x_offset;
    }

    public int getYOnCanvas(int map_y) {
        int sy = viewport.y / ts;
        sy = sy > 0 ? sy : 0;
        int y_offset = sy * ts - viewport.y;
        return (map_y - sy) * ts + y_offset;
    }

    private Game getGame() {
        if (manager != null) {
            return manager.getGame();
        } else {
            return null;
        }
    }

    @Override
    public void update() {
        if (current_delay < SPRITE_INTERVAL) {
            current_delay++;
        } else {
            current_delay = 0;
            TilePainter.updateFrame();
/*            UnitPainter.updateFrame();*/
        }
    }

    public void hideActionBar() {
        if (action_bar.isVisible()) {
            action_bar.setVisible(false);
        }
    }

    public void updateActionBar() {
        if (isOperatable()) {
            Unit unit = manager.getSelectedUnit();
            switch (manager.getState()) {
                case GameManager.STATE_ACTION:
                    action_bar.display(unit.getX(), unit.getY());
                    break;
                case GameManager.STATE_SELECT:
                    Unit selected_unit = manager.getSelectedUnit();
                    if (manager.getUnitToolkit().isUnitAccessible(selected_unit)) {
                        action_bar.display(unit.getX(), unit.getY());
                    } else {
                        action_bar.setVisible(false);
                    }
                    break;
                default:
                    action_bar.setVisible(false);
            }
        } else {
            action_bar.setVisible(false);
        }
    }
    
    public Point getViewportLocation() {
        return new Point(viewport.x, viewport.y);
    }
    
    public Point createViewportLocation(int map_x, int map_y) {
        Point location = new Point();
        int center_sx = map_x * ts;
        int center_sy = map_y * ts;
        int map_width = getGame().getMap().getWidth() * ts;
        int map_height = getGame().getMap().getHeight() * ts;
        if (viewport.width < map_width) {
            location.x = center_sx - (viewport.width - ts) / 2;
            if (location.x < 0) {
                location.x = 0;
            }
            if (location.x > map_width - viewport.width) {
                location.x = map_width - viewport.width;
            }
        } else {
            location.x = (map_width - viewport.width) / 2;
        }
        if (viewport.height < map_height) {
            location.y = center_sy - (viewport.height - ts) / 2;
            if (location.y < 0) {
                location.y = 0;
            }
            if (location.y > map_height - viewport.height) {
                location.y = map_height - viewport.height;
            }
        } else {
            location.y = (map_height - viewport.height) / 2;
        }
        return location;
    }

    public void locateViewport(int map_x, int map_y) {
        Point location = createViewportLocation(map_x, map_y);
        viewport.x = location.x;
        viewport.y = location.y;
    }

    public void dragViewport(int delta_x, int delta_y) {
        int map_width = getGame().getMap().getWidth() * ts;
        int map_height = getGame().getMap().getHeight() * ts;
        if (viewport.width < map_width) {
            if (0 <= viewport.x + delta_x
                    && viewport.x + delta_x <= map_width - viewport.width) {
                viewport.x += delta_x;
            } else {
                viewport.x = viewport.x + delta_x < 0 ? 0 : map_width - viewport.width;
            }
        } else {
            viewport.x = (map_width - viewport.width) / 2;
        }
        if (viewport.height < map_height) {
            if (0 <= viewport.y + delta_y
                    && viewport.y + delta_y <= map_height - viewport.height) {
                viewport.y += delta_y;
            } else {
                viewport.y = viewport.y + delta_y < 0 ? 0 : map_height - viewport.height;
            }
        } else {
            viewport.y = (map_height - viewport.height) / 2;
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        paintTiles(g, ts);
        if (!isAnimating() && getGame().isLocalPlayer()) {
            switch (manager.getState()) {
                case GameManager.STATE_RMOVE:
                case GameManager.STATE_MOVE:
                    paintMoveAlpha(g);
                    paintMovePath(g, ts);
                    break;
                case GameManager.STATE_ATTACK:
                    paintAttackAlpha(g);
                    break;
                default:
                //do nothing
            }
        }
        paintTombs(g);
        paintUnits(g);
        paintAnimation(g);
        super.paint(g);
    }

    private void paintTiles(Graphics g, int ts) {
        for (int x = 0; x < getGame().getMap().getWidth(); x++) {
            for (int y = 0; y < getGame().getMap().getHeight(); y++) {
                int sx = getXOnCanvas(x);
                int sy = getYOnCanvas(y);
                if (isWithinPaintArea(sx, sy)) {
                    int index = getGame().getMap().getTileIndex(x, y);
                    TilePainter.paint(g, index, sx, sy, ts);
                    Tile tile = TileRepository.getTile(index);
                    if (tile.getTopTileIndex() != -1) {
                        int top_tile_index = tile.getTopTileIndex();
                        g.drawImage(
                                ResourceManager.getTopTileImage(top_tile_index),
                                sx, sy - ts, this);
                    }
                }
            }
        }
    }

    private void paintTombs(Graphics g) {
        ArrayList<Tomb> tomb_list = manager.getGame().getMap().getTombList();
        for (Tomb tomb : tomb_list) {
            if (needTombDisplay(tomb.x, tomb.y)) {
                int sx_tomb = getXOnCanvas(tomb.x);
                int sy_tomb = getYOnCanvas(tomb.y);
                g.drawImage(ResourceManager.getTombImage(), sx_tomb, sy_tomb, this);
            }
        }
    }

    private void paintMoveAlpha(Graphics g) {
        ArrayList<Point> movable_positions = manager.getMovablePositions();
        for (Point position : movable_positions) {
            int sx = getXOnCanvas(position.x);
            int sy = getYOnCanvas(position.y);
            if (isWithinPaintArea(sx, sy)) {
                g.drawImage(ResourceManager.getMoveAlpha(), sx, sy, this);
            }
        }
    }

    private void paintMovePath(Graphics g, int ts) {
        g.setColor(ResourceManager.getMovePathColor());
        Unit unit = manager.getSelectedUnit();
        int start_x = unit.getX();
        int start_y = unit.getY();
        int dest_x = getCursorXOnMap();
        int dest_y = getCursorYOnMap();
        ArrayList<Point> move_path = manager.getUnitToolkit().createMovePath(unit, start_x, start_y, dest_x, dest_y);
        for (int i = 0; i < move_path.size(); i++) {
            if (i < move_path.size() - 1) {
                Point p1 = move_path.get(i);
                Point p2 = move_path.get(i + 1);
                if (p1.x == p2.x) {
                    int x = p1.x;
                    int y = p1.y < p2.y ? p1.y : p2.y;
                    int sx = getXOnCanvas(x);
                    int sy = getYOnCanvas(y);
                    g.fillRect(sx + ts / 3, sy + ts / 3, ts / 3, ts / 3 * 4);
                }
                if (p1.y == p2.y) {
                    int x = p1.x < p2.x ? p1.x : p2.x;
                    int y = p1.y;
                    int sx = getXOnCanvas(x);
                    int sy = getYOnCanvas(y);
                    g.fillRect(sx + ts / 3, sy + ts / 3, ts / 3 * 4, ts / 3);
                }
            } else {
                Point dest = move_path.get(i);
                int sx = getXOnCanvas(dest.x);
                int sy = getYOnCanvas(dest.y);
                g.drawImage(ResourceManager.getMoveTargetCursorImage(), sx, sy, this);
            }
        }
    }

    private void paintAttackAlpha(Graphics g) {
        ArrayList<Point> attackable_positions = manager.getAttackablePositions();
        for (Point position : attackable_positions) {
            int sx = getXOnCanvas(position.x);
            int sy = getYOnCanvas(position.y);
            if (isWithinPaintArea(sx, sy)) {
                g.drawImage(ResourceManager.getAttackAlpha(), sx, sy, this);
            }
        }
    }

    private void paintUnits(Graphics g) {
        Set<Point> unit_positions = getGame().getMap().getUnitPositionSet();
        for (Point position : unit_positions) {
            Unit unit = manager.getUnit(position.x, position.y);
            //if this unit isn't animating, then paint it. otherwise, let animation paint it
            if (!isOnUnitAnimation(unit.getX(), unit.getY())) {
                int unit_x = unit.getX();
                int unit_y = unit.getY();
                int sx = getXOnCanvas(unit_x);
                int sy = getYOnCanvas(unit_y);
                if (isWithinPaintArea(sx, sy)) {
                    UnitPainter.paint(g, unit, sx, sy, ts);
                }
            }
        }
    }

    private void paintAnimation(Graphics g) {
        if (isAnimating()) {
            MapAnimation animation = (MapAnimation) manager.getCurrentAnimation();
            animation.paint(g, this);
        }
    }

}
