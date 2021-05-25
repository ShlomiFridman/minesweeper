package mines;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.LinkedList;
import java.util.List;

public class Mines implements Iterable<Entry<Point, SpaceMine>>{
	
	private int height,width;
	private HashMap<Point,SpaceMine> map;
	private boolean showAll;
	private List<Point> toOpen;	// is cleared in controller after opening the buttons
	private List<Point> mines;

	public Mines(int height, int width, int numMines) {
		toOpen = new LinkedList<Point>();
		mines = new LinkedList<Point>();
		setWidth(width);
		setHeight(height);
		buildMap();
		addRandomMines(numMines);
	}
	
	// build the map, if there is one already than reset it
	public void buildMap() {
		map = new HashMap<Point,SpaceMine>();
		for (int i=0;i<height;i++)
			for (int j=0;j<width;j++) {
				map.put(new Point(i,j), new SpaceMine(i,j));
			}
	}
	
	// add a given number of mines to the map
	public void addRandomMines(int num) {
		if (num>width*height)
			return;
		int i,j;
		while (num>0) {
			i = (int) (Math.random()*height);
			j = (int) (Math.random()*width);
			if (map.get(new Point(i,j)).isMine())
				continue;
			addMine(i,j);
			num--;
		}
	}
	
	// add a mine at (i,j)
	public boolean addMine(int i,int j) {
		if (i<0 || i>=height || j<0 || j>=width)
			return false;
		map.get(new Point(i,j)).setMine(true);
		mines.add(new Point(i,j));
		Iterator<Point> arr = aroundOf(i,j).iterator();
		while (arr.hasNext()) {
			map.get(arr.next()).addOneMine();
		}
		return true;
	}
	
	// get the neighbors of (i,j)
	public List<Point> aroundOf(int i,int j) {
		LinkedList<Point> res = new LinkedList<Point>();
		boolean up,down,left,right;
		up = i>0;
		down = i<height-1;
		left = j>0;
		right = j<width-1;
		if (up && left)
			res.add(new Point(i-1,j-1));
		if (down && left)
			res.add(new Point(i+1,j-1));
		if (down && right)
			res.add(new Point(i+1,j+1));
		if (up && right)
			res.add(new Point(i-1,j+1));
		if (up)
			res.add(new Point(i-1,j));
		if (down)
			res.add(new Point(i+1,j));
		if (left)
			res.add(new Point(i,j-1));
		if (right)
			res.add(new Point(i,j+1));
		return res;
	}
	
	// opens the space and if its empty all around it
	public boolean open(int i,int j) {
		Point point = new Point(i,j);
		SpaceMine space = map.get(point);
		toOpen.add(point);
		if (space.isMine())
			return false;
		if (space.isOpen())
			return true;
		space.setOpen(true);
		if (space.getMinesAround()!=0)
			return true;
		Iterator<Point> arr = aroundOf(i,j).iterator();
		Point tmp;
		while (arr.hasNext()) {
			tmp = arr.next();
			open(tmp.x,tmp.y);
		}
		return true;
	}
	
	public void toggleFlag(int x,int y) {
		Point point = new Point(x,y);
		map.get(point).setFlag(!map.get(point).getFlag());
	}
	
	// if there is a space that isn't a mine and isn't open return false
	public boolean isDone() {
		Iterator<Entry<Point, SpaceMine>> it = map.entrySet().iterator();
		while (it.hasNext()){
			SpaceMine tmp = it.next().getValue();
			if (!tmp.isOpen() && !tmp.isMine())
				return false;
		}
		return true;
	}
	
	// return the toString of a space
	public String get(int i,int j) {
		Point point = new Point(i,j);
		return (showAll)? map.get(point).getRealString():map.get(point).toString();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}
	
	@Override
	public String toString() {
		String res="";
		for (int i=0;i<height;i++) {
			for (int j=0;j<width;j++)
				res+=get(i,j);
			res+="\n";
		}
		return res;
	}

	@Override
	public Iterator<Entry<Point, SpaceMine>> iterator() {
		// For the FX grid
		return map.entrySet().iterator();
	}

	public List<Point> getToOpen() {
		return toOpen;
	}
	public void emptyToOpen() {
		toOpen.clear();
	}

	public List<Point> getMines() {
		return mines;
	}
}
