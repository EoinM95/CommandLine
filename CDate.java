import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.*;
public class CDate extends Command{
	private String date;
	private Shell sh;
	public CDate(String args, Shell sh){
		this.sh=sh;
		if(args.equals("")){
			SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd");
			date=dateFormat.format(new Date());
		}	
		else{
			Pattern argFormat= Pattern.compile("\\+(%([dHMmY])([/-:\\\\\\. ])?)*");
			Matcher m=argFormat.matcher(args);
			String format="";
			if(m.matches()){
				Pattern elementFormat=Pattern.compile("(%(?<dateInfo>[dHMmY])(?<seperator>[/-:\\\\\\. ])?)");
				m=elementFormat.matcher(args);
				while(m.find()){
					String seperator=m.group("seperator");
					String nextElement=m.group("dateInfo");
					if(nextElement.equals("Y"))
						nextElement+=nextElement;
					nextElement+=nextElement;
					format+=nextElement;
					if(seperator!=null)
						format+=seperator;
				}
				SimpleDateFormat dateFormat=new SimpleDateFormat(format);
				date = dateFormat.format(new Date());
			}
			else{
				sh.showErrorMessage("Argument illégale pour commande <<date>>");
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
