package it.tristana.keystroke.keys;

/**
 * A simple POJO object to hold the values of a rectangle. Since those values are final, they are publicly accessible
 * @author Massimiliano Micol
 */

final class Rectangle {

	public final double x;
	public final double y;
	public final double width;
	public final double height;
	
	Rectangle(final double x, final double y, final double width, final double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
