package it.tristana.keystroke.keys;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.settings.KeyBinding;

/**
 * A class to represent the jump button as a filled line instead of text
 * @author Massimiliano Micol
 * @see Key
 */

public final class KeyJump extends Key {
	
	/**
	 * @see Key#Key(double, double, double, double, KeyBinding, String)
	 */
	
	public KeyJump(final double percentX, final double percentY, final double percentWidth, final double percentHeight, final KeyBinding key, final String keyName) {
		super(percentX, percentY, percentWidth, percentHeight, key, keyName);
	}

	/**
	 * Ignores the button name and draws a filled centered rectangle with the text's color
	 */
	
	@Override
	protected void drawKeyText(final MatrixStack matrix, final float textRatio, final int elementX, final int elementY, final int elementWidth, final int elementHeight) {
		final int offsetX = elementWidth / 4;
		final int offsetY = elementHeight * 3 / 8;
		// Draws a centered rectangle inside the key's background
		AbstractGui.fill(matrix, elementX + offsetX, elementY + offsetY, elementX + elementWidth - offsetX, elementY + elementHeight - offsetY, TEXT_COLOR);
	}
}
