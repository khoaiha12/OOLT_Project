package projectCK.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import projectCK.core.SuffixFileFilter;
import projectCK.core.map.TileRepository;
import projectCK.core.unit.UnitFactory;
import projectCK.gui.util.ResourceUtil;

public class ResourceManager {

    private static final SuffixFileFilter png_file_filter = new SuffixFileFilter("png");

    private static BufferedImage img_background;
    private static BufferedImage gold_icon;
    private static BufferedImage attack_icon;
    private static BufferedImage red_defence_icon;
    private static BufferedImage blue_defence_icon;
    private static BufferedImage level_icon;
    private static BufferedImage population_icon;
    private static BufferedImage poisoned_status;
    private static BufferedImage[] action_icons;
    private static BufferedImage[] borders;
    private static BufferedImage[] tiles;
    private static BufferedImage[] top_tiles;
    private static BufferedImage tomb;
    private static BufferedImage[][] units;
    private static BufferedImage[][] standby_units;
    private static BufferedImage move_target;
    private static BufferedImage move_alpha;
    private static BufferedImage attack_alpha;
    private static BufferedImage[] numbers;
    private static BufferedImage[] lnumbers;
    private static BufferedImage minus;
    private static BufferedImage lplus;
    private static BufferedImage lminus;
    private static BufferedImage ldivision;
    private static BufferedImage rise_arrow;
    private static BufferedImage reduce_arrow;
    private static Color aeii_panel_bg;
    private static Color text_background;
    private static Color move_path_color;
    private static Color p_attack_color;
    private static Color m_attack_color;
    private static Color[] team_color;
    private static Font title_font;
    private static Font label_font;
    private static Font text_font;

    private ResourceManager() {
    }

    public static void init(int ts) throws IOException {
        img_background = ImageIO.read(new File("res/img/grassbg1.gif"));
        loadBorder();
        loadTiles(ts);
        loadTopTiles(ts);
        loadCursors(ts);
        loadUnits(ts);
        loadAlpha(ts);
        loadChars(ts);
        loadActionIcons(ts);
        loadIcons(ts);
        loadStatus(ts);
        loadArrows(ts);
        aeii_panel_bg = new Color(36, 42, 69);
        text_background = new Color(206, 206, 206);
        move_path_color = new Color(225, 0, 82);
        p_attack_color = new Color(227, 0, 117);
        m_attack_color = new Color(0, 0, 255);
        team_color = new Color[4];
        team_color[0] = new Color(0, 100, 198);
        team_color[1] = new Color(161, 0, 112);
        team_color[2] = new Color(0, 153, 55);
        team_color[3] = new Color(0, 65, 114);
        title_font = new Font(Font.DIALOG, Font.BOLD, ts / 2);
        label_font = new Font(Font.DIALOG, Font.BOLD, ts / 3);
        text_font = new Font(Font.DIALOG, Font.PLAIN, ts / 4);
    }

    private static void loadBorder() throws IOException {
        BufferedImage img_borders = ImageIO.read(new File("res/img/border.png"));
        borders = new BufferedImage[8];
        for (int i = 0; i < borders.length; i++) {
            BufferedImage border
                    = ResourceUtil.getImageClip(img_borders, 16 * i, 0, 16, 16);
            borders[i] = border;
        }
    }

    private static void loadTiles(int ts) throws IOException {
        int tile_count = TileRepository.getTileCount();
        System.out.println(tile_count);
        tiles = new BufferedImage[tile_count];
        for (int i = 0; i < tile_count; i++) {
            File tile = new File("res/img/tiles/tile_" + i + ".png");

            tiles[i] = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
            tiles[i].getGraphics().drawImage(ImageIO.read(tile), 0, 0, ts, ts, null);
        }
        File tomb_file = new File("res/img/tombstone.png");
        tomb = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
        tomb.getGraphics().drawImage(ImageIO.read(tomb_file), 0, 0, ts, ts, null);
    }

    private static void loadTopTiles(int ts) throws IOException {
        File img_top_tile_dir = new File("res/img/tiles/top_tiles");
        int top_tile_count = img_top_tile_dir.listFiles(png_file_filter).length;
        top_tiles = new BufferedImage[top_tile_count];
        for (int i = 0; i < top_tile_count; i++) {
            File tile = new File("res/img/tiles/top_tiles/top_tile_" + i + ".png");
            top_tiles[i] = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
            top_tiles[i].getGraphics().drawImage(ImageIO.read(tile), 0, 0, ts, ts, null);
        }
    }

