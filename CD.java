import java.io.File;
import java.util.regex.*;
public class CD extends Command {
	private Shell sh;
	private File newDirectory=null;
	private final Pattern filePattern;
	public static String seperator=File.separator;
	public static String regexSep=(seperator.equals("\\")?"\\\\" : seperator);
	public CD(String args, Shell s) {
		filePattern=Pattern.compile(regexSep+"?((([a-zA-Z0-9])([a-zA-Z0-9_ ]*)"+regexSep+"?)+)");
		this.sh=s;
		String path="";
		if(args.startsWith("..")){
			String parent=sh.getDirectory().getParent();
			args=parent+args.substring(2,args.length());
		}
		if(isAbsolutePath(args))	
			path=args;
		else if(filePattern.matcher(args).matches()){
				path=sh.getDirectory()+seperator+args;
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
	public static boolean isAbsolutePath(String path){
		String root="";
		File directory=Shell.START_DIRECTORY;
		while(directory.getParent()!=null){
			directory=new File(directory.getParent());
		}
		root=directory.toString();
		root=root.replaceAll(regexSep,"");
		Pattern absolutePattern=Pattern.compile(root+regexSep+"((([a-zA-Z0-9 _\\.]+)("+regexSep+"?))*)");
		return absolutePattern.matcher(path).matches();
	}
	
	public File directory(){
		return newDirectory;
	}

	@Override
	public String result() {
		return null;
	}

}
