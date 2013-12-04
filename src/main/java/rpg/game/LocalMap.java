package rpg.game;

public class LocalMap {
	private int x;
	private int y;

	public LocalMap(int x, int y) {
		super();
		this.y = y;
		this.x = x;
	}

	public int height() {
		return y;
	}

	public int width() {
		return x;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + y;
		result = prime * result + x;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalMap other = (LocalMap) obj;
		if (y != other.y)
			return false;
		if (x != other.x)
			return false;
		return true;
	}
}
