import java.io.File;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.*;
public class Shell implements Runnable {
	public static final String USER_NAME=System.getProperty("user.name");
	public static final File START_DIRECTORY=new File(System.getProperty("user.home"));
	private Interpreter i;
	private File currentDirectory;
	private boolean error;
	private final Pattern commandPattern;
	private Matcher m;
	private Hashtable<Integer,Thread> activeProcesses;
	private int processCount=0;
	public Shell(){
		commandPattern=Pattern.compile("(?<command>[a-z]+) ?(?<args>.*)");
		i=new Interpreter(this);
		activeProcesses=new Hashtable<Integer,Thread>();
		error=false;
		currentDirectory=START_DIRECTORY;
		System.out.println(USER_NAME+"@"+currentDirectory.toString());
	}
	
	/**
	 * @return La répétiore courante
	 */
	public File getDirectory() {
		return currentDirectory;
	}
	
	public void pwd(){
		System.out.println(USER_NAME+"@"+currentDirectory.toString());
	}

	/**
	 * @param directory, mettre à jour la répétoire courante
	 */
	public void setDirectory(File directory) {
		this.currentDirectory = directory;
	}

	public void run(){
		try{
			while(true){
				error=false;
				String input=readInput();
				m=commandPattern.matcher(input);
				boolean pwd=false;
				/*
				 * Il faut ajouter un cas ici pour des commandes avec |
				 * Il faut améliorer l'implémentation multi-taches
				 */
				if(m.matches()){
					String args[] =parse(input);
					if(args[0].equals("pwd")){
						pwd();
						pwd=true;
					}	
					else{
						Command c=i.command(args[0],args[1]);
						if(!error&&c!=null){
							c.setPID(processCount++);
							Thread current=new Thread(c);
							activeProcesses.put((Integer)c.getPID(),current);
							current.start();
							current.join();
						}
					}
				}
				else 
					showErrorMessage("Symbole illégale");
				if(!pwd)
					pwd();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private String[] parse(String input) {
		String[] parsed= new String[2];
		parsed[0]=m.group("command");
		parsed[1]=m.group("args");
		return parsed;
	}

	private String readInput() {
		Scanner scanner=new Scanner(System.in);
		String input = scanner.nextLine();
		return input;
	}
	
	
	public void showErrorMessage(String message){
		error=true;
		System.out.println("Erreur: "+message);
	}
	
	public static void main(String[] args){//Juste pour tester
		Shell s = new Shell();
		s.run();
	}
	
}
