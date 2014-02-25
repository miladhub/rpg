package rpg.game;

public class ScriptSpecificationBuilder {
	private Script script;
	private Integer durationInSeconds;
	private int frequencyInSeconds;

	ScriptSpecificationBuilder(Script script) {
		this.script = script;
	}

	public ScriptSpecificationBuilder lasting(int seconds) {
		this.durationInSeconds = seconds;
		return this;
	}
	
	public ScriptSpecificationBuilder every(int seconds) {
		this.frequencyInSeconds = seconds;
		return this;
	}
	
	public ScriptSpecification build() {
		return new ScriptSpecification(script, durationInSeconds, frequencyInSeconds);
	}
}
