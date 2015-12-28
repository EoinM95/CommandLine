
public class PWD extends Command {
	private Shell sh;
	private String result;
	public PWD(Shell sh) {
		this.sh=sh;
	}

	@Override
	public void run() {
		result=sh.getDirectory().toString();
	}

	@Override
	public String result() {
		return result;
	}

}
