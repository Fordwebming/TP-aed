package aed.tp2;

public class Promo{
    public double factor;
    public double tope;
    public double acumulacionMillas; 

    public Promo(double f){  // promocionActiva
        factor = f;
        tope = 0;   // como tope >0 entonces cuando sea 0 indica es promoActiva 
        acumulacionMillas = 0;  //ver en acumularMillas cuandos sean iguales 
    }

    public Promo(double f, double t){  // promocionActivaTope 
        factor = f;
        tope = t;
        acumulacionMillas = 0;
    }
}