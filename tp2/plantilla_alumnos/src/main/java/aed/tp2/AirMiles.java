package aed.tp2;
import java.util.ArrayList;

public class AirMiles implements SistemaAirMiles {
    private ABB<Usuario> usuarios;
    private Trie<Promo> promociones;
    private HeapMax<Usuario> ranking;
    private double maxMillasGastadas;
    private int  idMayorCanjeador;
    private double maxMillasTotales;
    private int idMasMillasTotales;

    public AirMiles() {
        this.usuarios = new ABB<>();
        this.promociones = new Trie<Promo>();
        Promo predeterminada = new Promo(1.0);
        this.promociones.insertar("NoPromo", predeterminada);
        ComparadorUsuarios comparador = new ComparadorUsuarios();
        ranking = new HeapMax<>(comparador);
        this.maxMillasGastadas = 0;
        this.idMayorCanjeador = -1;
        this.idMasMillasTotales=-1;
        this.maxMillasTotales=-1;//para indicar que todavia no hay ninguno registrado
    }

    public void registrarUsuario(int id) {
        
        Usuario u = new Usuario(id);  
        usuarios.insertar(u);
        HandleHeap<Usuario> handle =new HandleHeap<Usuario>(u);//agrego estas 3 lineas para registrando en el heap
        handle.asignarHeap(ranking);
        ranking.insertar(handle);
        u.handleRanking=handle;
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        Usuario u = new Usuario(id);
        Usuario referidor = buscarUsuarioPorID(ref); 
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
        u.handleRanking=handle;
        ranking.actualizar(referidor.handleRanking);

        actualizarMaxMillasTotales(u);
        actualizarMaxMillasTotales(referidor);

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
        ranking.actualizar(u.handleRanking);
        actualizarMaxMillasTotales(u);
    }

    public void canjearMillas(int id, double cantidad) {
        Usuario usuario = buscarUsuarioPorID(id);
        if (usuario.millas_disponibles < cantidad) {
            return;
        }
        usuario.millas_disponibles -= cantidad;
        usuario.millas_gastadas += cantidad;

        if (usuario.millas_gastadas > maxMillasGastadas) {//me sirve para modificar los punteros q usa el mas canjeador
            maxMillasGastadas = usuario.millas_gastadas;
            idMayorCanjeador = usuario.identificador;
        }
        ranking.actualizar(usuario.handleRanking);
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        Usuario origenID = buscarUsuarioPorID (origen);
        Usuario destinoID = buscarUsuarioPorID(destino);

        if (origenID.millas_disponibles < cantidad){
            return;
        }
        origenID.millas_disponibles -= cantidad;
        origenID.millas_gastadas += cantidad;

        destinoID.millas_disponibles += cantidad;//tengo entendido que solo se modifican las disponibles
        
        if (origenID.millas_gastadas > maxMillasGastadas){//me actualiza los punteros del mas canjeador 
            maxMillasGastadas = origenID.millas_gastadas;
            idMayorCanjeador = origenID.identificador;

        }
        ranking.actualizar(origenID.handleRanking);//en estas dos lineas actualizo el heap ya que para uno aumenta y para el otro disminuye
        ranking.actualizar(destinoID.handleRanking);

        if (origenID.unicoUsuarioAlQTransfiere < 0) {//registro si los usuarios siempre transfieren a uno solo
            origenID.unicoUsuarioAlQTransfiere = destino;
        } else if (origenID.unicoUsuarioAlQTransfiere != destino) {
            origenID.transfirioExclusivamente = false;
        }

        if (destinoID.unicoUsuarioDelQRecibe < 0) {//registro si un usuario recibe siempre del mismo
            destinoID.unicoUsuarioDelQRecibe = origen;
        } else if (destinoID.unicoUsuarioDelQRecibe != origen) {
            destinoID.recibioExclusivamente = false;
        }
        
        actualizarExclusividad(origenID);
        actualizarExclusividad(destinoID);
        
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
        int[] res;
        if(ranking.cantDeElems() < n) {
            res = new int[ranking.cantDeElems()];
            
        } else {
            res = new int[n];
        }
        ArrayList<HandleHeap<Usuario>> guardadoTemporal = new ArrayList<>();
        for (int i=0; i<res.length; i+=1) {
            Usuario usuarioRanking = ranking.devolverMaximo();
            res[i] = usuarioRanking.identificador;
            HandleHeap<Usuario> handleDeUsuario = usuarioRanking.handleRanking;
            ranking.eliminarEn(0);//siempre elimino el primero para que avance al siguiente del rank
            guardadoTemporal.add(handleDeUsuario);
        }//voy sacando los handle del haep para luego volver a insertar en log n
        for (int i = 0; i < res.length; i+=1) {//vuelo a insertar los handle que saque en log n
            HandleHeap<Usuario> recuperoHandle = guardadoTemporal.get(i);
            recuperoHandle.asignarHeap(ranking);
            ranking.insertar(recuperoHandle);
        }

        return res;
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
        return idMasMillasTotales;
    }

