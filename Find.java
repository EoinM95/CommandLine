import java.util.regex.*;
import java.io.File;
public class Find extends Command implements OutPipeable, Backgroundable{
	private Shell sh;
	private File path;
	private Pattern regex;
	private String result;
	private boolean caseSensitive;
	private boolean dead;
	private boolean background=false;
	public Find(String args, Shell sh){
		this.sh=sh;
		dead=false;
		result="";
		Pattern argFormat=Pattern.compile("(?<path>.*) (?<param>-name|-iname) (?<regex>.*)");
		Matcher m=argFormat.matcher(args);
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
	}
	
	@Override
	public void run() {
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
							//System.out.println(file.toString());
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
		result+="\nCommande terminé\n";
	}

	@Override
	public void setBackground(boolean background) {
		this.background=background;
	}

}
