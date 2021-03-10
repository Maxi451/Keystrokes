package it.tristana.keystroke.keys;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;

/**
 * The base class to represent a key. It has a position, bounds and an associated<br>
 * {@code net.minecraft.client.settings.KeyBinding}. All the keys have the<br>
 * same pressed and unpressed colors, as well as the same text color
 * @author Massimiliano Micol
 */

public class Key {

	/**
	 * The base scale applied to the drawn text to increase its font size
	 */
	
	public static final float TEXT_BASE_SCALE = 4f;
	
	/**
	 * The keys width was designed for this resolution, so<br>
	 * for other resolutions it gets scaled using this value
	 */
	
	public static final float BASE_SCREEN_WIDTH = 1920f;
	
	/**
	 * The keys height was designed for this resolution, so<br>
	 * for other resolutions it gets scaled using this value
	 */
	
	public static final float BASE_SCREEN_HEIGHT = 1080f;

	/**
	 * A semi-transparent light gray color for an unpressed key
	 */
	
	public static final int BUTTON_UP_COLOR = toARGB(0x10, 0xa0, 0xa0, 0xa0);
	
	/**
	 * A semi-transparent darker gray color for a pressed key
	 */
	
	public static final int BUTTON_DOWN_COLOR = toARGB(0x10, 0x50, 0x50, 0x50);
	
	/**
	 * A completely opaque white color for the keys' text
	 */
	
	public static final int TEXT_COLOR = toARGB(0xff, 0xff, 0xff, 0xff);
	
	/**
	 * The Minecraft font renderer instance
	 */
	
	public static final FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
	
	/**
	 * The bounds of this key, expressed as a percentage following these rules:
	 * <ul>
	 * 	<li>The X value refers to a percentage of the main window's X</li>
	 * 	<li>The Y value refers to a percentage of the main window's Y</li>
	 * 	<li>The width value refers to a percentage of the main window's width</li>
	 * 	<li>The height value refers to a percentage of the main window's height</li>
	 * </ul>
	 */
	
	private final Rectangle bounds;
	
	/**
	 * The KeyBinding associated to this object
	 */
	
	private final KeyBinding key;
	
	/**
	 * The name used to render this key. May be null
	 */
	
	private final String keyName;
	
	/**
	 * Holds the keybinding status
	 */
	
	private boolean isKeyDown;

	/**
	 * The class constructor
	 * @param percentX The X coordinate of this key, expressed as a percentage of the main window's X
	 * @param percentY The Y coordinate of this key, expressed as a percentage of the main window's Y
	 * @param percentWidth The width of this key, expressed as a percentage of the main window's width
	 * @param percentHeight The height of this key, expressed as a percentage of the main window's height
	 * @param key The associated KeyBinding
	 * @param keyName The name of this key, may be null
	 */
	
	public Key(final double percentX, final double percentY, final double percentWidth, final double percentHeight, final KeyBinding key, final String keyName) {
		bounds = new Rectangle(percentX, percentY, percentWidth, percentHeight);
		this.key = key;
		this.keyName = keyName;
	}
	
	/**
	 * Gets the integer value of the associated KeyBinding, as specified by <a href="https://www.glfw.org/docs/latest/group__keys.html">this link</a>
	 * @return The integer value of the associated KeyBinding
	 */
	
	public int getKey() {
		return key.getKey().getKeyCode();
	}
	
	/**
	 * Draws the key components on the screen, with the background color based on the key's status
	 * @param window The main window
	 * @param matrix The graphics matrix
	 */
	
