package mines;

public enum Icon {
	
	// credit to: https://textfac.es/
	DEFAULT("(◐ω◑ )"),CLICK("(ಠ_ಠ)"),WIN("(/^▽^)/"),LOSS("(ಥ_ಥ)");

	final public String icon;
	private Icon(String string) {
		this.icon=string;
	}
	
	@Override
	public String toString() {
		return this.icon;
	}

}
