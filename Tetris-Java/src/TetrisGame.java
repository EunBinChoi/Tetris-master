
/*
============================================================================
Name        : TetirsGame.java
Author      : Eunbin Choi
Version     :
Copyright   : 
Description : TetrisGame
============================================================================
 */
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TetrisGame extends Application {

	private Button startBtn = new Button("start");
	private Button pauseBtn = new Button("pause");
	private TetrisGrid mainGrid = new TetrisGrid(22, 10);
	private TetrisGrid nextGrid = new TetrisGrid(6, 6);
	private TextField levelField = new TextField();
	private TextField lineField = new TextField();
	private TextField scoreField = new TextField();
	private TetrisBlockFactory tetrisBlockFactory = new TetrisBlockFactory();
	private Timeline shoftDropTimer = new Timeline();
	private EventHandler<ActionEvent> btnHandler = new ButtonHandler();

	private int[] framesPerSecond = { 48, 43, 38, 33, 28, 23, 18, 13, 8, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 2 };
	private int[] pointsPerLine = { 0, 40, 100, 300, 1200 };

	private int score = 0;
	private int numberOfLines = 0;
	private int level = 1;

	private class TimerHandler implements EventHandler<ActionEvent> {
		private boolean active = true;

		@Override
		public void handle(ActionEvent event) {
			active = mainGrid.moveShapeDown();
			mainGrid.repaint();
			if (!active) {
				int removedLines = mainGrid.removeFullRow();
				numberOfLines += removedLines;

				switch (removedLines) {
					case 1:
						Sound.play("SingleLineClear");
						break;
					case 2:
						Sound.play("DoubleLineClear");
						break;
					case 3:
					case 4:
						Sound.play("TripleLineClear");
						break;
				}

				int prevLevel = level;
				level = numberOfLines / 10 + 1;
				if (level == prevLevel + 1) {
					levelUp();
					System.out.println("level up");
				}

				score += pointsPerLine[removedLines] * level + 4;

				levelField.setText(level + "");
				lineField.setText(numberOfLines + "");
				scoreField.setText(score + "");

				tetrisBlockFactory.changeBlock();
				startNewBlock();
				active = true;
			}
		}
	}

	private class KeyboardHandler implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event) {
			switch (event.getCode()) {
				case LEFT:
					mainGrid.moveShapeLeft();
					event.consume();
					break;
				case RIGHT:
					mainGrid.moveShapeRight();
					event.consume();
					break;
				case UP:
					mainGrid.rotateShape();
					event.consume();
					break;
				case DOWN:
					mainGrid.moveShapeDown();
					event.consume();
					break;
				case SPACE:
					mainGrid.moveShapeToBottom();
					event.consume();
					score += 4;
					break;
			}
			mainGrid.repaint();
		}
	}

	private class ButtonHandler implements EventHandler<ActionEvent> {
		private boolean paused = false;

		@Override
		public void handle(ActionEvent event) {
			Object source = event.getSource();
			if (source == startBtn) { // 최초 시작, 재시작
				pauseBtn.setDisable(false);
				reset();
			} else if (source == pauseBtn) {
				if (!paused) {
					pauseBtn.setStyle("-fx-text-fill: gray;");
					mainGrid.setOnKeyPressed(null);
					shoftDropTimer.stop();
					paused = true;
				} else {
					pauseBtn.setStyle("-fx-text-fill: black;");
					mainGrid.setOnKeyPressed(new KeyboardHandler());
					mainGrid.requestFocus();
					shoftDropTimer.play();
					paused = false;
				}
			}
		}
	}

	private HBox createActionPane() {
		HBox actionBox = new HBox();
		actionBox.setSpacing(10);
		actionBox.setPadding(new Insets(20, 0, 10, 0));
		actionBox.setAlignment(Pos.CENTER);
		startBtn.setMinWidth(80);
		pauseBtn.setMinWidth(80);
		startBtn.setOnAction(btnHandler);
		pauseBtn.setOnAction(btnHandler);
		pauseBtn.setDisable(true); // pause는 start 이후에만 사용 가능
		actionBox.getChildren().addAll(startBtn, pauseBtn);
		return actionBox;
	}

	private VBox createStatePane() {
		VBox stateBox = new VBox();
		stateBox.setSpacing(10);
		stateBox.setPadding(new Insets(10, 20, 0, 0));
		stateBox.setAlignment(Pos.TOP_CENTER);
		Label levelLabel = new Label("level");
		Label lineLabel = new Label("line");
		Label scoreLabel = new Label("score");
		levelField.setMaxWidth(120);
		lineField.setMaxWidth(120);
		scoreField.setMaxWidth(120);
		levelField.setEditable(false);
		lineField.setEditable(false);
		scoreField.setEditable(false);
		levelField.setText(level + "");
		lineField.setText(numberOfLines + "");
		scoreField.setText(score + "");

		VBox.setMargin(nextGrid, new Insets(0, 0, 80, 0));
		stateBox.getChildren().addAll(nextGrid, levelLabel, levelField,
				lineLabel, lineField, scoreLabel, scoreField);
		return stateBox;
	}

	private Scene createMainTetrisScene() {
		BorderPane mainPane = new BorderPane();

		VBox mainBox = new VBox();
		mainBox.setPadding(new Insets(10, 0, 0, 20));
		mainBox.getChildren().add(mainGrid);
		// 키보드 처리자 등록
		mainGrid.setOnKeyPressed(new KeyboardHandler());

		mainPane.setCenter(mainBox);
		mainPane.setTop(createActionPane());
		mainPane.setRight(createStatePane());
		return new Scene(mainPane, 380, 540);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		shoftDropTimer.setCycleCount(Animation.INDEFINITE);

		primaryStage.setTitle("Java Tetris");
		primaryStage.setScene(createMainTetrisScene());
		primaryStage.show();
	}

	public void startNewBlock() {
		if (mainGrid.insertShape(tetrisBlockFactory.getCurrent())) {
			mainGrid.repaint();
			nextGrid.clear();
			nextGrid.insertShape(tetrisBlockFactory.getNext());
			nextGrid.repaint();
		} else { // 게임종료
			shoftDropTimer.stop();
			shoftDropTimer.getKeyFrames().clear();
			pauseBtn.setDisable(true);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					HallOfFame dialog = new HallOfFame();
					dialog.show(score);
				}
			}); // TimerLine Handling 중에는 dialog showAndWait 실행 못함

			mainGrid.setOnKeyPressed(null);
			startBtn.requestFocus();
		}
	}

	public void levelUp() {
		shoftDropTimer.stop();
		shoftDropTimer.getKeyFrames().clear();
		shoftDropTimer.getKeyFrames().add(
				new KeyFrame(Duration.millis((double) 1000 * framesPerSecond[level - 1] / 60), new TimerHandler()));
		shoftDropTimer.play();
	}

	public void reset() {// 재시작과 관련된 함수 reset
		numberOfLines = 0;
		score = 0;
		level = 1;
		levelField.setText(level + "");// 초기 세팅
		lineField.setText(numberOfLines + "");// 초기 세팅
		scoreField.setText(score + "");// 초기 세팅
		mainGrid.clear();// 화면 지우기
		nextGrid.clear();// 화면 지우기
		shoftDropTimer.getKeyFrames().clear();
		shoftDropTimer.getKeyFrames().add(
				new KeyFrame(Duration.millis((double) 1000 * framesPerSecond[level - 1] / 60), new TimerHandler()));
		shoftDropTimer.play();
		mainGrid.requestFocus();
		startNewBlock();
		mainGrid.setOnKeyPressed(new KeyboardHandler());
		// 버튼 누르고 나서의 입력 처리가 잘 되도록.
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
