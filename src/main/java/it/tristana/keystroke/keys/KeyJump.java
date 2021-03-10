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
