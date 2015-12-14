import java.io.File;
import java.util.regex.*;
public class CD extends Command {
	private Shell sh;
	private File newDirectory;
	private final Pattern filePattern;
	public CD(String args, Shell s) {//On peut ajouter des symboles dans l'expression
		//pour accepter plus de données 
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
			sh.showErrorMessage("Nom illégal pour un répetoire");
		}
		newDirectory=new File(path);
	}

	@Override
	public void run() {
		if(newDirectory.exists()&&newDirectory.isDirectory())
			sh.setDirectory(newDirectory);
		else
			sh.showErrorMessage("Répetoire pas trouvé");
	}
	/**
	 * 
	 * @param path, un chemin à tester
	 * @return true si "path" est un chemin absolue
	 */
	private boolean isAbsolutePath(String path){//il faut changer cette expression pour des systèmes *NIX
		Pattern absolutePattern=Pattern.compile("C:\\\\((([a-zA-Z0-9 ]+)(\\\\?))*)");
		return absolutePattern.matcher(path).matches();
	}
	
	public File directory(){
		return newDirectory;
	}

}
