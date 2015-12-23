import java.io.File;
import java.util.regex.*;

/**
 * 
 * Change de r�pertoire. Syntaxe : cd r�pertoire.
 * 
 * @author Eoin Murphy
 *
 */
public class CD extends Command {
	private Shell sh;
	private File newDirectory;
	private final Pattern filePattern;
	public static String seperator = File.separator;
	public static String regexSep = seperator;

	/**
	 * Construit un nouvel objet CD.
	 * 
	 * @param args
	 *            r�pertoire dans lequel se d�placer
	 * @param sh
	 *            shell associ�
	 */
	public CD(String args, Shell sh) {
		if (seperator.equals("\\"))
			regexSep = "\\\\";
		filePattern = Pattern.compile("((([a-zA-Z0-9])([a-zA-Z0-9 ]*)" + regexSep + "?)+)");
		this.sh = sh;

		String path = "";

		/*
		 * Se d�place dans le r�pertoire parent.
		 */
		if (args.startsWith("..")) {
			String parent = sh.getDirectory().getParent();
			args = parent + args.substring(2, args.length());
		}
		/*
		 * Si l'argument est donn� en chemin absolue.
		 */
		if (isAbsolutePath(args))
			path = args;
		else if (filePattern.matcher(args).matches()) {
			path = sh.getDirectory() + seperator + args;
		} else {
			sh.showErrorMessage("Nom ill�gal pour un r�pertoire.");
		}
		newDirectory = new File(path);
	}

	@Override
	public void run() {
		if (newDirectory.exists() && newDirectory.isDirectory())
			sh.setDirectory(newDirectory);
		else
			sh.showErrorMessage("cd: aucun fichier ni dossier de ce nom.");
	}

	/**
	 * 
	 * Retourne vrai si l'argument est un chemin absolue, sinon faux.
	 * 
	 * @param path
	 *            chemin � tester
	 * @return true si l'argument est un chemin absolue
	 */
	public static boolean isAbsolutePath(String path) {
		// Expression � changer sous un syst�me UNIX.
		String root = "";
		File directory = Shell.START_DIRECTORY;
		while (directory.getParent() != null) {
			directory = new File(directory.getParent());
		}
		root = directory.toString();
		root = root.replaceAll(regexSep, "");
		Pattern absolutePattern = Pattern.compile(root + regexSep + "((([a-zA-Z0-9 _]+)(" + regexSep + "?))*)");
		return absolutePattern.matcher(path).matches();
	}

	/**
	 * Retourne le r�petoire courant.
	 * 
	 * @return le r�pertoire courant
	 */
	public File directory() {
		return newDirectory;
	}

	@Override
	public String result() {
		return newDirectory.toString();
	}

}
