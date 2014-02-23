package rpg.main;

import java.util.Timer;
import java.util.TimerTask;

import rpg.game.Game;

public class Clock {
	private Game game;
	private Timer timer;

	public Clock(Game game) {
		this.game = game;
		timer = new Timer();
	}

	public void start() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				game.tick();
			}
		}, 1000, 1000);
	}

	public void stop() {
		timer.cancel();
	}
}
