
public class Interpreter {
	private Shell sh;
	public Interpreter(Shell sh){
		this.sh=sh;
	}
	enum Commands{
		CD,LS;
		public Command command(String cmd, String args, Shell sh){
			switch(this){
			case CD:
				return new CD(args,sh);
			case LS:
				return new LS(sh);
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
