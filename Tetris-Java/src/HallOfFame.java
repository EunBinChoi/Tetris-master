/*
============================================================================
Name        : HallOfFame.java
Author      : Eunbin Choi
Version     :
Copyright   : 
Description : HallOfFame
============================================================================
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HallOfFame {
	private Button okBtn = new Button("확인");
	private Stage mainStage = null;
	
	private class ButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			mainStage.close();
		}	
	}
	
	public static ObservableList<HallOfFameData> loadAndSaveData(HallOfFameData currentScore){
		ObservableList<HallOfFameData> data = FXCollections.observableArrayList();
		int size = 0;
		File file = new File("score.dat");
		try(ObjectInputStream obj_in = new ObjectInputStream(new FileInputStream(file))){
			HallOfFameData initData = (HallOfFameData)obj_in.readObject();
			size = initData.getRank();
			int i = 0;
			while(i<size){
				HallOfFameData scoreData = (HallOfFameData)obj_in.readObject();
				data.add(scoreData);
				++i;
			}
			++size;
			data.add(currentScore);
			data.sort((d1,d2)->{
				int scoreDiff = d2.getScore()-d1.getScore();
				if(scoreDiff==0){
					return d2.getDate().compareTo(d1.getDate());
				}
				else return scoreDiff;
			});
			
		} catch (FileNotFoundException e) {
			currentScore.setRank(1);
			data.add(currentScore);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		int n=1;
		for(HallOfFameData scoreData: data){
			scoreData.setRank(n);
			++n;
		}
		if(size>10){
			size = 10;
			data.remove(10);
		}
		try(ObjectOutputStream obj_out = new ObjectOutputStream(new FileOutputStream(file))){
			HallOfFameData initData = new HallOfFameData();
			initData.setRank(size);
			obj_out.writeObject(initData);
			for(int i=0; i<size; i++){
				obj_out.writeObject(data.get(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public void show(int score){
		TextInputDialog dialog = new TextInputDialog("별명 입력");
		dialog.setTitle("별명 입력창");
		dialog.setHeaderText(null);
		dialog.setContentText("별명:");
		
		Optional<String> result = dialog.showAndWait();
		String nickName = "";
		if (result.isPresent()){
			nickName = result.get();
		}
		
		HallOfFameData currentScore = new HallOfFameData(nickName,LocalDateTime.now(),score);
		
		mainStage = new Stage();
		mainStage.initModality(Modality.APPLICATION_MODAL);
		mainStage.setTitle("Hall of Fame");
		mainStage.setMinWidth(400);
		
		TableView<HallOfFameData> table = new TableView<>();
		TableColumn<HallOfFameData, String> colRank = new TableColumn<>("순위");
		colRank.setMinWidth(40);
		colRank.setStyle("-fx-alignment: center");
		colRank.setCellValueFactory(new PropertyValueFactory<HallOfFameData, String>("Rank"));
		TableColumn<HallOfFameData, String> colName = new TableColumn<>("별명");
		colName.setMinWidth(80);
		colName.setStyle("-fx-alignment: center");
		colName.setCellValueFactory(new PropertyValueFactory<HallOfFameData, String>("Name"));
		TableColumn<HallOfFameData, String> colDate = new TableColumn<>("날짜");
		colDate.setMinWidth(180);
		colDate.setStyle("-fx-alignment: center");
		colDate.setCellValueFactory(new PropertyValueFactory<HallOfFameData, String>("GameDate"));		
		TableColumn<HallOfFameData, Integer> colScore = new TableColumn<>("점수");
		colScore.setMinWidth(80);
		colScore.setStyle("-fx-alignment: center");
		colScore.setCellValueFactory(new PropertyValueFactory<HallOfFameData, Integer>("Score"));
		
		table.getColumns().addAll(colRank, colName, colDate, colScore);
		table.setItems(loadAndSaveData(currentScore));
		
		table.setFixedCellSize(25);
	    table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(12));
	    table.minHeightProperty().bind(table.prefHeightProperty());
	    table.maxHeightProperty().bind(table.prefHeightProperty());
		
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(10));
		FlowPane btnPane = new FlowPane();
		btnPane.setPadding(new Insets(10));
		btnPane.getChildren().add(okBtn);
		btnPane.setAlignment(Pos.CENTER);
		mainPane.setCenter(table);
		mainPane.setBottom(btnPane);
		okBtn.setOnAction(new ButtonHandler());

		mainStage.setScene(new Scene(mainPane));
		mainStage.showAndWait();
	}
}
