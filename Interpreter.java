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
		cd,ls, date,find,compteJusqua, grep,pwd;
		public Command command(String cmd, String args, Shell sh){
			switch(this){
			case cd:
				return new CD(args,sh);
			case ls:
				return new LS(sh);
			case date:
				return new CDate(args,sh);
			case find:
				return new Find(args,sh);
			case compteJusqua:
				return new Compte(args,sh);
			case grep:
				return new Grep(args,sh);
			case pwd:
				return new PWD(sh);
			default:
				return null;
			}
		}
	}
	public Command command(String cmd, String args){
		Command c=null;
		for(Commands com:Commands.values()){
			if(com.toString().equals(cmd))
				c=com.command(cmd,args,sh);
		}
		if(c==null)
			sh.showErrorMessage("Commande: "+cmd+" pas trouvée");
		return c;		
	}
}
