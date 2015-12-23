import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.*;
import java.util.ArrayList;

/**
 * Grep recherche dans les fichiers indiqu�s les lignes correspondantes � un
 * certain motif. Syntaxe : grep regex fichiers.
 * 
 * @author Eoin Murphy
 *
 */
public class Grep extends Command {
	private String result;
	private Shell sh;
	private Pattern argFormat;
	private Pattern regex;
	private ArrayList<File> fileList;
	private BufferedReader inputStream;

	/**
	 * Construit un nouvel objet Grep.
	 * 
	 * @param args
	 *            format
	 * @param sh
	 *            shell associ�
	 */
	public Grep(String args, Shell sh) {
		this.sh = sh;
		fileList = new ArrayList<File>();
		result = ""; // need to fix regex, does not work for multiple files
		argFormat = Pattern.compile("(?<regex>[^ ]*)(?<files> (.*))+");
		Matcher m = argFormat.matcher(args);
		if (m.matches()) {
			try {
				regex = Pattern.compile(m.group("regex"));

				String file = m.group("files");
				String[] files = file.split(" ");
				for (String f : files) {
					if (!(f.equals("") || f.equals(" "))) {
						File next = new File(sh.getDirectory() + CD.seperator + f);
						if (!next.exists())
							next = new File(f);
						if (next != null && next.exists())
							fileList.add(next);
						else
							sh.showErrorMessage("Un des fichiers: " + f + " n'a pas �t� trouv�");
					}
				}
			} catch (PatternSyntaxException e) {
				sh.showErrorMessage("Syntaxe de la regex incorrecte");
			}
		} else {
			sh.showErrorMessage("grep: entr�e incorrecte");
		}
	}

	@Override
	public void run() {
		Matcher m;
		try {
			String fileInput;
			for (File file : fileList) {
				fileInput = "";
				inputStream = new BufferedReader(new FileReader(file));
				while (fileInput != null) {
					fileInput = inputStream.readLine();
					if (fileInput != null) {
						m = regex.matcher(fileInput);
						if (m.find()) {
							// System.out.println(fileInput);
							result += fileInput + "\n";
						}
					}
				}
			}
			sh.notifyFinished(this, false);
		} catch (FileNotFoundException e) {
			sh.showErrorMessage("Fichier inexistant");
		} catch (IOException e) {
			sh.showErrorMessage("Erreur de lecture de fichier");
		}
	}

	@Override
	public String result() {
		return result;
	}

}
