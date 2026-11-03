package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    private BaseUsuarios usuarios;
    private int idMayorCanjeador;
    private double maxMillasGastadas;
    //public double millas_totales; ->esta me sirve par la categoria , funca sin transferencias, solo millas por vuelos y ponele q con bonos creo , esta solo CRECE
    //public double millas_disponibles;-> lo uso pa los rankins tambien almacena las millas recibias e historicas, millas para gastar transferis/canjear , VAria
    //public double millas_gastadas;
    // defini estas variables en usuario son las q use/ claro cada una empeiza en 0

    
    public AirMiles() {
        this.usuarios = new BaseUsuarios();
        this.idMayorCanjeador = -1;
        this.maxMillasGastadas = 0;
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
        //asumoq  el usuario existe y la distancia siempre es mayor a 0
        Usuario usuario = usuarios.buscarUsuarioPorID(id);
        if (usuario.millas_disponibles < cantidad) {
        return;
        }
        usuario.millas_disponibles -= cantidad;
        usuario.millas_gastadas += cantidad;

        //defini   public Handle<Usuario> lugarEnRanking; en usuario para representar las posicion del user en el ranking de millasdisponibl(maxheap)
        // tengo entendido q ordena segunlas millas disponible spara el rankin millas
        // me sirve para modificar el saldo y reordenar el rankin 
        Handle<Usuario> user = usuario.lugarEnRanking;//para modificar el ranking xq le bajaron las millas, queda pendiente por ver
        if (user !=null){
            user.modificar(usuario);
        }

        if (usuario.millas_gastadas > maxMillasGastadas) {//me sirve para modificar los punteros q usa el mas canjeador
            maxMillasGastadas = usuario.millas_gastadas;
            idMayorCanjeador = usuario.identificador;
            }
        }

    public void transferirMillas(int origen, int destino, double cantidad) {
        //asumo q cantidad mayor a 0 y q son usuarios distintos y  q ambos existen
        //falta ver como implemetar la lista q vincula a los q se trasnfieren entre si
        Usuario origenID = usuarios.buscarUsuarioPorID (origen);
        Usuario destinoID = usuarios.buscarUsuarioPorID(destino);

        if (origenID.millas_disponibles < cantidad){
            return;
        }
        origenID.millas_disponibles -= cantidad;
        origenID.millas_gastadas += cantidad;

        destinoID.millas_disponibles += cantidad;//tengo entendido que solo se modifican las disponibles

        // mismo razonamiento q con canjear millas, lo modifico en el ranking
        Handle<Usuario> elOrigen = origenID.lugarEnRanking;//baja en el ranking
        if (elOrigen!=null){
            elOrigen.modificar(origenID);
        }
        //hola , aca tambien se mueve en el ranking
        Handle<Usuario> elDestino = destinoID.lugarEnRanking;//sube en el ranking
        if (elDestino!=null){
            elDestino.modificar(destinoID);
        }

        if (origenID.millas_gastadas > maxMillasGastadas){//me actualiza los punteros del mas canjeador 
            maxMillasGastadas = origenID.millas_gastadas;
            idMayorCanjeador = origenID.identificador;

        }
    }

    public double consultarSaldo(int id) {
        Usuario u = usuarios.buscarUsuarioPorID(id);
        return u.millas_disponibles;
    }

    public String consultarCategoria(int id) {
        // siempre se asume q el user existe
       Usuario user =  usuarios.buscarUsuarioPorID(id);

       double totales = user.millas_totales;

       if (totales >= 1000000){
        return "Platino";
       }else if (totales >= 50000){
        return "Oro";
       }else if (totales >= 10000){
        return "Plata";
       }else {
        return "Bronce";
       }

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
