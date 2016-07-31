package rpg.tcp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

import rpg.game.*;

public class ClientHandler implements Callable<String>, ClientState {
	private final Socket clientSocket;
	private final CommandExecutor game;
	private final BufferedReader reader;
	private final PrintWriter writer;
	private final CharacterHandle handle;
    private String character;

    public ClientHandler(CommandExecutor game, final Socket clientSocket) throws IOException {
		super();
		this.game = game;
		this.clientSocket = clientSocket;
		reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		writer = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream()), true);
		handle = new CharacterHandle() {
			@Override
			public void heardFromGame(String message) {
				writer.println(message);
			}					
			@Override
			public void heardFrom(String from, String what) {
				writer.println(from + ": " + what);
			}
			@Override
			public void sees(String whoOrWhat, LocalPosition where, LocalMap localMap) {
				writer.println(new GridPrinter(where, localMap).print(whoOrWhat));
			}
			@Override
			public void isAt(LocalPosition localPosition, LocalMap localMap) {
				writer.println(new GridPrinter(localPosition, localMap).print("you"));
			}
			@Override
			public void endSession() {
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	@Override
	public String call() throws Exception {
		try {
			final Commands commands = new Commands(handle, this);
			String command;
			while ((command = reader.readLine()) != null) {
				try {
					Command charCommand = commands.parseCommand(command);
					game.execute(character, charCommand);
				} catch (UnknownCommandException e) {
					writer.println("What does that mean?");
				} catch (NotInGameException e) {
					writer.println("Enter the game first.");
				}
			}
			return "done";
		} finally {
			clientSocket.close();
		}
	}

    @Override
    public void setCharacter(String character) {
        this.character = character;
    }

    @Override
    public String character() {
        return character;
    }
}