    private static void loadUnits(int ts) throws IOException {
        int unit_count = UnitFactory.getUnitCount();
        units = new BufferedImage[2][unit_count];
        standby_units = new BufferedImage[2][unit_count];
        for (int team = 0; team < 2; team++) {
            for (int index = 0; index < unit_count; index++) {
            	File unit_team = new File("res/img/units/unit_icons_" + team + "_" + index + ".png");
            	units[team][index] = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
                units[team][index].getGraphics().drawImage(
                		ImageIO.read(unit_team),
                        0, 0,
                        ts, ts,
                        null);
                standby_units[team][index] = ResourceUtil.getGrayScaledImage(units[team][index]);
            }
        }
    }

    private static void loadCursors(int ts) throws IOException {
        ts = ts / 24 * 26;
        File move_target_file = new File("res/img/move_target_cursor.png");
        move_target = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
        move_target.getGraphics().drawImage(ImageIO.read(move_target_file), 0, 0, ts, ts, null);
    }

    private static void loadAlpha(int ts) throws IOException {
        File alpha_file = new File("res/img/alpha.png");
        BufferedImage img_alpha = new BufferedImage(ts * 2, ts, BufferedImage.TYPE_INT_ARGB);
        img_alpha.getGraphics().drawImage(ImageIO.read(alpha_file), 0, 0, ts * 2, ts, null);
        move_alpha = ResourceUtil.getImageClip(img_alpha, ts, 0, ts, ts);
        attack_alpha = ResourceUtil.getImageClip(img_alpha, 0, 0, ts, ts);
    }

    private static void loadChars(int ts) throws IOException {
        int w = ts / 24 * 6;
        int h = ts / 24 * 7;
        int lw = ts / 24 * 8;
        int lh = ts / 24 * 11;
        File chars_file = new File("res/img/chars.png");
        BufferedImage img_chars = new BufferedImage(w * 12, h, BufferedImage.TYPE_INT_ARGB);
        img_chars.getGraphics().drawImage(ImageIO.read(chars_file), 0, 0, w * 12, h, null);
        File lchars_file = new File("res/img/lchars.png");
        BufferedImage img_lchars = new BufferedImage(lw * 13, lh, BufferedImage.TYPE_INT_ARGB);
        img_lchars.getGraphics().drawImage(ImageIO.read(lchars_file), 0, 0, lw * 13, lh, null);
        numbers = new BufferedImage[10];
        lnumbers = new BufferedImage[10];
        for (int i = 0; i < 10; i++) {
            numbers[i] = ResourceUtil.getImageClip(img_chars, i * w, 0, w, h);
            lnumbers[i] = ResourceUtil.getImageClip(img_lchars, i * lw, 0, lw, lh);
        }
        minus = ResourceUtil.getImageClip(img_chars, 10 * w, 0, w, h);
        lminus = ResourceUtil.getImageClip(img_lchars, 11 * lw, 0, lw, lh);
        lplus = ResourceUtil.getImageClip(img_lchars, 12 * lw, 0, lw, lh);
        ldivision = ResourceUtil.getImageClip(img_lchars, 10 * lw, 0, lw, lh);
    }

    private static void loadActionIcons(int ts) throws IOException {
        int iw = ts / 24 * 16;
        int ih = ts / 24 * 16;
        File icons_file = new File("res/img/action_icons.png");
        BufferedImage action_icon_image = new BufferedImage(iw * 8, ih, BufferedImage.TYPE_INT_ARGB);
        action_icon_image.getGraphics().drawImage(ImageIO.read(icons_file), 0, 0, iw * 8, ih, null);
        action_icons = new BufferedImage[8];
        for (int i = 0; i < 8; i++) {
            action_icons[i] = ResourceUtil.getImageClip(action_icon_image, iw * i, 0, iw, ih);
        }
    }

    private static void loadIcons(int ts) throws IOException {
        int hw = ts / 24 * 13;
        int hh = ts / 24 * 16;
        File hud_icon_file = new File("res/img/hud_icons.png");
        BufferedImage img_hud_icon = new BufferedImage(hw * 4, hh, BufferedImage.TYPE_INT_ARGB);
        img_hud_icon.getGraphics().drawImage(ImageIO.read(hud_icon_file), 0, 0, hw * 4, hh, null);
        attack_icon = ResourceUtil.getImageClip(img_hud_icon, 0, 0, hw, hh);
        red_defence_icon = ResourceUtil.getImageClip(img_hud_icon, hw, 0, hw, hh);
        blue_defence_icon = ResourceUtil.getImageClip(img_hud_icon, hw * 2, 0, hw, hh);
        level_icon = ResourceUtil.getImageClip(img_hud_icon, hw * 3, 0, hw, hh);
        int i2w = ts / 24 * 11;
        int i2h = ts / 24 * 11;
        File hud_icon2_file = new File("res/img/hud_icons_2.png");
        BufferedImage img_hud_icon2 = new BufferedImage(i2w * 2, i2h, BufferedImage.TYPE_INT_ARGB);
        img_hud_icon2.getGraphics().drawImage(ImageIO.read(hud_icon2_file), 0, 0, i2w * 2, i2h, null);
        gold_icon = ResourceUtil.getImageClip(img_hud_icon2, i2w, 0, i2w, i2h);
        population_icon = ResourceUtil.getImageClip(img_hud_icon2, 0, 0, i2w, i2h);
    }

