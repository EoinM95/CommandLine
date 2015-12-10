/**
 * Classe qui contient tous les commandes implementées et qui decide
 * à quelle commande l'entrée dans le terminal correspond
 * @author Eoin Murphy
 *
 */
public class Interpreter {
	private Shell sh;
	public Interpreter(Shell sh){
		this.sh=sh;
	}
	enum Commands{
		CD,LS, DATE;
		public Command command(String cmd, String args, Shell sh){
			switch(this){
			case CD:
				return new CD(args,sh);
			case LS:
				return new LS(sh);
			case DATE:
				return new CDate(args);
			default:
				return null;
			}
		}
	}
	public Command command(String cmd, String args){
		Command c=null;
		cmd=cmd.toUpperCase();
		for(Commands commands:Commands.values()){
			if(commands.toString().equals(cmd))
				c=commands.command(cmd,args,sh);
		}
		return c;		
	}
}
