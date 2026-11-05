package aed.tp2;
public class HeapMax<T extends Comparable<T>>{
    //raiz es el que tene mas millas
    //prop del heap max: caa padre tiene un valor mayor o igual al de sus hijos, sino reacomodar el heap
    private NodoHeap raiz;
    //al agregar en el heap se coloca en el primer lugar libre, de izq a der, luego reacomodamos el heap
    private int cantidad; //el arbol tiene que esta completo, tipo equilibrado, por eso hay que saber la prox posic disp
    

    public HeapMax() {
        raiz = null;
        cantidad = 0;
    }

    private String aBinario(int posicion) {
        String res = "";
        while (posicion>0) {
            int resto = posicion % 2;
            res = resto + res;
            posicion = posicion / 2;
        }
        return res;
    }

    private NodoHeap obtenerNodo(int posicion) {//para poder obetener correctamente el nodo padre del nuevo nodo y no romper el vinculo
        String caminoHastaNodo = aBinario(posicion);
        NodoHeap actual = raiz;
        for (int i=1; i<caminoHastaNodo.length(); i++){//iniciando i en 1 me aseguro de no tomar el primer 1 del camino binario
            //ya que ese referencia a la raiz
            if (caminoHastaNodo.charAt(i) == '0') {//en binario los 0 indican izquierda y los 1 derecha
                actual = actual.izq;
            } else {
                actual = actual.der;
            }

        }
        return actual;
    }

    private class NodoHeap {
        T valor; //aca se va a guarda el handle
        NodoHeap izq;
        NodoHeap der;
        NodoHeap padre;
        NodoHeap(T v) {
            valor=v;
        }
    }


    public void insertar(T elem) {
        NodoHeap nuevo = new NodoHeap(elem);
        if (cantidad==0) {
            raiz=nuevo;
        } else {
            int posicionNueva = cantidad +1;
            int posicionPadre = posicionNueva/2;
            NodoHeap padreDelNuevo= obtenerNodo(posicionPadre);
            nuevo.padre=padreDelNuevo;
            if (posicionNueva%2 ==0) {
                padreDelNuevo.izq = nuevo;
            } else {
                padreDelNuevo.der = nuevo;
            }
        }//en esta creo el vinculo entre el nodo nuevo y su padre ya existente
        //en obtenerNodo recorro el camino que me da el binario para encontrar el nodo que quiero usar como padre
        cantidad+=1;
        siftUp(nuevo);//conservo el equilibrio del heap
    }

    private void intercambiar(NodoHeap a, NodoHeap b) {
        T copiaParaIntercambiar = a.valor;
        a.valor = b.valor;
        b.valor = copiaParaIntercambiar;
    }

    //cuando inserto un nodo nuevo
    private void siftUp(NodoHeap actual) {
        while (actual.padre != null && actual.valor.compareTo(actual.padre.valor) > 0) {//veo si el hijo es mayor que el padre
            intercambiar(actual, actual.padre);
            actual = actual.padre; //cuando llego a la raiz termino puedo ya no hay padre -> es maximo

        }
    }

    public void siftDown(NodoHeap actual) {//si un padre es menor que uno de sus hijos, lo bajo intercambianddo
        while (actual.izq != null) {
            NodoHeap mayor =actual.izq;//voy buscndo algun hijo que sea mayor que se padre
            if (actual.der != null && actual.der.valor.compareTo(mayor.valor) > 0) {
                mayor = actual.der;
            }
            if (actual.valor.compareTo(mayor.valor) >= 0) {
                return;
            }
            intercambiar(actual, mayor);//si el padre es mnor que elhijo, intercambio
            actual = mayor;//sigo bajando
        }
    }//uso este metodo luego para lo casos en los que la raiz quede menor que algun hijo luego e una transaccion

    public T maximo() {
        return raiz.valor;
    }

    private  void actualizar(NodoHeap actual) {
        if (actual.padre != null && actual.valor.compareTo(actual.padre.valor) > 0) {
            siftUp(actual);
        } else {
            siftDown(actual);
        }
    }

}