import java.io.File;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.*;

/**
 * Shell.
 * 
 * @author Eoin Murphy
 *
 */
public class Shell implements Runnable {

	private class ShellThread extends Thread {
		private Command c;

		public ShellThread(Command c) {
			super(c);
			this.c = c;
		}

		public void kill() {
			// this.interrupt();
		}

		/**
		 * @return Command
		 */
		public Command getCommand() {
			return c;
		}
	}

	public static final String USER_NAME = System.getProperty("user.name");
	public static final File START_DIRECTORY = new File(System.getProperty("user.home"));
	private Interpreter interpreter;
	private File currentDirectory;
	private boolean error;
	private final Pattern commandPattern;
	private Matcher m;
	private Hashtable<Integer, ShellThread> activeProcesses;
	private int processCount = 0;
	private boolean pwd;

	/**
	 * Construit un nouvel objet thread.
	 */
	public Shell() {
		commandPattern = Pattern.compile("(?<command>[a-zA-Z]+) ?(?<args>.*)");
		interpreter = new Interpreter(this);
		activeProcesses = new Hashtable<Integer, ShellThread>();
		error = false;
		currentDirectory = START_DIRECTORY;
		pwd();
		pwd = true;
	}

	/**
	 * Retourne le r�pertoire courant.
	 * 
	 * @return le r�pertoire courant
	 */
	public File getDirectory() {
		return currentDirectory;
	}

	/**
	 * Afiche le nom du r�pertoire de travail en cours.
	 */
	public void pwd() {
		System.out.println(USER_NAME + "@" + currentDirectory.toString());
	}

	/**
	 * @param directory,
	 *            mettre à jour la répétoire courante
	 */

	/**
	 * Initialise le r�pertoire courant.
	 * 
	 * @param directory
	 *            r�pertoire courant � modifier
	 */
	public void setDirectory(File directory) {
		this.currentDirectory = directory;
	}

	/**
	 * Ex�cute le shell.
	 */
	public void run() {
		try {
			while (true) {
				ShellThread current = null;
				error = false;
				String input = readInput();
				m = commandPattern.matcher(input);
				pwd = false;
				if (m.matches()) {
					String args[] = parse(input);
					if (args[0].equals("pwd")) {
						pwd();
						pwd = true;
					} else {
						Command c = interpreter.command(args[0], args[1]);
						if (!error && c != null) {
							c.setPID(processCount++);
							current = new ShellThread(c);
							// activeProcesses.put((Integer)c.getPID(),current);
							System.out.println(args[0] + " pid=" + c.getPID());
							current.start();
							Thread.sleep(20);
						}
					}
				} else
					showErrorMessage("Symbole illégale");
				if (!pwd && (current == null || !current.isAlive()))
					pwd();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la commande ex�cut�e si elle s'est termin� correctement.
	 * 
	 * @param c
	 *            commande termin�e
	 * @param error
	 *            true s'il y a une erreur
	 */
	public void notifyFinished(Command c, boolean error) {
		if (!error) {
			System.out.println("La commande pid=" + c.getPID() + " a termin�, son resultat :");
			System.out.println(c.result());
			pwd();
			pwd = true;
		}
	}

	private String[] parse(String input) {
		String[] parsed = new String[2];
		parsed[0] = m.group("command");
		parsed[1] = m.group("args");
		return parsed;
	}

	private String readInput() {
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		return input;
	}

	/**
	 * Affiche un message d'erreur d'une commande mal ex�cut�e et l'arr�te.
	 * 
	 * @param message
	 *            message d'erreur li� � la commande
	 */
	public void showErrorMessage(String message) {
		error = true;
		System.out.println("Erreur: " + message);
	}

	public static void main(String[] args) {
		Shell s = new Shell();
		s.run();
	}

}
