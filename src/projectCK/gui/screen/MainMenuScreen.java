package projectCK.gui.screen;

import java.awt.Dimension;
import java.awt.Graphics;

import projectCK.core.animation.Animation;
import projectCK.core.animation.AnimationListener;
import projectCK.gui.AEIIApplet;
import projectCK.gui.ResourceManager;
import projectCK.gui.Screen;
import projectCK.gui.animation.TitleAnimation;
import projectCK.gui.screen.internal.MainMenu;

public class MainMenuScreen extends Screen {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final TitleAnimation title_merge_animation;
	
	private final MainMenu menu;
	private boolean title_shown = false;

	public MainMenuScreen(Dimension size, AEIIApplet context) {
		super(size, context);
		title_merge_animation = new TitleAnimation();
		title_merge_animation.setInterval(1);
		title_merge_animation.addAnimationListener(new AnimationListener() {
			@Override
			public void animationCompleted(Animation animation) {
				showMenu();
			}
		});
		menu = new MainMenu(context);
	}
	
	@Override
	public void initComponents() {
		this.setLayout(null);
		this.menu.initComponents(ts, this);
		this.menu.setVisible(false);
		this.add(menu);
	}
	
	private void showMenu() {
		title_shown = true;
		this.menu.setVisible(true);
	}
	
	public MainMenu getMenu() {
		return menu;
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(ResourceManager.getBackgroundImage(), getWidth(), getHeight(), null);
		g.drawImage(ResourceManager.getBackgroundImage(), 0, 0, this.getWidth(), this.getHeight(),this);
//			title_glow_animation.paint(g);
		super.paint(g);
	}

	@Override
	public void update() {
		if(title_shown) {
		} else {
			title_merge_animation.update();
		}
	}

}
