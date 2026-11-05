package aed.tp2;
import java.util.Comparator;
public class ComparadorUsuarios implements Comparator<Usuario>{
    @Override 
    public int compare(Usuario u1, Usuario u2) {
        return Double.compare(u1.millas_disponibles, u2.millas_disponibles);
    }
}
