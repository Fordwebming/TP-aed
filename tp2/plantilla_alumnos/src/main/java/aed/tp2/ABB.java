package aed.tp2;

public class ABB<T extends Comparable<T>>{
    public Nodo raiz;
    public int cardinal;
    

    public class Nodo {
        // Agregar atributos privados del Nodo
        public T usuario; //si bien el atributo se llama usuario sigue siendo de tipo generico
        public Nodo izquierda;
        public Nodo padre;
        public Nodo derecha;

        // Crear Constructor del nodo
        public Nodo(T u) {
            usuario = u;
            //id = u.identificador;
            izquierda = null;
            derecha = null;
            padre = null;
        }
        
    }

    public ABB() {
        raiz = null;
        cardinal = 0;
    }

    public void insertar(T usuario){
        boolean insertar = false;
        Nodo nuevo = new Nodo(usuario);
        Nodo raiz_actual = this.raiz;
        if (raiz_actual == null){
            this.raiz = nuevo;
            this.cardinal++;
         }
        else{
            while (raiz_actual != null && insertar == false){
                if (nuevo.usuario.compareTo(raiz_actual.usuario) > 0){
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
                }else if (nuevo.usuario.compareTo(raiz_actual.usuario) < 0) {
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

    public T minimo(){
        Nodo raiz_actual = this.raiz;
        while (raiz_actual.izquierda != null){
            raiz_actual = raiz_actual.izquierda;
        }
        return raiz_actual.usuario;
    }

}