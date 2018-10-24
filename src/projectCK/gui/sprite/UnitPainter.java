package projectCK.gui.sprite;

import java.awt.Graphics;

import projectCK.core.unit.Buff;
import projectCK.core.unit.Unit;
import projectCK.gui.ResourceManager;
import projectCK.gui.util.CharPainter;

public class UnitPainter {

    private UnitPainter() {
    }

    public static void paint(Graphics g, Unit unit, int x, int y, int ts) {
        int team = unit.getTeam();
        int index = unit.getIndex();
        if (unit.isStandby()) {
            g.drawImage(ResourceManager.getStandbyUnitImage(team, index),
                    x, y, ts, ts, null);
        } else {
            g.drawImage(ResourceManager.getUnitImage(team, index),
                    x, y, ts, ts, null);
        }
        if (unit.getCurrentHp() < unit.getMaxHp()) {
            int hp_dx = 0;
            int hp_dy = ts - CharPainter.getCharHeight();
            CharPainter.paintNumber(g, unit.getCurrentHp(), x + hp_dx, y + hp_dy);
        }
        int sw = ts / 24 * 7;
        int buff_count = unit.getBuffCount();
        for (int i = 0; i < buff_count; i++) {
            Buff buff = unit.getBuff(i);
            switch (buff.getType()) {
                case Buff.POISONED:
                    g.drawImage(ResourceManager.getPoisonedStatusImage(), x + i * sw, y, null);
                    break;
                default:
                //do nothing
            }
        }
    }

}
