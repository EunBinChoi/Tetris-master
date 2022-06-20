
/*
============================================================================
Name        : TetirsGridCell.java
Author      : Eunbin Choi
Version     :
Copyright   : 
Description : TetrisGridCell
============================================================================
 */
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TetrisGridCell extends Rectangle {
	private Color color;

	public TetrisGridCell(int x, int y) {
		super(x, y, 20, 20);
		color = Color.DARKGRAY;
		setStroke(Color.GRAY);
		setFill(color);
	}

	public void setColor(Color color2) {
		this.color = color2;
	}

	public Color getColor() {
		return color;
	}

	public boolean isEmpty() {
		return color == Color.DARKGRAY;
	}

	public void draw() {
		setStroke(Color.GRAY);
		setFill(color);
	}
}
