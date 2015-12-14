
public abstract class Command implements Runnable {
	protected int pid;
	public void setPID(int pid){
		this.pid=pid;
	}	
	public int getPID(){
		return pid;
	}
}
