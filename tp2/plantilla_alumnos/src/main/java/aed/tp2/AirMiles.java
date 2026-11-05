package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    private ABB<Usuario> usuarios;
    private Trie<Promo> promociones;
    private HeapMax<Usuario> ranking;

    public AirMiles() {
        this.usuarios = new ABB<>();
        this.promociones = new Trie<Promo>();
        Promo predeterminada = new Promo(1.0);
        this.promociones.insertar("NoPromo", predeterminada);
        ComparadorUsuarios comparador = new ComparadorUsuarios();
        ranking = new HeapMax<>(comparador);
    }

    public void registrarUsuario(int id) {
        if (buscarUsuarioPorID(id)!=null){
            throw new UnsupportedOperationException("el usuario ya pertenece al sistema");   // arreglar esto 
        }
        Usuario u = new Usuario(id);  
        usuarios.insertar(u);
        HandleHeap<Usuario> handle =new HandleHeap<Usuario>(u);//agrego estas 3 lineas para registrando en el heap
        handle.asignarHeap(ranking);
        ranking.insertar(handle);
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        if (buscarUsuarioPorID(id)!=null){
            throw new UnsupportedOperationException("el usuario ya pertenece al sistema");    // arreglar esto 
        }
        Usuario u = new Usuario(id);
        Usuario referidor = buscarUsuarioPorID(ref); 
        if (referidor == null){
            throw new UnsupportedOperationException("el referidor no pertenece al sistema");
        }
        u.millas_disponibles += 500;
        u.millas_totales += 500;  
        usuarios.insertar(u);
        referidor.millas_disponibles += 500;
        referidor.millas_totales += 500;
        // como las constantes no cuentan para la complejidad puedo recorrer el arbol 3 veces sin romper la consigna
        //agrego las mismas lineas que en registrar usuario
        HandleHeap<Usuario> handle =new HandleHeap<Usuario>(u);
        handle.asignarHeap(ranking);
        ranking.insertar(handle);

    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        Usuario u = buscarUsuarioPorID(id);
        Promo p = promociones.obtenerValor(nombrePromo);
        double factorPromocion = factorPromo(nombrePromo);
        String categoriaU = consultarCategoria(id);
        double factorCatU = factorCategoria(categoriaU);

        double millasGanadas = distancia*factorCatU*factorPromocion;

        u.millas_totales += millasGanadas;
        u.millas_disponibles += millasGanadas;

        if (p != null && p.tope != 0){   // => esPromoConTope
            p.acumulacionMillas += millasGanadas;
            if (p.acumulacionMillas >= p.tope) {
                promociones.eliminar(nombrePromo);
            }
        }
    }

    public void canjearMillas(int id, double cantidad) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public double consultarSaldo(int id) {
        Usuario u = buscarUsuarioPorID(id);
        return u.millas_disponibles;
    }

    public String consultarCategoria(int id) {
        Usuario u= buscarUsuarioPorID(id);
        double totales = u.millas_totales;
        String res = "Platino";
        if (totales < 10000){
            res = "Bronce";
        } if (10000 <= totales && totales < 50000){
            res = "Plata";
        } if(50000 <= totales && totales < 1000000){
            res = "Oro";
        }
        return res; 
    }

    public int[] rankingMillas(int n) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void iniciarPromocion(String nombre, double factor) {
        Promo nuevaPromo = new Promo(factor);
        promociones.insertar(nombre, nuevaPromo);
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        Promo nuevaPromo = new Promo(factor, tope);
        promociones.insertar(nombre, nuevaPromo);
    }

    public void finalizarPromocion(String nombre) {
        promociones.eliminar(nombre); 
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

    //metodos auxiliares
    public Usuario buscarUsuarioPorID(int u){
        ABB<Usuario>.Nodo raiz_actual = usuarios.raiz;
        while (raiz_actual != null && raiz_actual.usuario.identificador != u ){
            if (raiz_actual.usuario.identificador < u){ //busco a la derecha
                raiz_actual = raiz_actual.derecha;
            }
            else{
                raiz_actual = raiz_actual.izquierda; //busco a la izquierda
            }
        }
        //si el usuario no existe devuelve null, asi que tambien sirve como un metodo para saber si pertenece un usuario
        if (raiz_actual == null){
            return null;
        }
        else{
            return raiz_actual.usuario;
        }
    }

    public double factorCategoria(String categoria){
        double res = 0;   //Platino 
        if (categoria == "Bronce"){
            res = 1.0;
        } if(categoria == "Plata"){
            res = 1.5;
        } if(categoria == "Oro"){
            res = 2.0;
        }
        return res; 
    }

    public double factorPromo(String promo){
        Promo p = promociones.obtenerValor(promo);
        if (p==null){
            return 1.0; // la promo no esta activa o alcanzo el tope
        }
        return p.factor;
    }
    
}




