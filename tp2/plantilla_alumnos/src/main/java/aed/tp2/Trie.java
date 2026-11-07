package aed.tp2;

public class Trie<T> {
    private Nodo raiz; 

    private class Nodo {
        Nodo[] hijos;   
        T valor;

        Nodo() {                     // 52 es la cantidad de letras min y masy
            hijos = new Nodo[52];  
            valor = null;           // empieza con valor null y cuando termina 
        }                           // la palabra le guardo la promo 
    }

    public Trie() {
        raiz = new Nodo();
    }

    public void insertar(String palabra, T elem){
        Nodo actual = raiz; 
        for (int i=0; i<palabra.length(); i++){
            char caracter = palabra.charAt(i);
            int indice = obtenerIndice(caracter);

            if (actual.hijos[indice] == null){
                actual.hijos[indice] = new Nodo();
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

    public Nodo obtenerNodo(String palabra){  
        Nodo actual = raiz;
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
        Nodo p = obtenerNodo(palabra);
        if (p != null && p.valor != null){
            res = true;
        }
        return res; 
    }

    private boolean tieneHijos(Nodo n){
        boolean res = false;
        for (int i=0; i<n.hijos.length; i++){  // voy recorriendo el array aver si en esa posicion tiene un puntero (hijo)
            if (n.hijos[i] != null){
                res = true;
            }
        }
        return res; 
    }

    private boolean eliminarRecursivamente(Nodo actual, String palabra, int i){
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
            Nodo actual = obtenerNodo(palabra);    
            actual.valor = null;
            eliminarRecursivamente(raiz, palabra, 0);   // va desde la raiz al ultimo nodo 
        }
    }

    public T obtenerValor(String palabra){
        T res = null;
        if (pertenece(palabra)){
            Nodo actual = obtenerNodo(palabra);  
            res = actual.valor;
        }
        return res; 
    }
}

