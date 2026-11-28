package aed.tp2;

public class Usuario implements Comparable<Usuario> {
    private  int identificador;  //los atributos son publicos para poder usarlos en la clase nodo del ABB de usuarios
    private double millas_totales;
    private  double millas_disponibles;
    private double millas_gastadas;
    private HandleUsuario<Usuario> handleRanking;
    private boolean recibioExclusivamente;
    private  boolean transfirioExclusivamente;
    private  int unicoUsuarioDelQRecibe;
    private int unicoUsuarioAlQTransfiere;
    private boolean esTransferidorExclusivo;
    private Usuario usuarioExclusivo;


    public Usuario(int identificador) {
        this.identificador = identificador;
        this.millas_totales = 0;
        this.millas_disponibles = 0;
        this.millas_gastadas = 0;
        handleRanking = null;
        usuarioExclusivo=null;

        recibioExclusivamente = true;
        transfirioExclusivamente = true;
        unicoUsuarioDelQRecibe = -1;
        unicoUsuarioAlQTransfiere = -1;
        esTransferidorExclusivo = false;
    }
    @Override 
    public int compareTo(Usuario otro){
        return Double.compare(this.millas_disponibles, otro.millas_disponibles);
    }

    public boolean recibioSiempreDelMismo() {
        return recibioExclusivamente;
    }

    public void cambiaValorRecibioExclusivamente(boolean b) {
        this.recibioExclusivamente = b;
    }

    public Usuario obtenerUsuarioExclusivo() {
        return usuarioExclusivo;
    }

    public void asignarUsuarioExclusivo(Usuario u) {
        usuarioExclusivo=u;
    }

    public int idUnicoUsuarioDelQueRecibe() {
        return unicoUsuarioDelQRecibe;
    }

    public void asignarIdUnicoUsuarioDelQRecibe(int id) {
        this.unicoUsuarioDelQRecibe = id;
    }

    public double obtenerMillasDisponibles() {
        return millas_disponibles;
    }

    public double obtenerMillasTotales() {
        return millas_totales;
    }

    public double obtenerMillasGastadas() {
        return millas_gastadas;
    }

    public int obtenerIdentificador() {
        return identificador;
    }

    public void aumentarTotales(double x) {
        this.millas_totales += x;
    }

    public void aumentarDisponibles(double x) {
        this.millas_disponibles += x;
    }

    public void aumentarGastadas(double x) {
        this.millas_gastadas += x;
    }

    public void disminuirDisponibles(double x) {
        this.millas_disponibles -= x;
    }

    public boolean esExclusivo() {
        return esTransferidorExclusivo;
    }

    public HandleUsuario<Usuario> obtenerHandle() {
        return handleRanking;
    }

    public void asignarHandle(HandleUsuario<Usuario> h) {
        this.handleRanking = h;
    }

    public void registrarTransferenciaConUsuario(Usuario otro) {
        //
        if (this.unicoUsuarioAlQTransfiere < 0) {//registro si los usuarios siempre transfieren a uno solo
            this.unicoUsuarioAlQTransfiere = otro.obtenerIdentificador();
        } else if (this.unicoUsuarioAlQTransfiere != otro.obtenerIdentificador()) {
            this.transfirioExclusivamente = false;
        }

        //verifico exclusividad despues de algun movimiento con otro usuario
        if (otro.idUnicoUsuarioDelQueRecibe() < 0) {//registro si un usuario recibe siempre del mismo
            otro.asignarIdUnicoUsuarioDelQRecibe(this.identificador);
        } else if (otro.idUnicoUsuarioDelQueRecibe() != this.identificador) {
            otro.cambiaValorRecibioExclusivamente(false);
        }  
    }

    public void actualizarExclusividad(Usuario otro) {
        this.esTransferidorExclusivo = this.transfirioExclusivamente && 
        this.recibioExclusivamente &&
        (this.unicoUsuarioAlQTransfiere == otro.obtenerIdentificador() || this.unicoUsuarioAlQTransfiere==-1)&&
        (this.unicoUsuarioDelQRecibe == otro.obtenerIdentificador() || this.unicoUsuarioDelQRecibe==-1);

        if (this.esTransferidorExclusivo) {
            this.asignarUsuarioExclusivo(otro);
        } else {
            this.asignarUsuarioExclusivo(null);
        }
    }
}



/*Las estructuras que quedaron son:
Un ABB de usuarios ordenador por identificador
Un Max heap para ranking millas donde la raíz es el que tiene más millas, 
se ordena por cantidad de millas y lo que guarda cada nodo es un handle que vincula los usuarios
del ABB con el heap para que al cambiar una estructura se cambie la otra automáticamente y
no nos queden datos sin actualizar
Para las promociones va a ser un Trie 
Va a haber una clase Usuario 
donde le vamos a dar atributos y métodos para tener órdenes más simples
Promoción también va a ser una clase en donde ahí mismo se va a declarar con atributos de es con tope o sin tope
Para el más canjeador y el más acumulador tenemos que acceder en O(1) entonces nos lo guardamos con atributos en la clase central para acceder directamente
 */
