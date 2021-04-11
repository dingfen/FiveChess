package application;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class GameTimer extends Thread {

	private Timer timer;
	private Calendar timenow;
	private Calendar timezero;
	public boolean isPause = false;
	public StringProperty timertxt;
	
    private static TimerTask timerTask;
	
	public GameTimer() {
		timer = new Timer();		
		timenow = Calendar.getInstance();
		timezero = Calendar.getInstance();
		timertxt = new SimpleStringProperty(String.valueOf(AppConfig.ROUND_TIME));
	}

	
	@Override
	public synchronized void start() {
		if (timerTask != null)
			timerTask.cancel();
		countdown(AppConfig.ROUND_TIME);
	}

	private void countdown(int time) {
		try {
			String ts = Integer.toString(time);
			SimpleDateFormat sdf = new SimpleDateFormat("ss");

			Date dateZero = sdf.parse("00");
			Date dataSet = sdf.parse(ts);
			timenow.setTime(dataSet);
			timezero.setTime(dateZero);
		} catch (Exception e) {
			System.out.println("string parse to Date Error !!");
		}
		
		timerTask = new TimerTask() {
			@Override
			public void run() {
				if (!isPause && timenow.compareTo(timezero) > 0) {
					timenow.set(Calendar.SECOND, timenow.get(Calendar.SECOND)-1);
					SimpleDateFormat dateFormat = new SimpleDateFormat("ss");
					String data = dateFormat.format(timenow.getTime());
					timertxt.set(data);
				}
				if (timenow.compareTo(timezero) == 0) {
					System.out.println("times up");
					this.cancel();
				}
			}
		};
		timer.scheduleAtFixedRate(timerTask, 0, 1000);
	}
	
	public static void main(String[] args) {
		GameTimer timer1 = new GameTimer();
		timer1.start();
	}
}
