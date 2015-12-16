/**
 * Une interface qui peut permet à Shell de decider si deux commandes peut accpeter
 * l'entrée/sortie 
 * @author Eoin Murphy
 *
 */
public abstract interface Pipeable {
	public abstract String result();
}

interface OutPipeable extends Pipeable{}

interface InPipeable{
	public abstract String input(String input);
}
