package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    // semi propuestas para hacer el codigo sin mucha linea roja
    private BaseUsuarios baseUsuarios;
    private Trie<Promocion> promociones;//colocarnonbre
    private int idMayorAcumulador;
    private int idMayorCanjeador;
    private double maxMillasTotales; 
    private double maxMillasGastadas;

    public AirMiles() {
        this.baseUsuarios = new BaseUsuarios();
        this.promociones = new Trie <>(); //colocar nombre
        this.promociones.insertar("algo");//asumo que promociones tendra esta forma para insertar y le coloco algo
        this.idMayorAcumulador = -1;
        this.idMayorCanjeador = -1;
        this.maxMillasTotales = 0;
        this.maxMillasGastadas = 0;
    }


    public void registrarUsuario(int id) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {//le faltan un par de cosas despues las veo
        Usuario usuario = BaseUsuarios.buscar(id);
        double factorCategoria;
        double millasTotalesAcumuladas = usuario.getMillasTotales();
        //bronce,plata,oro,platino(este ya no tiene beneficio)
        if (millasTotalesAcumuladas>=1000000){
            factorCategoria = 0.0;
        }else if (millasTotalesAcumuladas >= 500000){
            factorCategoria = 2.0;
        }else if (millasTotalesAcumuladas >= 100000){
            factorCategoria = 1.5;
        }else{
            factorCategoria =1.0;
        }

        if (factorCategoria == 0.0){
            return;//para q no sume millas
        }

        Promocion promocion = promociones.buscar(nombrePromo);
        double factorPromocion;
        boolean tieneTope = false;
        double tope = -1;
        double millasAcumuladasXPromocion= 0;

        if (promocion != null){
            factorPromocion = 1.0;
        }else{
            factorPromocion = promocion.getFactor();
            tieneTope = promocion.tieneTope();
            if (tieneTope){
                tope = promocion.getTope();
                millasAcumuladasXPromocion = promocion.getMillasAcumuladas();
            }
        }

        double millasGanadas = distancia * factorCategoria * factorPromocion;
        usuario.sumarMillasTotales (millasGanadas);
        usuario.sumarMillasDisponibles(millasGanadas);

        If (usuario.getMillastotales () > maxMillasTotales){
            maxMillasTotales = usuario.getMillasTotales();
            idMayorAcumulador = usuario.getId();
        }
        if (tieneTope){
            promocion.sumarMillasAcumuladas(millasGanadas);
            if (promocion.getMillasAcumuladas() >= tope){
                promociones.eliminar(nombrePromo);
            }
        }

    }

    public void canjearMillas(int id, double cantidad) {//aun le debo agregar varias cosas q le faltan
        Usuario usuario = baseUsuarios.buscar(id);
        if (usuario.getMillasDisponibles() <cantidad ){return; }
        usuario.restarDisponibles (cantidad);
        usuario.sumarGastadas(cantidad);
        // cuando haya nombre deberia modificar el ranking
        if (usuario.getMillasGastadas() > maxMillasGastadas){
            maxMillasGastadas = usuario.getMillasGastadas();
            idMayorCanjeador = usuario.getId();
        }
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public double consultarSaldo(int id) {
        throw new UnsupportedOperationException("Sin implementar");
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

class Usuario {
    private int iDentificador; 
    private double millasDisponibles;
    private double millasTotales;
    private Handle<Usuario> lugarEnRanking;
    private double millasGastadas;

    public Usuario (int iDentificador) {
        this.iDentificador = iDentificador;
        this.millasDisponibles = 0;
        this.millasTotales = 0;
        this.lugarEnRanking = null;
        this.millasGastadas = 0;
    }
// mis cositos para acortar codigo    
public double getMillasTotales(){return millasTotales;}
public double getmillasDisponibles (){return millasDisponibles;}
public void sumarMillasTotales (double millas){millasTotales += millas;}
public void sumarMillasDisponibles (double millas){millasDisponibles += millas;}
public int getId(){return iDentificador;}
public double getMillasDisponibles(){return millasDisponibles;}
public double getMillasGastadas(){return millasGastadas;}
public void restarDisponibles (double millas){millasDisponibles -= millas;} 
public void sumarGastadas (double millas){millasGastadas +=millas;}
}

class Promocion  {
    private String nombrePromo;
    private double factor;
    private  double tope;
    private double millasAcumuladas;

    public Promocion (String nombrePromo, double factor,double tope){
        this.nombrePromo = nombrePromo;
        this.factor =factor;
        this.tope = tope;
        this.millasAcumuladas = 0;
    }


public String getNombrePromo(){return nombrePromo;}
public double getFactor (){return factor;}
public double getTope(){return tope;}
public double getMillasAcumuladas(){return millasAcumuladas;}
public boolean tieneTope(){return tope >= 0;}
public void sumarMillasAcumuladas (double millas) {millasAcumuladas += millas;}
}

