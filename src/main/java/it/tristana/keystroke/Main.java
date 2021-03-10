/*
MIT License

Copyright (c) 2021 Massimiliano Micol

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package it.tristana.keystroke;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.matrix.MatrixStack;

import it.tristana.keystroke.config.Config;
import it.tristana.keystroke.keys.Key;
import net.minecraft.client.MainWindow;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Main Keystrokes mod's class, it also receives the events
 * @author Massimiliano Micol
 */
@Mod("keystroke")
@Mod.EventBusSubscriber
public final class Main {
	
	/**
	 * The singleton instance of the configuration
	 */
	
	private Config settings;

	/**
	 * Entry point of the mod. Registering the setup event and this class itself as an events holder
	 */
	
	public Main() {
		// The setup event
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadConfig);
		MinecraftForge.EVENT_BUS.register(this);
	}

	/**
	 * Called once forge starts to setup the mods
	 * @param event The setup event
	 */
	
	@SubscribeEvent
	public void loadConfig(final FMLClientSetupEvent event) {
		settings = Config.getInstance();
	}
	
	/**
	 * Called when the game finished to render the main window, without any opened gui
	 * @param event The rendering event
	 */
	
	@SubscribeEvent
	public void onRender(final RenderGameOverlayEvent.Post event) {
		MainWindow window = event.getWindow();
		MatrixStack matrix = event.getMatrixStack();
		// Draws all the keys on screen
		for (Key key : settings.getKeys()) {
			key.draw(window, matrix);
		}
	}
	
	/**
	 * Called when a keyboard's key is pressed, is hold or is released
	 * @param event The keyboard's event
	 */
	
	@SubscribeEvent
	public void keyPressed(final InputEvent.KeyInputEvent event) {
		settings.onKey(event.getKey(), event.getAction() != GLFW.GLFW_RELEASE);
	}
	
	/**
	 * Called when a mouse's key is pressed or released
	 * @param event The mouse's event
	 */
	
	@SubscribeEvent
	public void keyPressed(final InputEvent.MouseInputEvent event) {
		settings.onKey(event.getButton(), event.getAction() != GLFW.GLFW_RELEASE);
	}
}
