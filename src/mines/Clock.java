package mines;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Clock extends Label {
	
	private Timeline tl;
	private int sec,min;
	
	public Clock(){
		min=-1;
		this.relocate(130, 4);
		this.setAlignment(Pos.CENTER);
		this.setPrefWidth(200);
		this.setText("00:00");
		tl = new Timeline();
		tl.setCycleCount(Timeline.INDEFINITE);
		tl.getKeyFrames().add(new KeyFrame(Duration.seconds(1),new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				sec++;
				updateText();
			}
		}));
	}
	
	public void start() {
		tl.play();
	}
	
	public void pause() {
		tl.pause();
	}
	
	public void reset() {
		tl.stop();
		sec=0;
		updateText();
		start();
	}
	
	private void updateText() {
		this.setText(String.format("Time: %s\t|\tBest Time: %s", getTemplate(sec),(min!=-1)? getTemplate(min):"-"));
	}
	
	private String getTemplate(int s) {
		return String.format("%02d:%02d", s/60,s%60);
	}
	
	public void checkBest() {
		if (sec<min || min==-1)
			min=sec;
	}
}
