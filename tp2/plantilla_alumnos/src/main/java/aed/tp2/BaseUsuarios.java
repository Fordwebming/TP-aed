package aed.tp2;

public class BaseUsuarios {
    private Nodo raiz;
    private int cardinal;
    

    private class Nodo {
        // Agregar atributos privados del Nodo
        Usuario usuario;    
        int id;
        Nodo izquierda;
        Nodo padre;
        Nodo derecha;

        // Crear Constructor del nodo
        private Nodo(Usuario u) {
            usuario = u;
            id = u.identificador;
            izquierda = null;
            derecha = null;
            padre = null;
        }
        
    }

    public BaseUsuarios() {
        raiz = null;
        cardinal = 0;
    }

    public void insertar(Usuario usuario){
        boolean insertar = false;
        Nodo nuevo = new Nodo(usuario);
        Nodo raiz_actual = this.raiz;
        if (raiz_actual == null){
            this.raiz = nuevo;
            this.cardinal++;
         }
        else{
            while (raiz_actual != null && insertar == false){
                if (nuevo.id > raiz_actual.id){
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
                }else if (nuevo.id < raiz_actual.id) {
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

    public Usuario minimo(){
        Nodo raiz_actual = this.raiz;
        while (raiz_actual.izquierda != null){
            raiz_actual = raiz_actual.izquierda;
        }
        return raiz_actual.usuario;
    }

    public Usuario buscarUsuarioPorID(int u){
        Nodo raiz_actual = this.raiz;
        while (raiz_actual != null && raiz_actual.id != u ){
            if (raiz_actual.id < u){ //busco a la derecha
                raiz_actual = raiz_actual.derecha;
            }
            else{
                raiz_actual = raiz_actual.izquierda; //busco a la izquierda
            }
        }
        //si el usuario no existe devuelve null, asi que tambien sirve como un metodo para saber si pertenece un usuario
        if (raiz_actual == null){
            return null;
        }
        else{
            return raiz_actual.usuario;
        }
    }


}