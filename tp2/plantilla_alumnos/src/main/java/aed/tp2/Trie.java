package aed.tp2;

import java.util.ArrayList;

public class Trie<T> {
    private Nodo raiz; 

    private class Nodo {          
        ArrayList<Nodo> hijos;                    
        T valor;

        Nodo() {                     
            hijos = new ArrayList<Nodo>();
            for (int i=0; i< 52; i++){       //52 es la cantidad de mins y mays
                hijos.add(null);
            }
            valor = null;               
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

            if (actual.hijos.get(indice) == null){
                actual.hijos.set(indice, new Nodo());
            }
            actual = actual.hijos.get(indice);
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
            char caracter = palabra.charAt(i); 
            int indice = obtenerIndice(caracter);
            actual = actual.hijos.get(indice);   
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
        for (int i=0; i<n.hijos.size(); i++){  
            if (n.hijos.get(i) != null){
                res = true;
            }
        }
        return res; 
    }

    private boolean eliminarRecursivamente(Nodo actual, String palabra, int i){
        if (i < palabra.length()){
            char caracter = palabra.charAt(i);
            int indice = obtenerIndice(caracter);
            if (eliminarRecursivamente(actual.hijos.get(indice), palabra, i+1 )){   
                actual.hijos.set(indice, null);   
            }
        }
        return actual.valor == null && tieneHijos(actual) == false;  
    }

    public void eliminar(String palabra) {
        if (pertenece(palabra)){                        
            Nodo actual = obtenerNodo(palabra);    
            actual.valor = null;
            eliminarRecursivamente(raiz, palabra, 0);  
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

