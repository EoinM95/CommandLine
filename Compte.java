import java.util.Calendar;
import java.util.Date;
import java.util.regex.*;
public class Compte extends Command {
	private String args;
	private int limite;
	private Shell sh;
	private Pattern argFormat; 
	private String result;
	public Compte(String args, Shell sh) {
		this.sh=sh;
		this.args=args;
		this.result="";
		argFormat=Pattern.compile("(?<time>[\\d]+)( (?<format>.*))?");	
		Matcher m=argFormat.matcher(args);
		if(m.matches()){
			limite=Integer.parseInt(m.group("time"));
		}
	}

	@Override
	public void run() {
		Calendar c=Calendar.getInstance();
		for(int i=0;i<limite;i++){
			c.setTime(new Date());
			int second=c.get(Calendar.SECOND);
			result+=second+"\n";
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				sh.notifyFinished(this,true);
			}
		}
		sh.notifyFinished(this,false);
	}

	@Override
	public String result() {
		return result;
	}

}