    public int elMasCanjeador() {
        return idMayorCanjeador;
    }

    public int[][] paresTransferidoresExclusivos() {
        
        int[][] res;
        ArrayList<HandleHeap<Usuario>> arrayDeHandles =ranking.arrayDeRanking();
        Usuario[] arregloDeUsuarios = new Usuario[ranking.cantDeElems()];//me creo esta para poder acceder a todos los usuarios en O(U)
        
        //es este for obtengo los usuarios
        for(int i = 0; i<arregloDeUsuarios.length; i+=1) {
            Usuario usuario = arrayDeHandles.get(i).valor();
            arregloDeUsuarios[i] = usuario;
        }
        
        //aca cuento los pares que voy a tener
        int cantDePares =0;
        for (int i =0; i < arregloDeUsuarios.length; i+=1) {
            Usuario usuario =arregloDeUsuarios[i];
            if (usuario.esTransferidorExclusivo) {
                Usuario otro = usuario.obtenerUsuarioExclusivo();
                if(otro != null && otro.esTransferidorExclusivo &&
                    otro.obtenerUsuarioExclusivo().identificador == usuario.identificador &&
                    usuario.identificador< otro.identificador) {//para no agregar dos veces el mismo par
                        cantDePares +=1;
                    }
            }
        }

        //armo bien los pares y los pongo en res
        res = new int[cantDePares][2];
        int indice=0;
        for (int i=0; i<arregloDeUsuarios.length;i+=1) {
            Usuario usuario = arregloDeUsuarios[i];
            if (usuario.esTransferidorExclusivo) {
                Usuario otro =usuario.obtenerUsuarioExclusivo();
                if (otro != null && otro.esTransferidorExclusivo &&
                    otro.obtenerUsuarioExclusivo().identificador == usuario.identificador &&
                    usuario.identificador < otro.identificador) {
                    res[indice][0]= usuario.identificador;
                    res[indice][1]= otro.identificador;

                    indice+=1;
                }
            }
        }
            
        return res;
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
    private void actualizarExclusividad(Usuario u) {

        if (u.recibioExclusivamente && u.transfirioExclusivamente &&
            u.unicoUsuarioDelQRecibe != -1 && u.unicoUsuarioAlQTransfiere != -1 &&
            u.unicoUsuarioDelQRecibe == u.unicoUsuarioAlQTransfiere) {

            u.esTransferidorExclusivo = true;
            u.asignarUsuarioExclusivo(buscarUsuarioPorID(u.unicoUsuarioDelQRecibe));

        } else {

            u.esTransferidorExclusivo = false;
            u.asignarUsuarioExclusivo(null);
        }
    }

    private void actualizarMaxMillasTotales(Usuario u) {
        if (u.millas_totales > maxMillasTotales) {
            maxMillasTotales = u.millas_totales;
            idMasMillasTotales = u.identificador;
        }
    }

    
}




