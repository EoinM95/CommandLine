
public abstract class Command implements Runnable, OutPipeable{
	protected int pid;
	private String outputPath=null;
	public void setPID(int pid){
		this.pid=pid;
	}	
	public int getPID(){
		return pid;
	}
	/**
	 * @return outputPath
	 */
	public String getOutputPath() {
		return outputPath;
	}
	/**
	 * @param outputPath 
	 */
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

}
