import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.*;

/**
 * 
 * Affiche la date et l'heure du système.
 * 
 * @author Eoin Murphy
 *
 */
public class CDate extends Command {
	private String date;
	private Shell sh;

	/**
	 * Construit un nouvel objet CDate.
	 * 
	 * @param args
	 *            format de la date
	 * @param sh
	 *            shell associé
	 */
	public CDate(String args, Shell sh) {
		this.sh = sh;
		if (args.equals("")) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
			date = dateFormat.format(new Date());
		} else {
			Pattern argFormat = Pattern.compile("\\+(%([dHMmY])([/-:\\\\\\. ])?)*");
			Matcher m = argFormat.matcher(args);
			String format = "";
			if (m.matches()) {
				Pattern elementFormat = Pattern.compile("(%(?<dateInfo>[dHMmY])(?<seperator>[/-:\\\\\\. ])?)");
				m = elementFormat.matcher(args);
				while (m.find()) {
					String seperator = m.group("seperator");
					String nextElement = m.group("dateInfo");
					if (nextElement.equals("Y"))
						nextElement += nextElement;
					nextElement += nextElement;
					format += nextElement;
					if (seperator != null)
						format += seperator;
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				date = dateFormat.format(new Date());
			} else {
				sh.showErrorMessage("date: date incorrecte << " + args + " >>");
			}
		}
	}

	@Override
	public void run() {
		System.out.println(date);

	}

	@Override
	public String result() {
		return date;
	}
}
