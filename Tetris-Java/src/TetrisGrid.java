/*
============================================================================
Name        : TetirsGrid.java
Author      : Eunbin Choi
Version     :
Copyright   : 
Description : TetrisGrid 
============================================================================
 */

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class TetrisGrid extends Group {
	private TetrisGridCell[][] cells;
	private int width;
	private int height;
	private Location shapeLoc; // 현재 테트로미노의 위
	private TetrisBlockShape currShape; // 현재 테트로미노의 모양

	private final class Location implements Cloneable {
		public int x;
		public int y;

		public Location(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public boolean isValid() {
			return (x >= 0 && x < height && y >= 0 && y < width);
		}

		public void move(int x, int y) {
			this.x += x;
			this.y += y;
		}

		@Override
		public Location clone() {
			Location newLocation = null;
			try {
				newLocation = (Location) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return newLocation;
		}
	}

	public TetrisGrid(int height, int width) {
		resize(width * 20, height * 20);
		this.width = width;
		this.height = height;
		cells = new TetrisGridCell[height][width];
		for (int row = 0; row < height; row++)
			for (int col = 0; col < width; col++) {
				cells[row][col] = new TetrisGridCell(col * 20, row * 20);
				getChildren().add(cells[row][col]);
			}
	}

	public void clear() {
		for (int row = 0; row < height; row++)
			for (int col = 0; col < width; col++) {
				cells[row][col].setColor(Color.DARKGRAY);
			}
	}

	private void drawBlock() {
		byte[][] blockShape = currShape.getCurrentBlock();
		Location curr = new Location(0, 0);
		for (int r = 0; r < 4; r++) {
			curr.x = r + shapeLoc.x;
			for (int c = 0; c < 4; c++) {
				curr.y = c + shapeLoc.y;
				if (blockShape[r][c] == 1 && curr.isValid()) {
					cells[curr.x][curr.y].setColor(currShape.getColor());
				}
			}
		}
	}

	private void eraseBlock() {
		byte[][] blockShape = currShape.getCurrentBlock();
		Location curr = new Location(0, 0);
		for (int r = 0; r < 4; r++) {
			curr.x = r + shapeLoc.x;
			for (int c = 0; c < 4; c++) {
				curr.y = c + shapeLoc.y;
				if (blockShape[r][c] == 1 && curr.isValid())
					cells[curr.x][curr.y].setColor(Color.DARKGRAY);
			}
		}
	}

	private boolean canMove(Location newLoc, byte[][] blockShape) {
		Location curr = new Location(0, 0);
		for (int r = 0; r < 4; r++) {
			curr.x = r + newLoc.x;
			for (int c = 0; c < 4; c++) {
				curr.y = c + newLoc.y;
				if (blockShape[r][c] == 1 && !(curr.isValid() && cells[curr.x][curr.y].isEmpty()))
					return false;
			}
		}
		return true;
	}

	public boolean insertShape(TetrisBlockShape currShape) {
		this.currShape = currShape;
		int startY = (width - 4) / 2;
		shapeLoc = new Location(0, startY);
		if (canMove(shapeLoc, this.currShape.getCurrentBlock())) {
			// 움직일 수 있을때만 블록 그리고 true반환
			drawBlock();
			return true;
		}
		// 움직일 수 없을 때는 false반환(종료조건)
		return false;
	}

	public boolean moveShapeDown() {
		Location newLoc = shapeLoc.clone();
		newLoc.move(1, 0);
		return move(newLoc);
	}

	public void moveShapeLeft() {
		Location newLoc = shapeLoc.clone();
		newLoc.move(0, -1);
		move(newLoc);
	}

	public void moveShapeRight() {
		Location newLoc = shapeLoc.clone();
		newLoc.move(0, 1);
		move(newLoc);
	}

	private boolean move(Location newLoc) {
		boolean flag = false;
		eraseBlock();
		if (canMove(newLoc, currShape.getCurrentBlock())) {
			Sound.play("Move");
			shapeLoc = newLoc;
			flag = true;
		}
		drawBlock();
		return flag;
	}

	public void rotateShape() {
		eraseBlock();
		Location newLoc = shapeLoc.clone();
		if (canMove(newLoc, currShape.getNextBlock())) {
			Sound.play("Rotate");
			currShape.rotate();
		}
		drawBlock();
	}

	public void moveShapeToBottom() {
		Sound.play("HardDrop");
		while (moveShapeDown())
			;
	}

	private boolean isEmptyRow(int row) {
		for (int col = 0; col < width; col++) {
			if (!cells[row][col].isEmpty())
				return false;
		}
		return true;

	}

	private boolean isFullRow(int row) {
		for (int col = 0; col < width; col++) {
			if (cells[row][col].isEmpty())
				return false;
		}
		return true;
	}

	private void removeRow(int delRow) {
		for (int row = delRow; row >= 1; row--) {
			for (int col = 0; col < width; col++) {
				cells[row][col].setColor(cells[row - 1][col].getColor());
			}
			if (isEmptyRow(row)) {
				return;
			}
		}
	}

	public int removeFullRow() {
		int numberOfRows = 0;
		for (int row = height - 1; row >= 0; row--) {
			if (isEmptyRow(row))
				break;
			else if (isFullRow(row)) {
				removeRow(row);
				++row;
				++numberOfRows;
			}
		}
		return numberOfRows;
	}

	public void repaint() {
		for (int row = 0; row < height; row++)
			for (int col = 0; col < width; col++) {
				cells[row][col].draw();
			}
	}
}
