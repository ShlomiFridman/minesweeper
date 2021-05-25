package mines;

import java.awt.Point;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class ButtonMine extends SpaceMine {
	
	private Point point;
	private Button btn;
	
	public ButtonMine(Point point,int minesAround) {
		super(point.x,point.y,minesAround);
		setPoint(point);
		BuildBtn();
	}

	// create the standard setting and set the id
	private void BuildBtn() {
		this.btn=new Button();
		this.btn.setPrefSize(30, 30);
		this.btn.setId(String.format("%d|%d", i,j));
		this.btn.getStyleClass().add("btn");
		updateText();
	}
	
	// update the button's text
	public void updateText() {
		if (isOpen)
			if (!isMine)
				this.btn.setText(minesAround>0? minesAround+"":" ");
			else
				this.btn.setText("💣");
		else
			this.btn.setText(flag? "🚩":" ");
	}
	
	// open the space and add its style
	public boolean open() {
		if (isOpen)
			return true;
		isOpen = true;
		updateText();
		if (isMine) {
			this.btn.getStyleClass().add(flag? "flag":"mine");
			this.btn.setMouseTransparent(true);
		}
		else if (minesAround>0) {
			this.btn.getStyleClass().add(String.format("clicked%d", minesAround));
			//this.setMouseTransparent(true);
		}
		else
			btnFade();
		if (flag)
			toggleFlag();
		return true;
	}
	
	// the fade animation for empty spaces
	public void btnFade() {
		FadeTransition fade = new FadeTransition();
		fade.setNode(this.btn);
		fade.setDuration(Duration.seconds(0.4));
		fade.setFromValue(1);
		fade.setToValue(0);
//		fade.setOnFinished(ev->this.btn.setVisible(false));
		fade.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				btn.setVisible(false);
			}
			
		});
		fade.play();
	}
	
	public Point getPoint() {
		return point;
	}
	
	public void setPoint(Point point) {
		this.point = point;
	}
	
	public void setPoint(int i,int j) {
		this.point = new Point(i,j);
	}
	
	public Button getBtn() {
		return this.btn;
	}

	public void toggleFlag() {
		this.flag = !this.flag;
		if (flag)
			this.btn.getStyleClass().add("flag");
		else
			this.btn.getStyleClass().remove("flag");
		updateText();
	}

}