    private static void loadStatus(int ts) throws IOException {
        int sw = ts / 24 * 7;
        int sh = ts / 24 * 9;
        File status_file = new File("res/img/status.png");
        BufferedImage status_list = new BufferedImage(sw * 2, sh, BufferedImage.TYPE_INT_ARGB);
        status_list.getGraphics().drawImage(ImageIO.read(status_file), 0, 0, sw * 2, sh, null);
        poisoned_status = ResourceUtil.getImageClip(status_list, 0, 0, sw, sh);
    }

    private static void loadArrows(int ts) throws IOException {
        int aw = ts / 24 * 9;
        int ah = ts / 24 * 7;
        File arrow_file = new File("res/img/arrow_icons.png");
        BufferedImage img_arrows = new BufferedImage(aw * 3, ah, BufferedImage.TYPE_INT_ARGB);
        img_arrows.getGraphics().drawImage(ImageIO.read(arrow_file), 0, 0, aw * 3, ah, null);
        rise_arrow = ResourceUtil.getImageClip(img_arrows, aw, 0, aw, ah);
        reduce_arrow = ResourceUtil.getImageClip(img_arrows, aw * 2, 0, aw, ah);
    }
    
    public static BufferedImage getBackgroundImage() {
    	return img_background;
    }

    public static BufferedImage getBorderImage(int index) {
        return borders[index];
    }

    public static BufferedImage getAttackIcon() {
        return attack_icon;
    }

    public static BufferedImage getRedDefenceIcon() {
        return red_defence_icon;
    }

    public static BufferedImage getBlueDefenceIcon() {
        return blue_defence_icon;
    }

    public static BufferedImage getLevelIcon() {
        return level_icon;
    }

    public static BufferedImage getPopulationIcon() {
        return population_icon;
    }

    public static BufferedImage getGoldIcon() {
        return gold_icon;
    }

    public static BufferedImage getTileImage(int index) {
        return tiles[index];
    }

    public static BufferedImage getTopTileImage(int index) {
        return top_tiles[index];
    }

    public static BufferedImage getTombImage() {
        return tomb;
    }

    public static BufferedImage getUnitImage(int team, int index) {
        return units[team][index];
    }

    public static BufferedImage getStandbyUnitImage(int team, int index) {
        return standby_units[team][index];
    }
    
    public static BufferedImage getMoveTargetCursorImage() {
        return move_target;
    }

    public static BufferedImage getAttackAlpha() {
        return attack_alpha;
    }

    public static BufferedImage getMoveAlpha() {
        return move_alpha;
    }

    public static BufferedImage getNumber(int n) {
        return numbers[n];
    }

    public static BufferedImage getLNumber(int n) {
        return lnumbers[n];
    }

    public static BufferedImage getMinus() {
        return minus;
    }

    public static BufferedImage getLPlus() {
        return lplus;
    }

    public static BufferedImage getLMinus() {
        return lminus;
    }

    public static BufferedImage getLDivision() {
        return ldivision;
    }

    public static BufferedImage getPoisonedStatusImage() {
        return poisoned_status;
    }

    public static BufferedImage getActionIcon(int index) {
        return action_icons[index];
    }

    public static BufferedImage getRiseArrow() {
        return rise_arrow;
    }

    public static BufferedImage getReduceArrow() {
        return reduce_arrow;
    }

    public static Color getPhysicalAttackColor() {
        return p_attack_color;
    }

    public static Color getMagicalAttackColor() {
        return m_attack_color;
    }

    public static Color getTeamColor(int team) {
        return team_color[team];
    }

    public static Color getAEIIPanelBg() {
        return aeii_panel_bg;
    }

    public static Color getTextBackgroundColor() {
        return text_background;
    }

    public static Color getMovePathColor() {
        return move_path_color;
    }

    public static Font getTitleFont() {
        return title_font;
    }

    public static Font getLabelFont() {
        return label_font;
    }

    public static Font getTextFont() {
        return text_font;
    }

}
