
public abstract class Command implements Runnable, OutPipeable{
	protected int pid;
	public void setPID(int pid){
		this.pid=pid;
	}	
	public int getPID(){
		return pid;
	}
	
}