	public void draw(final MainWindow window, final MatrixStack matrix) {
		int windowWidth = window.getScaledWidth();
		int windowHeight = window.getScaledHeight();
		// All the values are calculated based on the current window size and position
		int elementX = ceil(windowWidth * bounds.x / 100d);
		int elementY = ceil(windowHeight * bounds.y / 100d);
		int elementWidth = ceil(windowWidth * bounds.width / 100d);
		int elementHeight = ceil(windowHeight * bounds.height / 100d);
		// Draws the background rectangle, scaled by one pixel in all the sides
		AbstractGui.fill(matrix, elementX + 1, elementY + 1, elementX + elementWidth - 1, elementY + elementHeight - 1, isKeyDown() ? BUTTON_DOWN_COLOR : BUTTON_UP_COLOR);
		// Stores the current matrix status so we can do whatever we like on it
		matrix.push();
		// Draws the key name
		drawKeyText(matrix, Math.min(windowWidth / BASE_SCREEN_WIDTH, windowHeight / BASE_SCREEN_HEIGHT) * TEXT_BASE_SCALE, elementX, elementY, elementWidth, elementHeight);
		// Restores the previous matrix status undoing all the changes
		matrix.pop();
	}
	
	/**
	 * Draws the key name on the center of the background rectangle, scaled by a given factor
	 * @param matrix The graphics matrix
	 * @param textRatio The scaling factor
	 * @param elementX The X coordinate of the background rectangle
	 * @param elementY The Y coordinate of the background rectangle
	 * @param elementWidth The width of the background rectangle
	 * @param elementHeight The height of the background rectangle
	 */
	
	protected void drawKeyText(final MatrixStack matrix, final float textRatio, final int elementX, final int elementY, final int elementWidth, final int elementHeight) {
		matrix.scale(textRatio, textRatio, textRatio);
		// Draws the key name
		AbstractGui.drawCenteredString(matrix, fontRenderer, getKeyName(), ceil((elementX + elementWidth / 2d) / textRatio), ceil((elementY + elementHeight / 2d) / textRatio) - fontRenderer.FONT_HEIGHT / 2, TEXT_COLOR);
	}
	
	/**
	 * Gets the key name, following these rules:
	 * <ul>
	 * 	<li>If the value of the field {@link #keyName} is not null, then that value is returned</li>
	 * 	<li>Else, the method tries to determine the key name based on {@link GLFW#glfwGetKeyScancode(int)}</li>
	 * 	<li>If a name is found then it is returned</li>
	 * 	<li>If all the above fails, the string "???" is returned</li>
	 * </ul>
	 * @return The key name
	 */
	
	public String getKeyName() {
		String name;
		// There is not a fixed name
		if (keyName == null) {
			int code = getKey();
			// Tries to get the key name...
			name = GLFW.glfwGetKeyName(code, GLFW.glfwGetKeyScancode(code));
			// ...and put it upper case
			if (name != null) {
				name = name.toUpperCase();
			}
			else {
				// A name for that key was not found
				name = "???";
			}
		}
		else {
			// We already have the key name
			name = keyName;
		}
		return name;
	}
	
	/**
	 * Gets the bounds of this key, as specified by {@link #bounds}
	 * @return The bounds of this key
	 */
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	/**
	 * Gets the key status
	 * @return {@code True} if the key is pressed, {@code False} othewhise
	 */
	
	public boolean isKeyDown() {
		return isKeyDown;
	}
	
	/**
	 * Sets the key status
	 * @param isKeyDown A value representing the key status
	 */
	
	public void setKeyDown(final boolean isKeyDown) {
		this.isKeyDown = isKeyDown;
	}
	
	/**
	 * Determines the first integer value equal to or greater than the input
	 * @param num The number whose value has to be ceiled
	 * @return The ceiled value
	 */
	
	protected static int ceil(final double num) {
		return (int) (num == (int) num ? num : num + 1);
	}
	
	/**
	 * Receives the four components of a color and returns an integer whose value is<br>
	 * determined putting each component in a byte in big endian order: [a][r][g][b].<br>
	 * For each component only the least significant byte is used (values from 0 to 255)
	 * @param alpha The alpha component
	 * @param red The red component
	 * @param green The green component
	 * @param blue The blue component
	 * @return The integer as stated above
	 */
	
	protected static int toARGB(final int alpha, final int red, final int green, final int blue) {
		return ((alpha & 0xff) << 030) | ((red & 0xff) << 020) | ((green & 0xff) << 010) | (blue & 0xff);
	}
}
