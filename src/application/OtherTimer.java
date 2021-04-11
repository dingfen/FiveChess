package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class OtherTimer extends Application {

    Timer timer = new Timer();
    Button btnStart = new Button("Start");
    Button btnPause = new Button("Pause");
    Button btnSet = new Button("Set");
    Text textTimeleft = new Text("HH:MM:SS");
    TextField textSet = new TextField("HH:MM");
    Calendar timenow =  Calendar.getInstance();
    Calendar timeZero =  Calendar.getInstance();
    boolean bPause = false;
    
    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {

            if (bPause) {
                if(timenow.compareTo(timeZero)>0) {
                    timenow.set(Calendar.SECOND, timenow.get(Calendar.SECOND)-1);
                    if ((timenow.getTimeInMillis() - timeZero.getTimeInMillis()) <= 10000) {
                        flashText();
                    }
                }else {
                    bPause = false;
                }
                //display
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");  
                String dateString = formatter.format(timenow.getTime()); 
                textTimeleft.setText(dateString);
            }
        }
    };
    
    boolean bToggle = false;
    private void flashText() {
        
        bToggle = !bToggle;
        if(bToggle) {
            textTimeleft.setFont(Font.font("Tahoma", FontWeight.BOLD, 64));
        }else {
            textTimeleft.setFont(Font.font("Tahoma", FontWeight.NORMAL, 64));
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        timer.scheduleAtFixedRate(timerTask, 0, 1000);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date dateZero = sdf.parse("00:00");
            Date dataSet = sdf.parse("00:10");
            timenow.setTime(dataSet);
            timeZero.setTime(dateZero);
            textTimeleft.setText("00:10:00");
        } catch (ParseException e) {
        }
        
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bPause = true;
            }
        });
        
        btnPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bPause = false;
            }
        });
        
        btnSet.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try {
                    timenow.setTime(sdf.parse(textSet.getText()));
                    sdf = new SimpleDateFormat("HH:mm:ss");  
                    String dateString = sdf.format(timenow.getTime()); 
                    textTimeleft.setText(dateString);
                } catch (ParseException e) {
                }
            }
        });
        

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        grid.add(btnStart, 0, 0);
        grid.add(btnPause, 2, 0);
        grid.add(btnSet, 2, 2);
        textTimeleft.setFont(Font.font("Tahoma", FontWeight.NORMAL, 64));
        grid.add(textTimeleft, 0, 1, 2, 1);
        GridPane.setHalignment(textTimeleft, HPos.CENTER);
        GridPane.setColumnSpan(textTimeleft, GridPane.REMAINING);
        GridPane.setHgrow(textTimeleft, Priority.ALWAYS);
        grid.add(textSet, 0, 2);

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        
        primaryStage.setTitle("倒计时");
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                timer.cancel();
            }
        });
    }
}