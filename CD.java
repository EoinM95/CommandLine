import java.io.File;
import java.util.regex.*;

/**
 * 
 * Change de répertoire. Syntaxe : cd répertoire.
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
	 *            répertoire dans lequel se déplacer
	 * @param sh
	 *            shell associé
	 */
	public CD(String args, Shell sh) {
		if (seperator.equals("\\"))
			regexSep = "\\\\";
		filePattern = Pattern.compile("((([a-zA-Z0-9])([a-zA-Z0-9 ]*)" + regexSep + "?)+)");
		this.sh = sh;

		String path = "";

		/*
		 * Se déplace dans le répertoire parent.
		 */
		if (args.startsWith("..")) {
			String parent = sh.getDirectory().getParent();
			args = parent + args.substring(2, args.length());
		}
		/*
		 * Si l'argument est donné en chemin absolue.
		 */
		if (isAbsolutePath(args))
			path = args;
		else if (filePattern.matcher(args).matches()) {
			path = sh.getDirectory() + seperator + args;
		} else {
			sh.showErrorMessage("Nom illégal pour un répertoire.");
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
	 *            chemin à tester
	 * @return true si l'argument est un chemin absolue
	 */
	public static boolean isAbsolutePath(String path) {
		// Expression à changer sous un système UNIX.
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
	 * Retourne le répetoire courant.
	 * 
	 * @return le répertoire courant
	 */
	public File directory() {
		return newDirectory;
	}

	@Override
	public String result() {
		return newDirectory.toString();
	}

}
