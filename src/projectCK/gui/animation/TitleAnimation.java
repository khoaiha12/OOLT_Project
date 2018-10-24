
package projectCK.gui.animation;

import projectCK.core.animation.Animation;

public class TitleAnimation extends Animation {
	
	private int logoDrapValue = 0;
	
	@Override
	protected void doUpdate() {
		if(logoDrapValue < 40) {
			logoDrapValue++;
		} else {
			complete();
		}
	}
	
}
