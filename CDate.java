import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.*;
public class CDate extends Command implements OutPipeable{
	private String date;
	public CDate(String args){
		if(args.equals("")){
			SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY.MM.dd");
			date=dateFormat.format(new Date());
		}	
		else{
			date="";
			//parse args 
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
