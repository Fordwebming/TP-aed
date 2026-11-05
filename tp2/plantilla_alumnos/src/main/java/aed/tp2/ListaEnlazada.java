package aed.tp2;

public class ListaEnlazada<T>{
    private Nodo primero;
    private Nodo ultimo;
    private int longitud; 

    private class Nodo{
        T valor;
        Nodo sig;
        Nodo ant;
        Nodo(T v) {valor = v;}
    }

    public ListaEnlazada(){
        primero = null;
        ultimo = null;
        longitud = 0;
    }

    public void agregarAtras(T elem){
        Nodo nuevo = new Nodo(elem);
        if (primero == null){
            primero = nuevo;
        } else {
            ultimo.sig = nuevo;
            nuevo.ant = ultimo;
        }
        ultimo = nuevo;
        longitud ++;
    }

    public int longitud(){
        return longitud;
    }

    public class ListaIterador implements Iterador<T>{
        private Nodo prox; //devuelve el siguiente
        private Nodo prev; //devuelve el anterior 

        ListaIterador(){
            prox = primero;
            prev = null;
        }

        public boolean haySiguiente(){
            return prox != null;
        }

        public boolean hayAnterior(){
            return prev != null;
        }

        public T siguiente(){
            T res = prox.valor;
            prev = prox;
            prox = prox.sig;
            return res; 
        }

        public T anterior(){
            T res = prev.valor;
            prox = prev;
            prev = prev.ant;
            return res; 
        }
    }
    public Iterador<T> iterador(){
        return new ListaIterador();
    }
}