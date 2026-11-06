package aed.tp2;

import java.util.ArrayList;
import java.util.Comparator;

public class HeapMax<T>{
    
    private int cantidad; //el arbol tiene que esta completo, tipo equilibrado, por eso hay que saber la prox posic disp
    private ArrayList<HandleHeap<T>> elementos;
    private Comparator<T> comparador;


    public HeapMax(Comparator<T> critComparacion) {
        elementos =new ArrayList<HandleHeap<T>>();
        cantidad = 0;
        comparador = critComparacion;
    }

    private int comparar(HandleHeap<T> h1, HandleHeap<T> h2) {
        return comparador.compare(h1.valor(), h2.valor());
    }

    public void insertar(HandleHeap<T> handle) {
        elementos.add(handle);
        cantidad+=1;
        handle.asignarIndice(cantidad-1);
        siftUp(cantidad-1);
    }

    private void siftUp(int indice) {//subo un elemento en el heap cuando tiene mayor prioridad que su padre
        while (indice>0) {
            int padre =(indice-1)/2;
            if (comparar(elementos.get(indice), elementos.get(padre)) >0) {//comparo si el hijo es mayor que el padre
                intercambiar(indice, padre);
                indice=padre;//voy subiendo hasta llegar a la raiz donde no hay mas padre
            } else {
                return;
            }
        }
    }

    private void intercambiar(int posicHijo, int posicPadre) {
        HandleHeap<T>  antiguoHijo = elementos.get(posicHijo);
        elementos.set(posicHijo, elementos.get(posicPadre));//bajo al antiguo padre a la pasicion del hijo
        elementos.set(posicPadre,antiguoHijo);//subo al hijo a la posicion del padre
        elementos.get(posicHijo).asignarIndice(posicHijo);//le cambio los indices a los intercambiados
        elementos.get(posicPadre).asignarIndice(posicPadre);
    }

    private void siftDown(int indice) {//voy bajando elelem en el heap cuando alguno de sus hijos tiene mayor prioridad
        int hijoIzq =(2*indice)+1;//busco uno de los hijos
        while (hijoIzq < cantidad) {//aca veo que existe tal hijo izq, va a tenr wue ser menor que la cantidad de elems
            int hijoMayor =hijoIzq;
            int hijoDer = hijoIzq +1;
            if ((hijoDer<cantidad) && comparar(elementos.get(hijoDer), elementos.get(hijoIzq)) >0) {
                hijoMayor = hijoDer;
            }
            if (comparar(elementos.get(hijoMayor), elementos.get(indice)) >0) {
                intercambiar(indice, hijoMayor);
                indice=hijoMayor;
                hijoIzq = (2*indice) +1;
            } else {
                return;
            }
        }
    }

    public void actualizar(HandleHeap<T> handle) {
        int indice =handle.obtenerIndice();
        siftUp(indice);
        siftDown(handle.obtenerIndice());
    }

    public void eliminarEn(int indice) {
        if (indice < 0 || indice>= cantidad) {
            return;
        }
        intercambiar(indice, cantidad-1);
        HandleHeap<T> eliminado = elementos.remove(cantidad-1);
        cantidad-=1;
        eliminado.asignarHeap(null);
        eliminado.asignarIndice(-1);
        
        if (indice < cantidad) {
            HandleHeap<T> elementoMovido = elementos.get(indice);
            siftUp(indice);
            siftDown(elementoMovido.obtenerIndice());
            //esto porque el ultimo elemento cambio deposicion y tengo que verificar o retomar el equilibrio
        }
    }
    public int cantDeElems() {
        return cantidad;
    }
    public ArrayList<HandleHeap<T>> arrayDeRanking() {
        return elementos;//creo que no lo uso
    }

    public T devolverMaximo() {
        if (cantidad==0) {
            return null;
        }
        return elementos.get(0).valor();
    }

}