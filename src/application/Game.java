package application;

import java.util.Optional;

import algorithm.Chess;
import algorithm.Play;
import algorithm.Tuple;
import algorithm.Chess.Type;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

public class Game {
	// 记录回合数
	int steps = 0;
	StringProperty stringProperty;
	// 总时间
	double sumTime = 0.0;
	// 棋子的大小
	double size = AppConfig.CHESS_SIZE;
	// 下棋位置
	int x, y;
	
	public Play play;
	public Chess chess;
	
	public Game() {
		chess = new Chess();
		play = new Play(chess);
		stringProperty = new SimpleStringProperty("Round: "+ steps);
	}
	
	public boolean manmove(int x, int y) {
		this.x = x;
		this.y = y;
		stringProperty.set("Round: "+ ++steps);
		System.out.printf("Man puts at %d %d\n", x, y);
		if(!play.addChess(new Tuple(x, y) ,Type.BlackChess)) {
			System.out.println("That one is illegal.");
			wrongPosDialog("Wrong Position", "You just clicked an illegal point "
					+ "which piece cannot be put, click ok and try it again.");
			return false;
		}
//		play.chess.show();
		return true;
	}
	
	public Tuple aimove() {
		Tuple postion = play.alphaBetaSearch().postion;
		return postion;
	}
	
	public boolean manWin() {
		String header = "You Win";
		String message = "Congratulations! You've beaten the AI.";
		return manWinDialog(header, message);
	}
	
	public boolean aiWin() {
		String header = "AI Wins";
		String message = "Sorry, you lose the game. AI wins";
		return aiWinDialog(header, message);
	}

	public boolean restart() {
		boolean endgame = restartDialog("Restart the Game ? ", "Are you sure to restart the game ? "
				+ "You will loss this game directly if you quit");
		if(endgame) {
			chess = new Chess();
			play = new Play(chess);
			steps = 0;
		}
		return endgame;
	}
	
	private boolean restartDialog(String header,String message) {
//      按钮部分可以使用预设的也可以像这样自己 new 一个
      Alert alert = new Alert(AlertType.WARNING,message,new ButtonType("cancel", ButtonData.NO),
              new ButtonType("OK", ButtonBar.ButtonData.YES));
//      设置窗口的标题
      alert.setTitle(header);
      alert.setHeaderText(header);

      Optional<ButtonType> _buttonType = alert.showAndWait();
      if(_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
          return true;
      }
      else {
          return false;
      }
    }
	
	private boolean wrongPosDialog(String header, String message) {
		Alert alert = new Alert(AlertType.ERROR, message, new ButtonType("ok", ButtonData.OK_DONE));
		alert.setTitle(header);
		alert.setHeaderText(header);
		
		Optional<ButtonType> type = alert.showAndWait();
		if (type.get().getButtonData().equals(ButtonData.OK_DONE)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean manWinDialog(String header, String message) {
		Alert alert = new Alert(AlertType.INFORMATION, message, new ButtonType("Great! ", ButtonData.OK_DONE), 
				new ButtonType("Play Again", ButtonData.YES));
		alert.setTitle(header);
		alert.setHeaderText(header);
		Optional<ButtonType> type = alert.showAndWait();
		if (type.get().getButtonData().equals(ButtonData.OK_DONE)) {
			return false;
		}
		else if (type.get().getButtonData().equals(ButtonData.YES)) {
			return true;
		}
		return false;
	}
	
	private boolean aiWinDialog(String header, String message) {
		Alert alert = new Alert(AlertType.INFORMATION, message, new ButtonType("Oh No! ", ButtonData.OK_DONE), 
				new ButtonType("Let me try Again", ButtonData.YES));
		alert.setTitle(header);
		alert.setHeaderText(header);
		Optional<ButtonType> type = alert.showAndWait();
		if (type.get().getButtonData().equals(ButtonData.OK_DONE)) {
			return false;
		}
		else if (type.get().getButtonData().equals(ButtonData.YES)) {
			return true;
		}
		return false;
	}
}
