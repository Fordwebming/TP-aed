package aed.tp2;

public class Trie<T> {
    private Nodo<T> raiz; 

    private static class Nodo<T> {          // con static el Nodo es independiente del Trie    
        Nodo<T>[] hijos;                    // entonces el nodo tiene que declarar su propio T que es el mismo T del Trie 
        T valor;
                                            // como java no deja crear arrays genericos lo casteo (Nodo<T>[])        
        @SuppressWarnings("unchecked")      // Trie.Nodo --> xq el nodo es una clase interna del Trie 
        Nodo() {                     
            hijos = (Nodo<T>[]) new Trie.Nodo[52];   // 52 es la cantidad de letras min y masy
            valor = null;               // empieza con valor null y cuando termina la palabra le guardo la promo
        }                          
    }

    public Trie() {
        raiz = new Nodo<T>();
    }

    public void insertar(String palabra, T elem){
        Nodo<T> actual = raiz; 
        for (int i=0; i<palabra.length(); i++){
            char caracter = palabra.charAt(i);
            int indice = obtenerIndice(caracter);

            if (actual.hijos[indice] == null){
                actual.hijos[indice] = new Nodo<T>();
            }
            actual = actual.hijos[indice];
        }
        actual.valor = elem;  
    }

    private int obtenerIndice(char caracter){
        int res=0;
        if(caracter >= 'A' && caracter <= 'Z'){
            res = caracter - 'A';
        } else {
            if(caracter >= 'a' && caracter <= 'z'){
                res = (caracter - 'a') + 26;
            }
        }
        return res;
    }

    public Nodo<T> obtenerNodo(String palabra){  
        Nodo<T> actual = raiz;
        for (int i=0; i<palabra.length(); i++){
            char caracter = palabra.charAt(i);  // devuelve el caracter que esta en la posicion i 
            int indice = obtenerIndice(caracter);
            actual = actual.hijos[indice];   // el if para que no tire error aca 
            if (actual == null){
                return null;
            }
        }
        return actual;
    }

    public boolean pertenece(String palabra){  
        boolean res = false;   
        Nodo<T> p = obtenerNodo(palabra);
        if (p != null && p.valor != null){
            res = true;
        }
        return res; 
    }

    private boolean tieneHijos(Nodo<T> n){
        boolean res = false;
        for (int i=0; i<n.hijos.length; i++){  // voy recorriendo el array aver si en esa posicion tiene un puntero (hijo)
            if (n.hijos[i] != null){
                res = true;
            }
        }
        return res; 
    }

    private boolean eliminarRecursivamente(Nodo<T> actual, String palabra, int i){
        if (i < palabra.length()){
            char caracter = palabra.charAt(i);
            int indice = obtenerIndice(caracter);
            if (eliminarRecursivamente(actual.hijos[indice], palabra, i+1 )){    // ahora va subiendo 
                actual.hijos[indice] = null;    // corto el puntero al hijo que quedo sin valor y sin hijos 
            }
        }
        return actual.valor == null && tieneHijos(actual) == false;   // cuando llegue a la ultima letra que le pusimos en eliminar null 
    }

    public void eliminar(String palabra) {
        if (pertenece(palabra)){                        
            Nodo<T> actual = obtenerNodo(palabra);    
            actual.valor = null;
            eliminarRecursivamente(raiz, palabra, 0);   // va desde la raiz al ultimo Nodo<T> 
        }
    }

    public T obtenerValor(String palabra){
        T res = null;
        if (pertenece(palabra)){
            Nodo<T> actual = obtenerNodo(palabra);  
            res = actual.valor;
        }
        return res; 
    }
}

