package aed.tp2;

public class Trie<T> {
    private Nodo raiz; 

    private class Nodo {
        Nodo[] hijos;   
        T valor;

        Nodo() {                     // 52 es la cantidad de letras min y masy
            hijos = new Nodo[52];   // valor que da null 
        }
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

    private Nodo<T> obtenerNodo(String palabra){
        Nodo<T> actual = raiz;
        for (int i=0; i<palabra.length(); i++){
            char caracter = palabra.charAt(i);
            int indice = obtenerIndice(caracter);
            actual = actual.hijos[indice];   // el if para que no tire error aca 
            if (actual == null){
                return null;
            }
        }
        return actual;
    }

    public boolean pertenece(String palabra){
        return obtenerNodo(palabra) != null;
    }

    public void eliminar(String palabra) {
        if (pertenece(palabra)){
            Nodo<T> actual = obtenerNodo(palabra);
            actual.valor = null;
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

