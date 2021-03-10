package it.tristana.keystroke.config;

import it.tristana.keystroke.keys.Key;
import it.tristana.keystroke.keys.KeyJump;
import it.tristana.keystroke.keys.KeyMouse;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;

/**
 * This class builds and holds the keys. It has a singleton pattern.<br>
 * Its instance may be accessed with {@link #getInstance()}
 * @author Massimiliano Micol
 */

public final class Config {
	
	/**
	 * The gui was designed for a 1920x1080 screen, so if the size<br>
	 * or proportions are different they are scaled by this factor
	 */
	
	private static final double SCREEN_1920_1080_RATIO = 1.777;
	
	/**
	 * The singleton instance
	 */
	
	private static Config instance;
	
	/**
	 * An array of all the declared keys
	 */
	
	private final Key[] elements;

	/**
	 * A private constructor for the singleton
	 */
	
	private Config() {
		// The base coordinates of the keys, expressed in percentage
		final double baseX = 75;
		final double baseY = 25;
		final double width = 5;
		final double height = width * SCREEN_1920_1080_RATIO;
		// No need for this warning, the Closeable instance is closed by the Minecraft code
		@SuppressWarnings("resource")
		GameSettings settings = Minecraft.getInstance().gameSettings;
		elements = new Key[] {
			new Key(baseX, baseY, width, height, settings.keyBindForward, null),
			new Key(baseX, baseY + height, width, height, settings.keyBindBack, null),
			new Key(baseX - width, baseY + height, width, height, settings.keyBindLeft, null),
			new Key(baseX + width, baseY + height, width, height, settings.keyBindRight, null),
			new KeyMouse(baseX - width, baseY + height * 2, width * 1.5, height, settings.keyBindAttack, "LMB"),
			new KeyMouse(baseX + width * 0.5, baseY + height * 2, width * 1.5, height, settings.keyBindUseItem, "RMB"),
			new KeyJump(baseX - width, baseY + height * 3, width * 3, height / 2, settings.keyBindJump, "JUMP")
		};
	}
	
	/**
	 * Gets this class singleton, instantiating it if needed
	 * @return The singleton instance
	 */
	
	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}
	
	/**
	 * Gets the array of the declared keys
	 * @return The same array reference
	 */
	
	public Key[] getKeys() {
		return elements;
	}
	
	/**
	 * Called from the {@link it.tristana.keystroke.Main Main} class every time<br>
	 * a keyboard or mouse key is pressed
	 * @param key
	 * @param pressed
	 */
	
	public void onKey(final int key, final boolean pressed) {
		// Searches for a Key with this value
		Key element = getElementByKey(key);
		// If it is found then it updates the key status
		if (element != null) {
			element.setKeyDown(pressed);
		}
	}
	
	/**
	 * Searches and returns the first key whose {@link Key#getKey()}<br>
	 * value is the same as the input parameter
	 * @param key The keyboard integer value, as specified by <a href="https://www.glfw.org/docs/latest/group__keys.html">this link</a>
	 * @return The first key with this integer value, or {@code null} if none are found
	 */
	
	private Key getElementByKey(final int key) {
		Key result = null;
		for (Key test : elements) {
			// Does this key have the required value?
			if (test.getKey() == key) {
				result = test;
				// We already found a key, we can exit now
				break;
			}
		}
		return result;
	}
}
