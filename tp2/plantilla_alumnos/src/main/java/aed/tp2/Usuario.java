package aed.tp2;

public class Usuario implements Comparable<Usuario> {
    public int identificador;  //los atributos son publicos para poder usarlos en la clase nodo del ABB de usuarios
    public double millas_totales;
    public double millas_disponibles;
    public double millas_gastadas;
    public HandleUsuario<Usuario> handleRanking;
    public boolean recibioExclusivamente;
    public boolean transfirioExclusivamente;
    public int unicoUsuarioDelQRecibe;
    public int unicoUsuarioAlQTransfiere;
    public boolean esTransferidorExclusivo;
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

    public Usuario obtenerUsuarioExclusivo() {
        return usuarioExclusivo;
    }

    public void asignarUsuarioExclusivo(Usuario u) {
        usuarioExclusivo=u;
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

    public boolean esExclusivo() {
        return esTransferidorExclusivo;
    }

    public void asignarHanlde(HandleUsuario<Usuario> h) {
        this.handleRanking = h;
    }

    public void registrarTransferenciaConUsuario(Usuario otro) {
        //
        if (this.unicoUsuarioAlQTransfiere < 0) {//registro si los usuarios siempre transfieren a uno solo
            this.unicoUsuarioAlQTransfiere = otro.identificador;
        } else if (this.unicoUsuarioAlQTransfiere != otro.identificador) {
            this.transfirioExclusivamente = false;
        }

        //verifico exclusividad despues de algun movimiento con otro usuario
        if (otro.unicoUsuarioDelQRecibe < 0) {//registro si un usuario recibe siempre del mismo
            otro.unicoUsuarioDelQRecibe = this.identificador;
        } else if (otro.unicoUsuarioDelQRecibe != this.identificador) {
            otro.recibioExclusivamente = false;
        }  
    }

    public void actualizarExclusividad(Usuario otro) {
        this.esTransferidorExclusivo = this.transfirioExclusivamente && 
        this.recibioExclusivamente &&
        (this.unicoUsuarioAlQTransfiere == otro.identificador || this.unicoUsuarioAlQTransfiere==-1)&&
        (this.unicoUsuarioDelQRecibe == otro.identificador || this.unicoUsuarioDelQRecibe==-1);

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
