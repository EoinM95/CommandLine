import java.io.File;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.*;
public class Shell implements Runnable {
	private class ShellThread extends Thread{
		private Command c;

		public ShellThread(Command c) {
			super(c);
			this.c=c;
		}
		
		public void kill(){
			this.interrupt();
			
		}

		/**
		 * @return Command
		 */
		public Command getCommand() {
			return c;
		}
	}
	public static final String USER_NAME=System.getProperty("user.name");
	public static final File START_DIRECTORY=new File(System.getProperty("user.home"));
	private Interpreter interpreter;
	private File currentDirectory;
	private boolean error;
	private final Pattern commandPattern;
	private Matcher m;
	private Hashtable<Integer,ShellThread> activeProcesses;
	private int processCount=0;
	private boolean pwd;
	public Shell(){
		commandPattern=Pattern.compile("(?<command>[a-zA-Z]+) ?(?<args>.*)");
		interpreter=new Interpreter(this);
		activeProcesses=new Hashtable<Integer,ShellThread>();
		error=false;
		currentDirectory=START_DIRECTORY;
		pwd();
		pwd=true;
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
				ShellThread current = null;
				//Thread.sleep(20);
				/*for(int i=0;i<processCount;i++){
					ShellThread p=activeProcesses.get((Integer)i);
					if(p!=null&&!p.isAlive()){
						System.out.println("La commande pid="+i+" a terminé, son resultat:");
						System.out.println(p.getCommand().result());
						activeProcesses.remove((Integer)i);
						pwd();
					}
				}*/
				error=false;
				String input=readInput();
				m=commandPattern.matcher(input);
				pwd=false;
				if(m.matches()){
					String args[] =parse(input);
					if(args[0].equals("pwd")){
						pwd();
						pwd=true;
					}	
					else{
						Command c=interpreter.command(args[0],args[1]);
						if(!error&&c!=null){
							c.setPID(processCount++);
							current=new ShellThread(c);
							//activeProcesses.put((Integer)c.getPID(),current);
							System.out.println(args[0]+" pid="+c.getPID());
							current.start();
							Thread.sleep(20);
						}
					}
				}
				else 
					showErrorMessage("Symbole illégale");
				if(!pwd&&(current==null||!current.isAlive()))
					pwd();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void notifyFinished(Command c, boolean error){
		if(!error){
			System.out.println("La commande pid="+c.getPID()+" a terminé, son resultat:");
			System.out.println(c.result());
			pwd();
			pwd=true;
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
