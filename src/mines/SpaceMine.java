package mines;

public class SpaceMine {
	
	protected int i,j,minesAround;
	protected boolean flag,isMine,isOpen;
	
	public SpaceMine(int i, int j) {
		setI(i);
		setJ(j);
	}

	public SpaceMine(int i, int j,int minesAround) {
		this(i,j);
		setMinesAround(minesAround);
	}
	
	// does minesAround++;
	public void addOneMine() {
		this.minesAround++;
	}
	// does minesAround--
	public void removeOneMine() {
		this.minesAround--;
	}
	
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public int getJ() {
		return j;
	}
	public void setJ(int j) {
		this.j = j;
	}
	public int getMinesAround() {
		return minesAround;
	}
	public void setMinesAround(int minesAround) {
		this.minesAround = minesAround;
	}
	public boolean getFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}
	
	// return the real value of the space no matter if it is opened
	public String getRealString() {
		if (isMine())
			return "X";
		return getMinesAround()>0? getMinesAround()+"":" ";
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
		if (this.isOpen)
			setFlag(false);
	}
	
	@Override
	public String toString() {
		if (getFlag())
			return "F";
		if (!isOpen())
			return ".";
		if (isMine())
			return "X";
		return getMinesAround()>0? getMinesAround()+"":" ";
	}
}
