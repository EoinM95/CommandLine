import java.util.regex.*;
import java.io.File;

/**
 * 
 * Recherche des fichiers dans une hiérarchie de répertoires. Syntaxe : find
 * répertoire [-(name|iname)] [fichier].
 * 
 * @author Eoin Murphy
 *
 */
public class Find extends Command implements OutPipeable {
	private Shell sh;
	private File path;
	private Pattern regex;
	private String result;
	private boolean caseSensitive;

	/**
	 * Construit un nouvel objet Find.
	 * 
	 * @param args
	 *            format : répertoire [-option] [fichier]
	 * @param sh
	 *            shell associé
	 */
	public Find(String args, Shell sh) {
		this.sh = sh;
		result = "";
		Pattern argFormat = Pattern.compile("(?<path>.*) (?<param>-name|-iname) (?<regex>.*)");
		Matcher m = argFormat.matcher(args);
		if (m.matches()) {
			CD directoryCheck = new CD(m.group("path"), sh);
			path = directoryCheck.directory();
			caseSensitive = m.group("param").equals("-name");
			try {
				if (caseSensitive)
					regex = Pattern.compile(m.group("regex"));
				else
					regex = Pattern.compile(m.group("regex").toLowerCase());
			} catch (PatternSyntaxException e) {
				sh.showErrorMessage("Syntaxe de la regex incorrecte");
			}
		} else {
			sh.showErrorMessage("find: '" + args + "' : Aucun fichier ni dossier de ce nom.");
		}
	}

	@Override
	public void run() {
		if (path.exists() && path.isDirectory()) {
			printFiles(path);
			sh.notifyFinished(this, false);
		} else {
			sh.notifyFinished(this, true);
			sh.showErrorMessage("Répertoire inexistant.");
		}
	}

	private void printFiles(File directory) {
		File[] files = directory.listFiles();
		Matcher m;
		if (files != null) {
			for (File file : files) {
				if (file != null) {
					if (file.isDirectory())
						printFiles(file);
					else {
						if (caseSensitive)
							m = regex.matcher(file.toString());
						else
							m = regex.matcher(file.toString().toLowerCase());
						if (m.find()) {
							result += file.toString() + "\n";
							// System.out.println(file.toString());
						}
					}
				}
			}
		}
	}

	@Override
	public String result() {
		return result;
	}

}
