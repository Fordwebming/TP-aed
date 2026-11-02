package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    private BaseUsuarios usuarios;

    public AirMiles() {
        this.usuarios = new BaseUsuarios();
    }

    public void registrarUsuario(int id) {
        Usuario u = new Usuario(id);  
        usuarios.insertar(u);
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        Usuario u = new Usuario(id);
        u.millas_disponibles += 500;
        u.millas_totales += 500;  
        usuarios.insertar(u);
        Usuario referidor = usuarios.buscarUsuarioPorID(ref); //estoy asumiendo que el referidor existe
        referidor.millas_disponibles += 500;
        referidor.millas_totales += 500;

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




