package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    private ABB<Usuario> usuarios;

    public AirMiles() {
        this.usuarios = new ABB<>();
    }

    public void registrarUsuario(int id) {
        if (usuarios.buscarUsuarioPorID(id)!=null){
            throw new UnsupportedOperationException("el usuario ya pertenece al sistema");
        }
        Usuario u = new Usuario(id);  
        usuarios.insertar(u);
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        if (usuarios.buscarUsuarioPorID(id)!=null){
            throw new UnsupportedOperationException("el usuario ya pertenece al sistema");
        }
        Usuario u = new Usuario(id);
        Usuario referidor = usuarios.buscarUsuarioPorID(ref); 
        if (referidor == null){
            throw new UnsupportedOperationException("el referidor no pertenece al sistema");
        }
        u.millas_disponibles += 500;
        u.millas_totales += 500;  
        usuarios.insertar(u);
        referidor.millas_disponibles += 500;
        referidor.millas_totales += 500;
        // como las constantes no cuentan para la complejidad puedo recorrer el arbol 3 veces sin romper la consigna

    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void canjearMillas(int id, double cantidad) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public double consultarSaldo(int id) {
        Usuario u = usuarios.buscarUsuarioPorID(id);
        return u.millas_disponibles;
    }

    public String consultarCategoria(int id) {
       throw new UnsupportedOperationException("Sin implementar");
    }

    public int[] rankingMillas(int n) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void iniciarPromocion(String nombre, double factor) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void finalizarPromocion(String nombre) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public int elMasAcumulador() {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public int elMasCanjeador() {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public int[][] paresTransferidoresExclusivos() {
        throw new UnsupportedOperationException("Sin implementar");
    }
}




