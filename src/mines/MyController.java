package mines;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MyController {
	
	private int totalMines,state; 	// state 2=win , 1=loss , 0=playing;
	private Stage stage;
	private Mines map;
	private HashMap<Point,ButtonMine> btnMap;
	private Clock clock;

    @FXML
    private AnchorPane ancPane;

    @FXML
    private Pane mainPane;
    
    @FXML
    private GridPane grid;

    @FXML
    private TextField tfWidth;

    @FXML
    private TextField tfHeight;

    @FXML
    private Button btnStart;

    @FXML
    private TextField tfMines;

    @FXML
    public Label info;

    @FXML
    private Label icon;
	
	public void newGame() {
		if (clock==null) {	// because there is no normal constructor i'm building it here
			clock = new Clock();
			mainPane.getChildren().add(clock);
		}
		int height,width,totalMines;
		height = width = totalMines = 0;
		try {
			height=Integer.parseInt(tfHeight.getText());
			width=Integer.parseInt(tfWidth.getText());
			totalMines=Integer.parseInt(tfMines.getText());
		} catch (Exception e) {
			info.setText("Invalid setting");
			return;
		}
		if (width<=0 || height<=0) {
			info.setText("Invalid size");
			return;
		}
		else if (totalMines>=width*height || totalMines<0) {
			info.setText("Invalid number of mines");
			return;
		}
		this.totalMines=totalMines;
		this.clock.reset();	// resets the clock
		if (grid.isMouseTransparent())
			unfreezeAll();
		this.state=0;
		updateIcon();
		grid.getChildren().clear();
		map = new Mines(height,width,totalMines);
		buildBtns();
		setMines();
		updateInfo();
		info.requestFocus();
		stage.sizeToScene();
	}
	
	// build the buttons from the map
	private void buildBtns() {
		btnMap = new HashMap<Point,ButtonMine>();
		Iterator<Entry<Point, SpaceMine>> it = map.iterator();
		Entry<Point, SpaceMine> tmp;
		ButtonMine btn;
		while (it.hasNext()) {
			tmp = it.next();
			btn = new ButtonMine(tmp.getKey(),tmp.getValue().getMinesAround());
			btnMap.put(tmp.getKey(),btn);
			setEvents(btn);	// set the events for the button
			grid.add(btnMap.get(tmp.getKey()).getBtn(), tmp.getKey().y, tmp.getKey().x);
		}
	}
	
	// set the mines in the buttons map
	private void setMines() {
		Iterator<Point> it = map.getMines().iterator();
		while (it.hasNext())
			btnMap.get(it.next()).setMine(true);
	}
	
	// the button events
	private void setEvents(ButtonMine btn) {
		btn.getBtn().setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				gridClicked(event);
				
			}
			
		});
		btn.getBtn().setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				gridUnclicked(event);
				updateIcon();
			}
			
		});
		
		btn.getBtn().setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (!(event.getSource() instanceof Button)) return;
				Button btn = (Button) event.getSource();
				ButtonMine btnMine = getBtnMine(btn);
				if (event.getClickCount()!=2 || !btnMine.isOpen())
					return;
				Iterator<Point> it = map.aroundOf(btnMine.getI(), btnMine.getJ()).iterator();
				while (it.hasNext())
					openBtn(btnMap.get(it.next()));
				updateIcon();
			}
			
		});
	}
	
	// update the info label, not the real amount of mines left, its just to help follow how many flags you need to put
	private void updateInfo() {
		info.setText(String.format("%d mines left", totalMines>0? totalMines:0));
		if (totalMines<0)
			info.setText(String.format("%s\n%d extra flag", info.getText(),totalMines*-1));
	}
	
    @FXML
    void enter(KeyEvent event) {
    	if (event.getCode()==KeyCode.ENTER)
    		newGame();
    }

    @FXML
    void go(MouseEvent event) {
    	newGame();
    }

    @FXML
    void gridClicked(MouseEvent event) {
    	icon.setText(Icon.CLICK.icon);
    }
    
    // update icon based on state of the game
    private void updateIcon() {
    	switch (state) {
			case 0: icon.setText(Icon.DEFAULT.icon); break;
			case 1: icon.setText(Icon.LOSS.icon); break;
			case 2: icon.setText(Icon.WIN.icon); break;
    	}
	}

    @FXML
    void gridUnclicked(MouseEvent event) {
    	Button btn = (Button) event.getSource();
    	ButtonMine btnMine = getBtnMine(btn);
    	if (event.getButton()==MouseButton.SECONDARY) {
    		toggleFlag(btnMine);
    		return;
    	}
    	openBtn(btnMine);
    }
    
    // open the space button if blank open all around it, won't open a flagged space (only in recursive it will open)
    private void openBtn(ButtonMine btn) {
    	if (btn.getFlag() || btn.isOpen())
    		return;
		if (!map.open(btn.getI(),btn.getJ())) {
			lostGame();
			return;
		}
		else {
	    	btn.open();
	    	Iterator<Point> it;
	    	it = map.getToOpen().iterator();
	    	while (it.hasNext())
	    		btnMap.get(it.next()).open();
	    	map.emptyToOpen();
			info.requestFocus();
			if (map.isDone()) 
				wonGame();
		}
    }
    
    // toggle the flag of a button, effect on the map and grid
    private void toggleFlag(ButtonMine btn) {
    	if (btn.isOpen())
    		return;
		map.toggleFlag(btn.getI(), btn.getJ());
		btn.toggleFlag();
		totalMines += (btn.getFlag())? -1:1;
		updateInfo();
    }
    
    public void lostGame() {
		info.setText("You Have Lost");
		state=1;
		updateIcon();
		Iterator<Point> it = map.getMines().iterator();
		while (it.hasNext())
			btnMap.get(it.next()).open();
		freezeAll();
    }
    
    public void wonGame() {
		info.setText("You Have Won!");
		state=2;
		Iterator<Point> it = map.getMines().iterator();
		Point tmp;
		while (it.hasNext()) {
			tmp = it.next();
			btnMap.get(tmp).getBtn().getStyleClass().add("flag");
			btnMap.get(tmp).open();
		}
		clock.checkBest();
		freezeAll();
    }
    
    // change the transparent state of the grid to true
    public void freezeAll() {
    	grid.setMouseTransparent(true);
    	clock.pause();
    }
    
    // change the transparent state of the grid to false
    public void unfreezeAll() {
    	grid.setMouseTransparent(false);
    }
    
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	// returns the ButtonMine for given button
	private ButtonMine getBtnMine(Button btn) {
		return btnMap.get(new Point(GridPane.getRowIndex(btn),GridPane.getColumnIndex(btn)));
	}
	

}
