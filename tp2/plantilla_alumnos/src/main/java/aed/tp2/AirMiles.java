package aed.tp2;
import java.util.ArrayList;

public class AirMiles implements SistemaAirMiles {
    private ABB<Usuario> usuarios;
    private Trie<Promo> promociones;
    private HeapMax<Usuario> ranking;
    
    //aca voy probando para cumplir la correccion
    private Usuario mayorCanjeador;
    private Usuario mayorAcumulador;

    public AirMiles() {
        this.usuarios = new ABB<>();
        this.promociones = new Trie<Promo>();
        Promo predeterminada = new Promo(1.0);
        this.promociones.insertar("NoPromo", predeterminada);
        
        ranking = new HeapMax<>();
        
        //agrago para cumplir la correccion
        this.mayorAcumulador=null;
        this.mayorCanjeador = null;
    }

    public void registrarUsuario(int id) {
        if (buscarUsuarioPorID(id) != null) {
            throw new IllegalArgumentException("Usuario ya registrado");
        }

        Usuario u = new Usuario(id);  
        usuarios.insertar(u);
        HandleUsuario<Usuario> handle =new HandleUsuario<Usuario>(u);//agrego estas 3 lineas para registrando en el heap
        handle.asignarHeap(ranking);
        ranking.insertar(handle);
        u.asignarHandle(handle);
        actualizarMayorAcumulador(u);
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        if (buscarUsuarioPorID(id) != null) {
            throw new IllegalArgumentException("Usuario ya registrado");
        }
        if (id == ref) {
            throw new IllegalArgumentException("Un usuario no puede ser su propio referidor");
        }
        Usuario referidor = buscarUsuarioPorID(ref);
        if (referidor == null) {
            throw new IllegalArgumentException("NO existe el Referidor ");
        }
        Usuario u = new Usuario(id);
        u.aumentarDisponibles(500);
        u.aumentarTotales(500);
        usuarios.insertar(u);
        referidor.aumentarDisponibles(500);
        referidor.aumentarTotales(500);
        // como las constantes no cuentan para la complejidad puedo recorrer el arbol 3 veces sin romper la consigna
        //agrego las mismas lineas que en registrar usuario
        ranking.actualizar(referidor.handleRanking);
        HandleUsuario<Usuario> handle =new HandleUsuario<Usuario>(u);
        handle.asignarHeap(ranking);
        ranking.insertar(handle);
        u.asignarHandle(handle);

        actualizarMayorAcumulador(u);
        actualizarMayorAcumulador(referidor);

    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        if (distancia < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa");
        }
        if (nombrePromo == null) {
            throw new IllegalArgumentException("La promocion debe existir");
        }
        Usuario u = buscarUsuarioPorID(id);
        if (u == null) {
            throw new IllegalArgumentException("Usuario no existe");
        }
        Promo p = promociones.obtenerValor(nombrePromo);
        double factorPromocion = factorPromo(nombrePromo);
        String categoriaU = consultarCategoria(id);
        double factorCatU = factorCategoria(categoriaU);

        double millasGanadas = distancia*factorCatU*factorPromocion;

        u.aumentarTotales(millasGanadas);
        u.aumentarDisponibles(millasGanadas);

        if (p != null && p.tope != 0){   // => esPromoConTope
            p.acumulacionMillas += millasGanadas;
            if (p.acumulacionMillas >= p.tope) {
                promociones.eliminar(nombrePromo);
            }
        }
        ranking.actualizar(u.handleRanking);
        actualizarMayorAcumulador(u);
    }

    public void canjearMillas(int id, double cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a canjear no puede ser negativa");
        }
        Usuario usuario = buscarUsuarioPorID(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no existe");
        }
        if (usuario.obtenerMillasDisponibles() < cantidad) {
            return;
        }
        usuario.disminuirDisponibles(cantidad);
        usuario.aumentarGastadas(cantidad);

