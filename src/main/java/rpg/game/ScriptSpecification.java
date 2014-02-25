package rpg.game;

public class ScriptSpecification {
	private Script script;
	private Integer durationInSeconds;
	private int frequencyInSeconds = 1;

	ScriptSpecification(Script script, Integer durationInSeconds, int frequencyInSeconds) {
		this.script = script;
		this.durationInSeconds = durationInSeconds;
		this.frequencyInSeconds = frequencyInSeconds;
	}

	public Script script() {
		return script;
	}
	
	public boolean hasDuration() {
		return durationInSeconds != null;
	}
	
	public Integer durationInSeconds() {
		return durationInSeconds;
	}
	
	public int frequencyInSeconds() {
		return frequencyInSeconds;
	}
}
