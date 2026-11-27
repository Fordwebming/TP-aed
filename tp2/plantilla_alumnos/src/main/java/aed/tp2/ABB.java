package aed.tp2;

public class ABB<T extends Comparable<T>, V>{
    private Nodo raiz;
    private int cardinal;
    

    public class Nodo {
        // Agregar atributos privados del Nodo
        private T clave;
        private V valor; //si bien el atributo se llama usuario sigue siendo de tipo generico
        private Nodo izquierda;
        private Nodo padre;
        private Nodo derecha;

        // Crear Constructor del nodo
        public Nodo(T c,V u) {
            valor = u;
            clave = c;
            izquierda = null;
            derecha = null;
            padre = null;
        }

        public T clave(){
            return this.clave;
        }
        
        public V valor(){
            return this.valor;
        }

        public Nodo izquierda(){
            return this.izquierda;
        }

        public Nodo derecha(){
            return this.derecha;
        }

        public Nodo Padre(){
            return this.padre;
        }
        
    }

    public ABB() {
        raiz = null;
        cardinal = 0;
    }

    public Nodo raiz(){
        return this.raiz;
    }

    public void insertar(T clave , V valor){
        boolean insertar = false;
        Nodo nuevo = new Nodo(clave,valor);
        Nodo raiz_actual = this.raiz;
        if (raiz_actual == null){
            this.raiz = nuevo;
            this.cardinal++;
         }
        else{
            while (raiz_actual != null && insertar == false){
                if (nuevo.clave.compareTo(raiz_actual.clave) > 0){
                    if (raiz_actual.derecha == null){ //si no hay nada mas a la derecha 
                        raiz_actual.derecha = nuevo;
                        nuevo.padre = raiz_actual;
                        insertar = true;
                        this.cardinal++;
                        //break
                    } 
                    else{
                        raiz_actual = raiz_actual.derecha; //recorro hacia la derecha
                    }
                }else if (nuevo.clave.compareTo(raiz_actual.clave) < 0) {
                    if (raiz_actual.izquierda == null){ //si no hay nada mas a la izquierda 
                        raiz_actual.izquierda = nuevo;
                        nuevo.padre = raiz_actual;
                        insertar = true;
                        this.cardinal++;
                        //break
                    }
                    else {
                        raiz_actual = raiz_actual.izquierda; //recorro hacia la izquierda
                    }
                }
                else{
                    insertar = true; //si usara break este no seria necesario, no estoy seguro si esta permitido asi que lo agrego
                }
            }
        }
    }
    public int cardinal() {
        return this.cardinal;
    }

    

}