        actualizarMayorCanjeador(usuario);
        ranking.actualizar(usuario.handleRanking);
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a transferir no puede ser negativa" );
        }
        if (origen == destino) {
            throw new IllegalArgumentException("Origen y destino no pueden ser el mismo usuario");
        }
        Usuario origenID = buscarUsuarioPorID (origen);
        if (origenID == null) {
            throw new IllegalArgumentException("Usuario origen inexistente");
        }
        Usuario destinoID = buscarUsuarioPorID(destino);        
        if (destinoID == null) {
            throw new IllegalArgumentException("Usuario destino inexistente");
        }

        if (origenID.obtenerMillasDisponibles() < cantidad){
            return;
        }
        origenID.disminuirDisponibles(cantidad);
        origenID.aumentarGastadas(cantidad);

        destinoID.aumentarDisponibles(cantidad);//tengo entendido que solo se modifican las disponibles
        
        actualizarMayorCanjeador(origenID);

        ranking.actualizar(origenID.handleRanking);//en estas dos lineas actualizo el heap ya que para uno aumenta y para el otro disminuye
        ranking.actualizar(destinoID.handleRanking);

        origenID.registrarTransferenciaConUsuario(destinoID);
        
        origenID.actualizarExclusividad(destinoID);
        destinoID.actualizarExclusividad(origenID);
        
    }

    public double consultarSaldo(int id) {
        Usuario u = buscarUsuarioPorID(id);
        if (u == null) {
            throw new IllegalArgumentException("Usuario inexistente");
        }
        return u.obtenerMillasDisponibles();
    }

    public String consultarCategoria(int id) {
        Usuario u= buscarUsuarioPorID(id);
        if (u == null) {
            throw new IllegalArgumentException("Usuario inexistente" );
        }
        double totales = u.obtenerMillasTotales();
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
        if (n < 0) {
            throw new IllegalArgumentException("El valor no puede ser negativo");
        }
        int[] res;
        if(ranking.cantDeElems() < n) {
            res = new int[ranking.cantDeElems()];
            
        } else {
            res = new int[n];
        }
        ArrayList<HandleUsuario<Usuario>> guardadoTemporal = new ArrayList<>();
        for (int i=0; i<res.length; i+=1) {
            Usuario usuarioRanking = ranking.devolverMaximo();
            res[i] = usuarioRanking.obtenerIdentificador();
            HandleUsuario<Usuario> handleDeUsuario = usuarioRanking.handleRanking;
            ranking.eliminarEn(0);//siempre elimino el primero para que avance al siguiente del rank
            guardadoTemporal.add(handleDeUsuario);
        }//voy sacando los handle del haep para luego volver a insertar en log n
        for (int i = 0; i < res.length; i+=1) {//vuelo a insertar los handle que saque en log n
            HandleUsuario<Usuario> recuperoHandle = guardadoTemporal.get(i);
            recuperoHandle.asignarHeap(ranking);
            ranking.insertar(recuperoHandle);
        }

        return res;
    }
    
    private void validarNombrePromo(String nombre) {
    if (nombre == null || nombre.isEmpty()) {
        throw new IllegalArgumentException("El nombre de la promoción no puede no existir ni estar vacío");
    }}
        // creo q aca deberia poenrse algo en el caso de q se le metan letras de otro alfabeto/idioma, creo es es tipo
        //!nombre.matches masomenos, pero no se como funciona

    public void iniciarPromocion(String nombre, double factor) {
        validarNombrePromo(nombre);
        if ("NoPromo".equals(nombre)) {
            throw new IllegalArgumentException("'NoPromo' es una promoción reservada del sistema");
        }
        if (factor < 0) {
            throw new IllegalArgumentException("El factor no puede ser negativo");
        }
        Promo nuevaPromo = new Promo(factor);
        promociones.insertar(nombre, nuevaPromo);
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        validarNombrePromo(nombre);
        if ("NoPromo".equals(nombre)) {
            throw new IllegalArgumentException("'NoPromo' es una promoción reservada del sistema");
        }
        if (factor < 0) {
            throw new IllegalArgumentException("El factor no puede ser negativo");
        }
        if (tope <= 0) {
            throw new IllegalArgumentException("El tope debe ser positivo");
        }
        Promo nuevaPromo = new Promo(factor, tope);
        promociones.insertar(nombre, nuevaPromo);
    }

    public void finalizarPromocion(String nombre) {
    if ("NoPromo".equals(nombre)) {
        throw new IllegalArgumentException("No se puede finalizar la promoción predeterminada 'NoPromo'");}
    
        promociones.eliminar(nombre); 
    }

    public int elMasAcumulador() {
        if (mayorAcumulador == null) {
            throw new IllegalStateException("No hay usuarios registrados en el sistema");
        }
        return mayorAcumulador.obtenerIdentificador();
    }

    public int elMasCanjeador() {
        if (mayorCanjeador == null) {
            throw new IllegalStateException("No hay usuarios con canjes/transferencias registradas");
        }
        return mayorCanjeador.obtenerIdentificador();
    }

    public int[][] paresTransferidoresExclusivos() {
        
        int[][] res;
        ArrayList<HandleUsuario<Usuario>> arrayDeHandles =ranking.arrayDeRanking();
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
                    otro.obtenerUsuarioExclusivo().obtenerIdentificador() == usuario.obtenerIdentificador() &&
                    usuario.obtenerIdentificador()< otro.obtenerIdentificador()) {//para no agregar dos veces el mismo par
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
                    otro.obtenerUsuarioExclusivo().obtenerIdentificador() == usuario.obtenerIdentificador() &&
                    usuario.obtenerIdentificador() < otro.obtenerIdentificador()) {
                    res[indice][0]= usuario.obtenerIdentificador();
                    res[indice][1]= otro.obtenerIdentificador();

                    indice+=1;
                }
            }
        }
            
        return res;
    }

    //metodos auxiliares
    public Usuario buscarUsuarioPorID(int u){
        ABB<Usuario>.Nodo raiz_actual = usuarios.raiz;
        while (raiz_actual != null && raiz_actual.usuario.obtenerIdentificador() != u ){
            if (raiz_actual.usuario.obtenerIdentificador() < u){ //busco a la derecha
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
            (u.unicoUsuarioDelQRecibe != -1 || u.unicoUsuarioAlQTransfiere != -1)) {

            int idExclusivo;

            if (u.unicoUsuarioDelQRecibe != -1) {
                idExclusivo = u.unicoUsuarioDelQRecibe;
            } else {
                idExclusivo = u.unicoUsuarioAlQTransfiere;
            }

            if (u.unicoUsuarioDelQRecibe != -1 && u.unicoUsuarioAlQTransfiere != -1 &&
                u.unicoUsuarioDelQRecibe != u.unicoUsuarioAlQTransfiere) {

                u.esTransferidorExclusivo = false;
                u.asignarUsuarioExclusivo(null);

            } else {
                u.esTransferidorExclusivo = true;
                u.asignarUsuarioExclusivo(buscarUsuarioPorID(idExclusivo));
            }

        } else {
            u.esTransferidorExclusivo = false;
            u.asignarUsuarioExclusivo(null);
        }
    }

    private void actualizarMayorAcumulador(Usuario u) {
        if (mayorAcumulador==null) {
            mayorAcumulador=u;
        } else if (u.obtenerMillasTotales() > mayorAcumulador.obtenerMillasTotales()) {
            mayorAcumulador = u;
        }
    }

    private void actualizarMayorCanjeador(Usuario u) {
        if (mayorCanjeador==null) {
            mayorCanjeador=u;
        } else if (u.obtenerMillasGastadas() > mayorCanjeador.obtenerMillasGastadas()) {
            mayorCanjeador = u;
        }
    }
    
}




