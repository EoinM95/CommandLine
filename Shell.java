import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
			if(c instanceof Backgroundable){
				((Backgroundable)c).kill();
			}
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
	public static final String NEW_LINE=System.getProperty("line.separator");
	private Interpreter interpreter;
	private File currentDirectory;
	private boolean error;
	private final Pattern linePattern;
	private Matcher m;
	private Hashtable<Integer,ShellThread> backgroundProcesses;
	private int processCount=0;
	private boolean pwd;
	public Shell(){//(?<command>[a-zA-Z]+) ?(?<args>[^&>|]*)
		String commandFormat="(([a-zA-Z]+) ?([^&>|]*))";
		linePattern=Pattern.compile(commandFormat+"( ?[|] ?"+commandFormat+" ?)*( (>> (?<outputPath>[^&]*)))?( ?&)?");
		interpreter=new Interpreter(this);
		backgroundProcesses=new Hashtable<Integer,ShellThread>();
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
	
	public void killCommand(int pid){
		ShellThread c=backgroundProcesses.get((Integer)pid);
		if(c!=null&&c.isAlive())
			c.kill();
		else
			System.out.println("Commade dèja terminé");
		backgroundProcesses.remove((Integer)pid);
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
				error=false;
				String input=readInput();
				m=linePattern.matcher(input);
				pwd=false;
				if(m.matches()){
					String args[]=parse(input);
					if(args[0].equals("pwd")){
						pwd();
						pwd=true;
					}
					else if(args[0].equals("kill")){
						try{
							int pid=Integer.parseInt(args[1]);
							killCommand(pid);
						}
						catch(NumberFormatException e){
							showErrorMessage("Argument invalide pour la commande kill");
						}
					}
					else{
						String outputPath=m.group("outputPath");
						Command c;
						if(input.contains("|"))
							c=new Pipe(this,input.split("\\|"));
						else
							c=interpreter.command(args[0],args[1]);
						c.setOutputPath(outputPath);
						if(!error&&c!=null){
							if(input.endsWith("&")&&c instanceof Backgroundable){
								current=new ShellThread(c);
								c.setPID(processCount++);
								((Backgroundable)c).setBackground(true);
								System.out.println(args[0]+" pid="+c.getPID()+" commencé");
								backgroundProcesses.put(c.getPID(),current);
								pwd();
								pwd=true;
								current.start();
							}
							else{
								c.run();
								String result=c.result();
								if(outputPath==null||outputPath.equals("")){								
									if(result!=null)
										System.out.println(result);
								}
								else{
									writeOutput(result,outputPath);
								}
							}	
							
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
	/**
	 * Quand une commande a fini cette méthode
	 * permet au Shell de savoir
	 * @param c, la commande qui a terminé
	 * @param error, true si'il y avait une erreur
	 */
	public void notifyFinished(Command c, boolean error){
		if(!error){
			System.out.println("La commande pid="+c.getPID()+" a terminé");
			String outputPath=c.getOutputPath();
			String result=c.result();
			boolean print=true;
			if(!(outputPath==null))
				if(writeOutput(result,outputPath)){
					print=false;
				}
			if(print){
				System.out.println("Resultat du commande:");
				System.out.println(result);
			}
			pwd();
			pwd=true;
		}
		backgroundProcesses.remove((Integer)c.getPID());
	}
	
	private boolean writeOutput(String output, String path){
		if(output==null||path==null)
			return false;
		output=output.replaceAll("\\n",NEW_LINE);
		File outputFile;
		PrintWriter outputStream=null;
		if(CD.isAbsolutePath(path))
			outputFile=new File(path);
		else
			outputFile=new File(currentDirectory.toString()+CD.seperator+path);
		try{
			if(outputFile.exists()||outputFile.createNewFile()){
				outputStream = new PrintWriter(new FileWriter(outputFile)) ;
				outputStream.print(output);
			}
			else{
				showErrorMessage("Nom du fichier pas valide");
				return false;
			}
		}
		catch (IOException e) {
			//showErrorMessage("Erreur IO");
			e.printStackTrace();
			return false;
		}
		if(outputStream!=null)
			outputStream.close();
		return true;
	}
	
	public String[] parse(String input) {
		String[] parsed= new String[2];
		Pattern commandPattern =Pattern.compile("(?<command>[a-zA-Z]+) ?(?<args>[^&>|]*) ?");
		Matcher commandMatcher=commandPattern.matcher(input);
		if(commandMatcher.find()){
			parsed[0]=commandMatcher.group("command");
			String args=commandMatcher.group("args");
			if(args.endsWith(" "))
				args=args.substring(0,args.length()-2);
			parsed[1]=args;
		}
		else
			showErrorMessage("Erreur du parsing");
		return parsed;
	}

	private String readInput() {
		Scanner scanner=new Scanner(System.in);
		String input = scanner.nextLine();
		return input;
	}
	
	
	
	/**
	 * Si dans l'execution d'une commande on reconte une erreur
	 * cette methode nous permet d'afficher une message et 
	 * arreter l'execution
	 * @param message
	 */
	public void showErrorMessage(String message){
		error=true;
		System.out.println("Erreur: "+message);
	}
	
	public static void main(String[] args){//Juste pour tester
		Shell s = new Shell();
		s.run();
	}
	
}
