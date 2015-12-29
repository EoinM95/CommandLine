
public abstract class Command implements Runnable{
	protected int pid;
	private String outputPath=null;
	private boolean overwrite=false;
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
	
	public abstract String result();
	/**
	 * @return overwrite
	 */
	public boolean canOverwrite() {
		return overwrite;
	}
	/**
	 * @param overwrite
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
	
}
