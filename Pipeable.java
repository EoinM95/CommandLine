/**
 * Une interface qui peut permet à Shell de decider si deux commandes peut accpeter
 * l'entrée/sortie 
 * @author Eoin Murphy
 *
 */
public interface Pipeable {
	public void setPiped(boolean piped);
}

