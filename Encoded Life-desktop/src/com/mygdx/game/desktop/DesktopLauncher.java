package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.EncodedLife;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		  config.title = "Encoded Life";
	      config.width = 800;
	      config.height = 480;
		new LwjglApplication(new EncodedLife(), config);
	}
}
