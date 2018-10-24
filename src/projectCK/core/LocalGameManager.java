package projectCK.core;

import java.util.LinkedList;
import java.util.Queue;

import projectCK.core.animation.Animation;
import projectCK.core.animation.AnimationDispatcher;
import projectCK.core.animation.AnimationListener;
import projectCK.core.animation.AnimationProvider;
import projectCK.core.unit.Unit;

public class LocalGameManager extends GameManager implements AnimationDispatcher {

    private final AnimationProvider animation_provider;

    private final Queue<Animation> animation_dispatcher;
    private Animation current_animation;

    public LocalGameManager(Game game, AnimationProvider provider) {
        super(game);
        this.animation_provider = provider;
        this.animation_dispatcher = new LinkedList<Animation>();
        current_animation = null;
    }

    @Override
    public void updateAnimation() {
        if (current_animation != null) {
            current_animation.update();
        }
    }

    @Override
    public void submitAnimation(Animation animation) {
        animation.addAnimationListener(new AnimationListener() {
            @Override
            public void animationCompleted(Animation animation) {
                current_animation = animation_dispatcher.poll();
            }
        });
        if (current_animation == null) {
            current_animation = animation;
        } else {
            animation_dispatcher.add(animation);
        }
    }

    @Override
    public Animation getCurrentAnimation() {
        return current_animation;
    }

    @Override
    public boolean isAnimating() {
        return current_animation != null;
    }

    public void reverseMove() {
        Unit unit = getSelectedUnit();
        if (getUnitToolkit().isUnitAccessible(unit) && canReverseMove() && getState() == STATE_ACTION) {
            int last_x = last_position.x;
            int last_y = last_position.y;
            getGame().getMap().moveUnit(unit, last_x, last_y);
            unit.setCurrentMovementPoint(unit.getMovementPoint());
            beginMovePhase();
        }
    }

    public boolean canReverseMove() {
        return is_selected_unit_moved;
    }

    @Override
    public void onOccupy() {
        this.submitOccupyAnimation();
    }

    @Override
    public void onRepair() {
        this.submitRepairAnimation();
    }

    @Override
    public void onUnitMove(Unit unit, int start_x, int start_y, int dest_x, int dest_y) {
        this.submitUnitMoveAnimation(unit, start_x, start_y, dest_x, dest_y);
    }

    @Override
    public void onTurnStart(int turn, int income, int team) {
        this.submitTurnStartAnimation(turn, income, team);
    }
    
    @Override
    public void onMapFocused(int map_x, int map_y) {
        listener.onMapFocused(map_x, map_y);
    }

    @Override
    public void onGameEventCleared() {
        listener.onGameEventCleared();
    }

    @Override
    public void submitOccupyAnimation() {
        Animation msg_animation = animation_provider.getOccupiedMessageAnimation();
        submitAnimation(msg_animation);
    }

    @Override
    public void submitRepairAnimation() {
        Animation msg_animation = animation_provider.getRepairedMessageAnimation();
        submitAnimation(msg_animation);
    }

    @Override
    public void submitUnitMoveAnimation(Unit unit, int start_x, int start_y, int dest_x, int dest_y) {
        Animation animation = animation_provider.getUnitMoveAnimation(unit, start_x, start_y, dest_x, dest_y);
        submitAnimation(animation);
    }

    @Override
    public void submitTurnStartAnimation(int turn, int income, int team) {
        income = getGame().isLocalPlayer() ? income : -1;
        Animation animation = animation_provider.getTurnStartAnimation(turn, income, team);
        submitAnimation(animation);
    }

    @Override
    public void submitGameOverAnimation(int alliance) {
        Animation animation = animation_provider.getGameOverAnimation(alliance);
        submitAnimation(animation);
    }

    public void update() {
        updateAnimation();
        if (!isAnimating()) {
            getGame().dispatchGameEvent();
        }
    }

    @Override
    public boolean isProcessing() {
        return super.isProcessing() || isAnimating();
    }

}
