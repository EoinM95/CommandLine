import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.*;
import java.util.ArrayList;
public class Grep extends Command implements Backgroundable, Pipeable {
	private String result;
	private Shell sh;
	private Pattern argFormat;
	private Pattern regex;
	private ArrayList<File> fileList;
	private BufferedReader inputStream;
	private boolean dead;
	private boolean background=false;
	private boolean piped=false;
	private String text;
	public Grep(String args, Shell sh){
		this.sh=sh;
		dead=false;
		fileList=new ArrayList<File>();
		result="";
		args=args.replaceAll("\n"," ");
		argFormat=Pattern.compile("(?<regex>[^ ]*)(?<files> (.*))+");
		Matcher m=argFormat.matcher(args);
		if(m.matches()){
			try{
				regex=Pattern.compile(m.group("regex"));
				if(!piped){
					String file=m.group("files");
					String[] files=file.split(" ");
					for(String f:files){
						if(!(f.equals("")||f.equals(" "))){
							File next=new File(sh.getDirectory()+CD.seperator+f);
							if(!next.exists())
								next=new File(f);				
							if(next!=null&&next.exists())
								fileList.add(next);
							else
								sh.showErrorMessage("Un des fichiers: "+f+" pas trouvée");
						}
					}
				}
				else
					text=m.group("files");
			}
			catch(PatternSyntaxException e){
				sh.showErrorMessage("Syntaxe illégale pour regex");
			}
		}
		else{
			sh.showErrorMessage("Input illégale");
		}
	}
	
	@Override
	public void run() {
		Matcher m;
		if(!piped){
			try {
				String fileInput;
				for(File file:fileList){
					if(dead)
						break;
					fileInput="";
					inputStream=new BufferedReader(new FileReader(file));
					while(fileInput!=null&&!dead){
						fileInput=inputStream.readLine();
						if(fileInput!=null&&!dead){
							m=regex.matcher(fileInput);
							if(m.find()){
								//System.out.println(fileInput);
								result+=fileInput+"\n";
							}
						}
					}
				}
				if(background)
					sh.notifyFinished(this,false);
			}
			catch (FileNotFoundException e) {		
				sh.showErrorMessage("Fichier pas trouvé");
			}
			catch (IOException e) {
				sh.showErrorMessage("Erreur de lecture de fichier");
			}
		}
		/*else{
			String[] lines=text.split("\n");
			for(int i=0;i<lines.length&&!dead;i++){
				m=regex.matcher(lines[i]);
				if(m.find()){
					result+=lines[i]+"\n";
				}
			}
			if(background)
				sh.notifyFinished(this,false);
		}*/
	}

	@Override
	public String result() {
		return result;
	}

	@Override
	public void kill() {
		dead=true;
		result+="\nCommand terminé\n";
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
