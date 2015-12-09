
public abstract interface Pipeable {
	public abstract String result();
}

interface OutPipeable extends Pipeable{}

interface InPipeable extends Pipeable{
	public abstract String input(String input);
}
