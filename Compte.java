import java.util.Calendar;
import java.util.Date;
import java.util.regex.*;
public class Compte extends Command implements Backgroundable{
	private String args;
	private int limite;
	private Shell sh;
	private Pattern argFormat; 
	private String result;
	private boolean dead;
	private String displayFormat;
	private boolean background=false;
	public Compte(String args, Shell sh) {
		dead=false;
		this.sh=sh;
		this.args=args;
		this.result="";
		argFormat=Pattern.compile("(?<time>[\\d]+)( (?<format>.*))?");	
		Matcher m=argFormat.matcher(args);
		if(m.matches()){
			limite=Integer.parseInt(m.group("time"));
			String f=m.group("format");
			if(f==null||f.equals(""))
				displayFormat="%d%n";
			else
				displayFormat=f;
		}
		else{
			sh.showErrorMessage("Input illégale pour commande <<compteJusqua>>");
		}
	}

	@Override
	public void run() {
		Calendar c=Calendar.getInstance();
		for(int i=0;i<limite&&!dead;i++){
			c.setTime(new Date());
			int second=c.get(Calendar.SECOND);
			String output=String.format(displayFormat,second);
			if(background)
				result+=output;
			else
				System.out.print(output);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				sh.showErrorMessage("Thread interrompu");
				if(background)
					sh.notifyFinished(this,true);
			}
		}
		if(background)
			sh.notifyFinished(this,false);
		else
			result=null;
	}

	@Override
	public String result() {
		return result;
	}
	@Override
	public void kill(){
		dead=true;
		result+="\nCommand terminé";
	}

	@Override
	public void setBackground(boolean background) {
		this.background=background;
		
	}

}
