/*
============================================================================
Name        : TetirsBlockFactory.java
Author      : Eunbin Choi
Version     :
Copyright   : 
Description : TetrisBlockFactory
============================================================================
 */
import java.util.Random;
import javafx.scene.paint.Color;


public class TetrisBlockFactory {
	public enum ShapeType {O_SHAPE, Z_SHAPE, S_SHAPE, L_SHAPE, J_SHAPE, T_SHAPE, I_SHAPE;
		public static ShapeType valueOf(int index){
			return ShapeType.values()[index];
		}
	};
	public static final int NUMBEROFSHAPES = 7;
	public static final Color[] shapeColor = {
		Color.YELLOW,
		Color.GREEN,
		Color.ORANGE,
		Color.BLUE,
		Color.CYAN,
		Color.MAGENTA,
		Color.PINK
	};
	private static final byte[][][] oBlock = {
		{
			{0, 0, 0, 0},
			{0, 1, 1, 0},
			{0, 1, 1, 0},
			{0, 0, 0, 0}
		},
		{
			{0, 0, 0, 0},
			{0, 1, 1, 0},
			{0, 1, 1, 0},
			{0, 0, 0, 0}
		}
	};
	private static final byte[][][] zBlock = {
		{
			{0, 0, 0, 0},
			{1, 1, 0, 0},
			{0, 1, 1, 0},
			{0, 0, 0, 0}
		},
		{
			{0, 1, 0, 0},
			{1, 1, 0, 0},
			{1, 0, 0, 0},
			{0, 0, 0, 0}
		},
	};
	private static final byte[][][] sBlock = {
		{
			{0, 0, 0, 0},
			{0, 1, 1, 0},
			{1, 1, 0, 0},
			{0, 0, 0, 0}
		},
		{
			{1, 0, 0, 0},
			{1, 1, 0, 0},
			{0, 1, 0, 0},
			{0, 0, 0, 0}
		}
	};
	private static final byte[][][] lBlock = {
		{
			{0, 0, 0, 0},
			{1, 1, 1, 0},
			{1, 0, 0, 0},
			{0, 0, 0, 0}
		},
		{
			{0, 1, 0, 0},
			{0, 1, 0, 0},
			{0, 1, 1, 0},
			{0, 0, 0, 0}
		},
		{
			{0, 0, 1, 0},
			{1, 1, 1, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}
		},
		{
			{1, 1, 0, 0},
			{0, 1, 0, 0},
			{0, 1, 0, 0},
			{0, 0, 0, 0}
		}
	};
	private static final byte[][][] jBlock = {
		{
			{0, 0, 0, 0},
			{1, 1, 1, 0},
			{0, 0, 1, 0},
			{0, 0, 0, 0}
		},
		{
			{0, 1, 1, 0},
			{0, 1, 0, 0},
			{0, 1, 0, 0},
			{0, 0, 0, 0}
		},
		{
			{1, 0, 0, 0},
			{1, 1, 1, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}
		},
		{
			{0, 1, 0, 0},
			{0, 1, 0, 0},
			{1, 1, 0, 0},
			{0, 0, 0, 0}
		}
	};
	private static final byte[][][] tBlock = {
		{
			{0, 0, 0, 0},
			{1, 1, 1, 0},
			{0, 1, 0, 0},
			{0, 0, 0, 0}
		},
		{
			{0, 1, 0, 0},
			{0, 1, 1, 0},
			{0, 1, 0, 0},
			{0, 0, 0, 0}
		},
		{
			{0, 1, 0, 0},
			{1, 1, 1, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}
		},
		{
			{0, 1, 0, 0},
			{1, 1, 0, 0},
			{0, 1, 0, 0},
			{0, 0, 0, 0}
		}
	};
	private static final byte[][][] iBlock ={
		{
			{0, 0, 0, 0},
			{1, 1, 1, 1},
			{0, 0, 0, 0},
			{0, 0, 0, 0}
		},
		{
			{0, 1, 0, 0},
			{0, 1, 0, 0},
			{0, 1, 0, 0},
			{0, 1, 0, 0}
		}
	};
	private TetrisBlockShape currShape;
	private TetrisBlockShape nextShape;
	private final Random randomGen = new Random(System.currentTimeMillis());
	public TetrisBlockFactory(){
		currShape = getInstance(ShapeType.valueOf(randomGen.nextInt(NUMBEROFSHAPES)));
		nextShape //= getInstance(ShapeType.L_SHAPE);
			= getInstance(ShapeType.valueOf(randomGen.nextInt(NUMBEROFSHAPES)));
	}
	public TetrisBlockShape getInstance(ShapeType type){
		switch(type){
		case O_SHAPE:
			return new TetrisBlockShape(oBlock, shapeColor[type.ordinal()]);
		case Z_SHAPE:
			return new TetrisBlockShape(zBlock, shapeColor[type.ordinal()]);
		case S_SHAPE:
			return new TetrisBlockShape(sBlock, shapeColor[type.ordinal()]);
		case L_SHAPE:
			return new TetrisBlockShape(lBlock, shapeColor[type.ordinal()]);
		case J_SHAPE:
			return new TetrisBlockShape(jBlock, shapeColor[type.ordinal()]);
		case T_SHAPE:
			return new TetrisBlockShape(tBlock, shapeColor[type.ordinal()]);
		default:
			return new TetrisBlockShape(iBlock, shapeColor[type.ordinal()]);
		}
	}
	public TetrisBlockShape getCurrent(){
		return currShape;
	}
	public TetrisBlockShape getNext(){
		return nextShape;
	}
	public void changeBlock(){
		currShape = nextShape;
		nextShape //= getInstance(ShapeType.L_SHAPE);
			= getInstance(ShapeType.valueOf(randomGen.nextInt(NUMBEROFSHAPES)));
	}
}
