/**
 * 
 * Si deux commandes peuvent communiquer l'entr�e et la sortie, alors utilisez
 * cette interface.
 * 
 * @author Eoin Murphy
 *
 */
public abstract interface Pipeable {
	public abstract String result();
}

interface OutPipeable extends Pipeable {
}

interface InPipeable {
	public abstract String input(String input);
}
