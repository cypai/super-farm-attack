package com.pipai.sfa.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pipai.sfa.SfaGame;

public final class DesktopLauncher {

	private DesktopLauncher() {
	}

	// SUPPRESS CHECKSTYLE UncommentedMain This is the main entry point
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 768;
		new LwjglApplication(new SfaGame(), config);
	}
}
