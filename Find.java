import java.util.regex.*;
import java.io.File;
public class Find extends Command implements Backgroundable,Pipeable{
	private Shell sh;
	private String args;
	private File path;
	private Pattern regex;
	private String result;
	private Pattern argFormat;
	private Pattern pipedArgFormat;
	private boolean caseSensitive;
	private boolean dead;
	private boolean background=false;
	private boolean piped=false;
	public Find(String args, Shell sh){
		this.sh=sh;
		this.args=args;
		dead=false;
		result="";
		argFormat=Pattern.compile("(?<path>.*) (?<param>-name|-iname) (?<regex>.*)");
		pipedArgFormat=Pattern.compile("(?<param>-name|-iname) (?<regex>.*) (?<path>.*)");
	}
	
	@Override
	public void run() {
		Matcher m;
		if(piped)
			m=pipedArgFormat.matcher(args);
		else
			m=argFormat.matcher(args);
		if(m.matches()){
			CD directoryCheck=new CD(m.group("path"),sh);
			path= directoryCheck.directory();
			caseSensitive=m.group("param").equals("-name");
			try{
				if(caseSensitive)
					regex=Pattern.compile(m.group("regex"));
				else
					regex=Pattern.compile(m.group("regex").toLowerCase());
			}
			catch(PatternSyntaxException e){
				sh.showErrorMessage("Syntaxe illégale pour regex");
			}
		}
		else{
			sh.showErrorMessage("Argument illégale pour commande <<find>>");
		}
		if(path.exists()&&path.isDirectory()){
			printFiles(path);
			if(background)
				sh.notifyFinished(this,false);
		}
		else{
			if(background)
				sh.notifyFinished(this,true);
			sh.showErrorMessage("Répétoire pas trouvée");
		}
	}
	
	private void printFiles(File directory){
		File[] files=directory.listFiles();
		Matcher m;
		if(files!=null&&!dead){
			for(File file:files){
				if(dead)
					break;
				else if(file!=null){
					if(file.isDirectory())
						printFiles(file);
					else{
						if(caseSensitive)
							m=regex.matcher(file.toString());
						else
							m=regex.matcher(file.toString().toLowerCase());
						if(m.find()){
							result+=file.toString()+"\n";
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

	@Override
	public void kill() {
		dead=true;
		result+="\nCommande terminé";
	}

	@Override
	public void setBackground(boolean background) {
		this.background=background;
	}

	@Override
	public void setPiped(boolean piped) {
		this.piped=piped;
	}
}
