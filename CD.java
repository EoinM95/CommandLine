import java.io.File;
import java.util.regex.*;
public class CD extends Command {
	private Shell sh;
	private File newDirectory;
	private final Pattern filePattern;
	public CD(String args, Shell s) {
		filePattern=Pattern.compile("((([a-zA-Z0-9])([a-zA-Z0-9 ]*)\\\\?)+)");
		this.sh=s;
		String path="";
		if(args.startsWith("..")){
			String parent=sh.getDirectory().getParent();
			args=parent+args.substring(2,args.length());
		}
		if(isAbsolutePath(args))	
			path=args;
		else if(filePattern.matcher(args).matches()){
				path=sh.getDirectory()+"\\"+args;
		}
		else{
			sh.showErrorMessage("Illegal directory name");
		}
		newDirectory=new File(path);
	}

	@Override
	public void run() {
		if(newDirectory.exists()&&newDirectory.isDirectory())
			sh.setDirectory(newDirectory);
		else
			sh.showErrorMessage("Directory not found");
	}
	
	private boolean isAbsolutePath(String path){//C:\\((([a-zA-Z0-9 ]+)(\\?))*)
		Pattern absolutePattern=Pattern.compile("C:\\\\((([a-zA-Z0-9 ]+)(\\\\?))*)");
		return absolutePattern.matcher(path).matches();
	}

}
