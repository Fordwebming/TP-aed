package aed.tp2;

public class HandleHeap<T> implements Handle<T> {
    private T valorReferencia;
    private int indice;
    private HeapMax<T> heap;

    public HandleHeap(T valor) {
        valorReferencia=valor;
        heap = null;
        indice = -1;
    }

    public T valor () {
        return valorReferencia;
    }

    public void modificar(T nuevoValor) {
        valorReferencia = nuevoValor;
        if (heap!=null) {
            heap.actualizar(this);
        }
    }

    public void eliminar() {
        if(heap != null) {
            heap.eliminarEn(indice);
        }
    }

    public int obtenerIndice() {
        return indice;
    }

    public void asignarIndice(int i) {
        indice = i;
    }

    public void asignarHeap(HeapMax<T> nuevoHeap) {
        heap =nuevoHeap;
    }

    public boolean equals(Handle<T> otro) {
        return otro != null && valorReferencia.equals(otro.valor());
    }
}
