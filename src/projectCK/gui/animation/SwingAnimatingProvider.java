package projectCK.gui.animation;

import java.util.ArrayList;
import projectCK.Language;
import projectCK.core.GameManager;
import projectCK.core.Point;
import projectCK.core.animation.Animation;
import projectCK.core.animation.AnimationListener;
import projectCK.core.animation.AnimationProvider;
import projectCK.core.unit.Unit;
import projectCK.gui.screen.GameScreen;

public class SwingAnimatingProvider implements AnimationProvider {

    private final int ts;
    private GameManager manager;
    private final GameScreen game_screen;

    public SwingAnimatingProvider(GameScreen screen, int ts) {
        this.ts = ts;
        this.game_screen = screen;
    }

    public void setGameManager(GameManager manager) {
        this.manager = manager;
    }

    @Override
    public Animation getOccupiedMessageAnimation() {
        MessageAnimation animation = new MessageAnimation(Language.getText("LB_MSG_OCCUPIED"), 15, ts);
        processAnimation(animation);
        return animation;
    }

    @Override
    public Animation getRepairedMessageAnimation() {
        MessageAnimation animation = new MessageAnimation(Language.getText("LB_MSG_REPAIRED"), 15, ts);
        processAnimation(animation);
        return animation;
    }

    @Override
    public Animation getUnitMoveAnimation(Unit unit, int start_x, int start_y, int dest_x, int dest_y) {
        ArrayList<Point> path = manager.getUnitToolkit().createMovePath(unit, start_x, start_y, dest_x, dest_y);
        UnitMoveAnimation animation = new UnitMoveAnimation(unit, path, ts);
        processAnimation(animation);
        return animation;
    }

    @Override
    public Animation getTurnStartAnimation(int turn, int income, int team) {
        NewTurnAnimation animation = new NewTurnAnimation(turn, income, team, ts);
        processAnimation(animation);
        return animation;
    }

    @Override
    public Animation getGameOverAnimation(int allience) {
        String message = Language.getText("LB_MSG_GAME_OVER").replaceAll("%team", "" + (allience + 1));
        MessageAnimation animation = new MessageAnimation(message, 30, ts);
        processAnimation(animation);
        return animation;
    }

    private Animation processAnimation(Animation animation) {
        animation.addAnimationListener(new AnimationListener() {
            @Override
            public void animationCompleted(Animation animation) {
                if (manager.getGame().isLocalPlayer()) {
                    game_screen.getActionPanel().update();
                }
            }
        });
        return animation;
    }

}
