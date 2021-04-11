package algorithm;

public class Tuple {
	public int x;
	public int y;
	
	public Tuple() {
		this.x = -1;
		this.y = -1;
	}
	
	public Tuple(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	void setPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
