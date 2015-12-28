
public class Pipe extends Command implements Backgroundable {
	private Interpreter interpreter;
	private Shell sh;
	private String[] commands;
	private String result;
	private boolean background=false;
	private boolean dead=false;
	public Pipe(Shell sh, String[] commands){
		this.sh=sh;
		this.commands=commands;
		interpreter=new Interpreter(sh);
	}
	
	@Override
	public void run() {
		String previousResult="";
		Command nextCommand=null;
		for(int i=0;i<commands.length&&!dead;i++){
			String[] args = sh.parse(commands[i]);
			if(i==0)
				nextCommand=interpreter.command(args[0],args[1]);
			else{
				nextCommand=interpreter.command(args[0],args[1]+" "+previousResult);
				if(nextCommand instanceof Pipeable) 
					((Pipeable)nextCommand).setPiped(true);
			}	
			if(nextCommand==null){
				if(background)
					sh.notifyFinished(this,true);
				break;
			}
			else if(nextCommand instanceof Pipeable ||i==0){
				nextCommand.run();
				previousResult=nextCommand.result();
			}
			else{
				sh.showErrorMessage("Commande "+args[0]+" ne peut pas etre <<Piped>>");
				if(background)
					sh.notifyFinished(this,true);
				break;
			}
		}
		if(nextCommand!=null)
			result=nextCommand.result();
		if(background)
			sh.notifyFinished(this,false);
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

}
