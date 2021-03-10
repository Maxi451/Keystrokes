package it.tristana.keystroke.keys;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.settings.KeyBinding;

/**
 * An extension of the {@link Key} class to represent mouse keys.<br>
 * This class holds a CPS (Clicks Per Second) counter, displayed on screen
 * @author Massimiliano Micol
 */

public final class KeyMouse extends Key {

	/**
	 * A list to hold the clicks timestamps
	 */
	
	private final List<Long> clickTimestamps;
	
	/**
	 * @see Key#Key(double, double, double, double, KeyBinding, String)
	 */
	
	public KeyMouse(final double percentX, final double percentY, final double percentWidth, final double percentHeight, final KeyBinding key, final String keyName) {
		super(percentX, percentY, percentWidth, percentHeight, key, keyName);
		clickTimestamps = new LinkedList<Long>();
	}

	/**
	 * Draws the mouse button name as well as the current CPS count
	 */
	
	@Override
	protected void drawKeyText(final MatrixStack matrix, float textRatio, final int elementX, final int elementY, final int elementWidth, final int elementHeight) {
		// Since there are two lines in this key the text must be made smaller
		textRatio *= 0.75f;
		matrix.scale(textRatio, textRatio, textRatio);
		int x = ceil((elementX + elementWidth / 2d) / textRatio);
		int y = ceil((elementY + elementHeight / 2d) / textRatio);
		// The key name
		AbstractGui.drawCenteredString(matrix, fontRenderer, getKeyName(), x, y - fontRenderer.FONT_HEIGHT - 1, TEXT_COLOR);
		// The current CPS
		AbstractGui.drawCenteredString(matrix, fontRenderer, getCps() + " CPS", x, y + 1, TEXT_COLOR);
	}
	
	/**
	 * Updates the key status and adds the current timestamp to the list of key pressing times
	 */
	
	@Override
	public void setKeyDown(final boolean isKeyDown) {
		super.setKeyDown(isKeyDown);
		// Only add the timestamp if the key has been pressed
		if (isKeyDown) {
			clickTimestamps.add(System.currentTimeMillis());
		}
	}
	
	/**
	 * Removes all the timestamps whose value is lower than the current<br>
	 * current timestamp minus 1000 and returns the list size
	 * @return The number of CPS in the last second
	 */
	
	private int getCps() {
		long time = System.currentTimeMillis();
		Iterator<Long> iterator = clickTimestamps.iterator();
		while (iterator.hasNext()) {
			long next = iterator.next();
			// If this timestamp is older than a second...
			if (next + 1000 < time) {
				// ...then it gets removed
				iterator.remove();
			}
			else {
				// Since the list is ordered, we can exit now
				break;
			}
		}
		return clickTimestamps.size();
	}
}
