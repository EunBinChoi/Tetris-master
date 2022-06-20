
/*
============================================================================
Name        : TetirsBlockShape.java
Author      : Eunbin Choi
Version     :
Copyright   : 
Description : TetrisBlockShape
============================================================================
 */
import javafx.scene.paint.Color;

public class TetrisBlockShape {
	private Color color;
	private byte[][][] block;
	private int formIndex = 0;

	public TetrisBlockShape(byte[][][] block, Color color) {
		this.block = block;
		this.color = color;
	}

	public void rotate() {
		formIndex = (formIndex + 1) % block.length;
	}

	public byte[][] getCurrentBlock() {
		return block[formIndex];
	}

	public byte[][] getNextBlock() {
		int nextIndex = (formIndex + 1) % block.length;
		return block[nextIndex];
	}

	public Color getColor() {
		return color;
	}
}
