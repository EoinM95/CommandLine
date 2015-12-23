
/**
 * 
 * Commande � ex�cuter.
 * 
 * @author Eoin Murphy
 *
 */
public abstract class Command implements Runnable, OutPipeable {
	protected int pid;

	/**
	 * Initialise pid par la valeur donn�e.
	 * 
	 * @param pid
	 *            id de la commande
	 */
	public void setPID(int pid) {
		this.pid = pid;
	}

	/**
	 * Retourne l'id de la commande.
	 * 
	 * @return l'id de la commande
	 */
	public int getPID() {
		return pid;
	}
}
