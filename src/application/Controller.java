package application;

import java.util.Optional;

import algorithm.Tuple;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Controller {
	@FXML Canvas canvas;
	@FXML BorderPane holder;
	@FXML StackPane controller;
	@FXML VBox vbox;
	@FXML Button startbtn;
	@FXML Button quitbtn;
	@FXML Text timetxt;
	@FXML Text turntxt;
	@FXML Label roundLabel;
	@FXML Button pausebtn;
	
	private boolean isGameOn;
	private static Game game;
	private static GameTimer timer;
	
	private int height;
	private int width;
	private int height_stride;
	private int width_stride;
	private int canvas_size = AppConfig.CANVAS_SIZE;
	
	public static Object locks = new Object();
	
	// create Chess canvas  
	// the paint process is implemented by canvas
	public void init() {
		canvas_size = AppConfig.CANVAS_SIZE;
		width = AppConfig.WIDTH;
		height = AppConfig.HEIGHT;
		width_stride = AppConfig.CANVAS_SIZE / width;
		height_stride = AppConfig.CANVAS_SIZE / height;
		
		startbtn.prefWidthProperty().bind(vbox.widthProperty());
		pausebtn.prefWidthProperty().bind(vbox.widthProperty());
		quitbtn.prefWidthProperty().bind(vbox.widthProperty());
		
		canvas.setHeight(canvas_size);
		canvas.setWidth(canvas_size);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(1);
		
		turntxt.setText("Click start");
		
		for (int i = 0; i <= width; i++) {
			gc.strokeLine(i*width_stride, 0, i*width_stride, canvas_size);
		}
		for (int j = 0; j <= height; j++) {
			gc.strokeLine(0, j*height_stride, canvas_size, j*height_stride);
		}
		canvas.setOnMouseClicked(eventHandler);
		
		// 倒计时结束后 弹出窗口
		timetxt.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals("00"))
					Platform.runLater(() -> {if (timeupDialog()) restart();});
			}

		});
		System.out.println("init over");
	}

	
	
	EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			double x = event.getX();
			double y = event.getY();
			double size = AppConfig.CHESS_SIZE;
			
			x = Math.round(x / height_stride) * height_stride;
			y = Math.round(y / width_stride) * width_stride;
			if (isGameOn) {
				int xPos = (int) Math.round(x / height_stride)-1;
				int yPos = (int) Math.round(y / width_stride)-1;
				// 玩家落子 计时开始
				if(game.manmove(xPos, yPos)) {
					timer.isPause = false;
					GraphicsContext gc = canvas.getGraphicsContext2D();
					gc.setFill(Color.BLACK);
					gc.fillOval(x-size/2, y-size/2, size, size);
					if (game.play.isManWinning()) {
						timer.isPause = true;
						if (game.manWin()) 
							restart();
						return;
					}
					turntxt.setText("AI's turn");
					// ai 落子
					putWhiteChess();
				}
			}
		}
	};
	
	// 机器落子
	public void putWhiteChess() {
		Tuple pos = game.aimove();
		double size = AppConfig.CHESS_SIZE;
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		int x = (pos.x+1) * height_stride;
		int y = (pos.y+1) * width_stride;
		gc.fillOval(x-size/2, y-size/2, size, size);
		System.out.printf("AI puts at %d %d\n", pos.x, pos.y);
		if (game.play.isAIWinning()) {
			timer.isPause = true;
			if (game.aiWin()) 
				restart();
			return;
		}
		turntxt.setText("Your turn");
		timer.start();
	}
	
	public void start() {
		if (!isGameOn) {
			game = new Game();
			System.out.println("start");
			isGameOn = true;
			
			roundLabel.textProperty().bind(game.stringProperty);
			
			turntxt.setText("You first");
			timer = new GameTimer();
			timetxt.textProperty().bind(timer.timertxt);
			timer.isPause = true;
			timer.start();
		}
		else {
			restart();
		}
	}
	
	public void restart() {
		// 弹出对话框 询问是否重新开始游戏
		if(game.restart()) {
			GraphicsContext gc = canvas.getGraphicsContext2D();
			gc.clearRect(0, 0, canvas_size, canvas_size);
			gc.setStroke(Color.BLACK);
			for (int i = 0; i <= width; i++) {
				gc.strokeLine(i*width_stride, 0, i*width_stride, canvas_size);
			}
			for (int j = 0; j <= height; j++) {
				gc.strokeLine(0, j*height_stride, canvas_size, j*height_stride);
			}
			timer.isPause = true;
			timer.start();
		}
	}
	
	public void pause() {
		if (timer != null) {
			timer.isPause = !timer.isPause;
			canvas.setVisible(!timer.isPause);
		}
		
	}
	
	public void exit() {
		Platform.exit();
	}
	
	private boolean timeupDialog() {
		String header = "Times up";
		String message = "Oh No! You ran out of time. Be cautious next time and Good luck.";
		Alert alert = new Alert(AlertType.INFORMATION, message, 
				new ButtonType("Oh no! ", ButtonData.OK_DONE), new ButtonType("Restart the Game", ButtonData.YES));
		
		alert.setTitle(header);
		alert.setHeaderText(header);
		
		Optional<ButtonType> type =  alert.showAndWait();
		if (type.get().getButtonData().equals(ButtonData.OK_DONE))
			return false;
		else if (type.get().getButtonData().equals(ButtonData.YES))
			return true;
		else 
			return false;
	}
}
