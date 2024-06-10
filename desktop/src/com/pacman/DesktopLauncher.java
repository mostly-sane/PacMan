package com.pacman;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.pacman.PacMan;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTransparentFramebuffer(false);
		config.setForegroundFPS(120);
		config.setTitle("PacMan");
		config.setWindowedMode(475, 550);
		new Lwjgl3Application(new PacMan(), config);
	}
}