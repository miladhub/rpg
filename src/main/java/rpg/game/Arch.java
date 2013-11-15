package rpg.game;

public class Arch {
	private final Location start;
	private final Location end;

	public Arch(Location first, Location second) {
		this.start = first;
		this.end = second;
	}

	public boolean startsFrom(Location loc) {
		return start.equals(loc);
	}
	
	public boolean endsAt(Location loc) {
		return end.equals(loc);
	}

	public Location end() {
		return end;
	}

	public Location start() {
		return start;
	}
	
	@Override
	public String toString() {
		return start + " --> " + end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
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
		Arch other = (Arch) obj;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		return true;
	}
}
