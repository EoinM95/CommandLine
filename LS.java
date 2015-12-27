
public class LS extends Command {
	private Shell sh;
	private String result=null;
	public LS(Shell sh){
		this.sh=sh;
		
	}
	@Override
	public void run() {
		result="";
		String files[]=sh.getDirectory().list();
		for(int i=0;i<files.length;i++){
			result+=files[i];
			if(i<files.length-1)
				result+="\n";
		}	
	}
	@Override
	public String result() {
		if(result==null)
			return null;
		return result;
	}

}
