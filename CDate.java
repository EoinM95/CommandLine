import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.*;
public class CDate extends Command{
	private String date=null;
	private Shell sh;
	private String format;
	public CDate(String args, Shell sh){
		this.sh=sh;
		format="";
		if(args.equals("")){
			format="YYYY-MM-dd";
		}	
		else{
			Pattern argFormat= Pattern.compile("\\+(%([dHMmY])([/-:\\\\\\. ])?)*");
			Matcher m=argFormat.matcher(args);		
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
			}
			else{
				sh.showErrorMessage("Argument illégale pour commande <<date>>");
			}
		}
	}

	@Override
	public void run() {
		SimpleDateFormat dateFormat=new SimpleDateFormat(format);
		date = dateFormat.format(new Date());	
	}

	@Override
	public String result() {
		return date;
	}
}
