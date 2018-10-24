package projectCK.gui;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import projectCK.Configuration;
import projectCK.Launcher;
import projectCK.core.AEIIException;
import projectCK.core.Game;
import projectCK.core.LocalGameManager;
import projectCK.core.creator.GameCreator;
import projectCK.core.map.LocalMapProvider;
import projectCK.core.map.MapProvider;
import projectCK.core.map.TileRepository;
import projectCK.core.unit.UnitFactory;
import projectCK.gui.screen.GameCreateScreen;
import projectCK.gui.screen.GameScreen;
import projectCK.gui.screen.MainMenuScreen;
import projectCK.gui.screen.internal.MainMenu;
import projectCK.gui.util.CharPainter;
import projectCK.gui.util.DialogUtil;

public class AEIIApplet {

    public static final String ID_MAIN_MENU_SCREEN = "main_menu";
    public static final String ID_GAME_CREATE_SCREEN = "game_create";
    public static final String ID_GAME_SCREEN = "game";

    public static final int MODE_LOCAL = 0x1;
    public static final int MODE_NET = 0x2;

    private final LocalMapProvider local_map_provider;

    private final Object FPS_LOCK = new Object();

    private boolean isRunning;
    private final boolean isDebugMode = true;

    private long fpsDelay;

    private Container content_pane;

    private final int TILE_SIZE;
    private final Dimension SCREEN_SIZE;

    private Screen current_screen;
    private MainMenuScreen main_menu_screen;
    private GameCreateScreen game_create_screen;
    private GameScreen game_screen;

    private CardLayout screen_container;

    public AEIIApplet(int ts, int width, int height) {
        this.TILE_SIZE = ts;
        SCREEN_SIZE = new Dimension(width, height);
        fpsDelay = 1000 / Configuration.getGameSpeed();

        File map_dir = new File("map/");
        local_map_provider = new LocalMapProvider(map_dir);
    }

    public void init() {
        /*ImageWaveEffect.createSinTab();*/
        content_pane = new Container();

        screen_container = new CardLayout();
        this.getContentPane().setLayout(screen_container);
        main_menu_screen = new MainMenuScreen(SCREEN_SIZE, this);
        main_menu_screen.initComponents();
        this.getContentPane().add(main_menu_screen, ID_MAIN_MENU_SCREEN);
        game_create_screen = new GameCreateScreen(SCREEN_SIZE, this);
        game_create_screen.initComponents();
        this.getContentPane().add(game_create_screen, ID_GAME_CREATE_SCREEN);
        game_screen = new GameScreen(SCREEN_SIZE, this);
        game_screen.initComponents();
        this.getContentPane().add(game_screen, ID_GAME_SCREEN);
        setCurrentScreen(ID_MAIN_MENU_SCREEN);

        CharPainter.init(TILE_SIZE);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isDebugMode() {
        return isDebugMode;
    }

    public void start() {
        isRunning = true;
        new Thread(new Animator(), "animation thread").start();
        new Thread(new Updater(), "update thread").start();
        //current_screen.repaint();
        loadResources();
    }

    public void stop() {
        isRunning = false;
    }

    private void loadResources() {
        new Thread("resource loading thread") {
            @Override
            public void run() {
                try {
                    File tile_data_dir = new File("data/tiles");
                    File unit_data_dir = new File("data/units");
                    TileRepository.init(tile_data_dir);
                    UnitFactory.init(unit_data_dir);
                    ResourceManager.init(getTileSize());
                } catch (IOException | AEIIException ex) {
                    DialogUtil.showError(
                            Launcher.getWindow(),
                            ex.getClass().toString() + ": " + ex.getMessage());
                    Launcher.exit();
                }
            }
        }.start();
    }

    public Container getContentPane() {
        return content_pane;
    }

    public void setCurrentScreen(String screen_id) {
        screen_container.show(getContentPane(), screen_id);
        switch (screen_id) {
            case ID_MAIN_MENU_SCREEN:
                current_screen = main_menu_screen;
                break;
            case ID_GAME_CREATE_SCREEN:
                current_screen = game_create_screen;
                break;
            case ID_GAME_SCREEN:
                current_screen = game_screen;
                break;
            default:
                break;
        }
        current_screen.revalidate();
    }

    public void gotoMainMenuScreen() {
        setCurrentScreen(ID_MAIN_MENU_SCREEN);
        main_menu_screen.getMenu().showMenu(MainMenu.ID_WELCOME_MENU);
    }

    public void gotoGameCreateScreen(GameCreator creator, MapProvider provider) {
        setCurrentScreen(ID_GAME_CREATE_SCREEN);
        game_create_screen.setGameCreator(creator);
        game_create_screen.setMapProvider(provider);
        game_create_screen.reloadMaps();
    }

    public void gotoGameScreen(Game game, int mode) {
        switch (mode) {
            case MODE_LOCAL:
                LocalGameManager manager
                        = new LocalGameManager(game, game_screen.getAnimationProvicer());
                game_screen.setGameManager(manager);
                setCurrentScreen(ID_GAME_SCREEN);
                break;
            case MODE_NET:
                break;
            default:
            //do nothing;
        }
    }

    public Screen getCurrentScreen() {
        return current_screen;
    }

    public LocalMapProvider getLocalMapProvider() {
        return local_map_provider;
    }

    public int getTileSize() {
        return TILE_SIZE;
    }

    public void setCurrentFpsDelay(long delay) {
        synchronized (FPS_LOCK) {
            fpsDelay = delay;
        }
    }

    public long getCurrentFpsDelay() {
        synchronized (FPS_LOCK) {
            return fpsDelay;
        }
    }

    public Object getUpdateLock() {
        return content_pane.getTreeLock();
    }

    private void updateCurrentScreen() {
        synchronized (getUpdateLock()) {
            getCurrentScreen().update();
        }
    }

    private class Animator implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                getCurrentScreen().repaint();
            }
        }

    }

    private class Updater implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                long start_time = System.currentTimeMillis();
                //System.out.println("update");
                updateCurrentScreen();
                long end_time = System.currentTimeMillis();
                long current_fps_delay = getCurrentFpsDelay();
                if (end_time - start_time < current_fps_delay) {
                    waitDelay(current_fps_delay - (end_time - start_time));
                } else {
                    waitDelay(0);
                }
            }
        }

        private void waitDelay(long time) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
            }
        }

    }

}
