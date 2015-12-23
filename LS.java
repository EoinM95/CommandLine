/**
 * Affiche le contenu d'un répertoire.
 * 
 * @author Eoin Murphy
 *
 */
public class LS extends Command {
	private Shell sh;
	private String result;

	/**
	 * Construit un nouvel objet LS.
	 * 
	 * @param sh
	 *            shell associé
	 */
	public LS(Shell sh) {
		this.sh = sh;
		result = "";
	}

	@Override
	public void run() {
		String files[] = sh.getDirectory().list();
		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i]);
			result += files[i] + "\n";
		}
	}

	@Override
	public String result() {
		return result;
	}

}
