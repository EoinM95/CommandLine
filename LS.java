
public class LS extends Command implements OutPipeable {
	private Shell sh;
	private String result;
	public LS(Shell sh){
		this.sh=sh;
		result="";
	}
	@Override
	public void run() {
		String files[]=sh.getDirectory().list();
		for(int i=0;i<files.length;i++){
			System.out.println(files[i]);
			result+=files[i]+"\n";
		}	
	}
	@Override
	public String result() {
		return result;
	}

}
