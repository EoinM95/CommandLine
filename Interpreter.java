/**
 * 
 * Commandes à exécuter.
 * 
 * @author Eoin Murphy
 *
 */
public class Interpreter {
	private Shell sh;

	/**
	 * Construit un nouvel objet Shell.
	 * 
	 * @param sh
	 *            shell associé
	 */
	public Interpreter(Shell sh) {
		this.sh = sh;
	}

	/**
	 * 6 commandes : cd, ls, date, find, compteJusqua, grep. Pour la syntaxe,
	 * voir les classes respectives.
	 */
	enum Commands {
		cd, ls, date, find, compteJusqua, grep;
		public Command command(String cmd, String args, Shell sh) {
			switch (this) {
			case cd:
				return new CD(args, sh);
			case ls:
				return new LS(sh);
			case date:
				return new CDate(args, sh);
			case find:
				return new Find(args, sh);
			case compteJusqua:
				return new Compte(args, sh);
			case grep:
				return new Grep(args, sh);
			default:
				return null;
			}
		}
	}

	/**
	 * Retourne la commande tapée dans le terminal.
	 * 
	 * @param cmd
	 *            commande à exécuter
	 * @param args
	 *            argument de la commande
	 * @return la commande
	 */
	public Command command(String cmd, String args) {
		Command c = null;
		for (Commands com : Commands.values()) {
			if (com.toString().equals(cmd))
				c = com.command(cmd, args, sh);
		}
		return c;
	}
}
