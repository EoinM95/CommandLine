import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.*;
import java.util.ArrayList;
public class Grep extends Command {

	private String result;
	private Shell sh;
	private Pattern argFormat;
	private Pattern regex;
	private ArrayList<File> fileList;
	private BufferedReader inputStream;
	public Grep(String args, Shell sh){
		this.sh=sh;
		fileList=new ArrayList<File>();
		result="";
		argFormat=Pattern.compile("(?<regex>.*) (?<files>.*)");
		Matcher m=argFormat.matcher(args);
		if(m.matches()){
			regex=Pattern.compile(m.group("regex"));
			String file=m.group("files");
			System.out.println(file);
			String[] files=file.split(" ");
			for(String f:files){
				File next;
				next=new File(sh.getDirectory(),f);
				if(!next.exists())
					next=new File(f);				
				if(next!=null&&next.exists())
					fileList.add(next);
				else
					sh.showErrorMessage("Un des fichiers: "+f+" pas trouvée");
				
			}
		}
		else{
			sh.showErrorMessage("Input illégale");
		}
	}
	
	@Override
	public void run() {
		Matcher m;
		try {
			String fileInput;
			for(File file:fileList){
				fileInput="";
				inputStream=new BufferedReader(new FileReader(file));
				while(fileInput!=null){
					fileInput=inputStream.readLine();
					if(fileInput!=null){
						m=regex.matcher(fileInput);
						if(m.find()){
							//System.out.println(fileInput);
							result+=fileInput+"\n";
						}
					}
				}
			}
			sh.notifyFinished(this,false);
		}
		catch (FileNotFoundException e) {		
				e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String result() {
		return result;
	}

}
