package aed.tp2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NuestrosTests{
    private AirMiles am; 

    @BeforeEach
    void setUp() {
        am = new AirMiles();
    }
    
    @Test 
    void transferirMillas(){
        AirMiles am1 = new AirMiles();
        am1.registrarUsuario(100);
        am1.registrarUsuario(101);
        double saldo = am1.consultarSaldo(101);
        am1.transferirMillas(100, 101, 100);
        //transferir sin saldo
        assertEquals(am1.consultarSaldo(101), saldo);
        //transferir un numero muy grande
        am1.acumularMillas(100, 999999999, "NoPromo");
        am1.transferirMillas(100, 101, 999999990);
        assertEquals(9 , am1.consultarSaldo(100));
        assertEquals(999999990 , am1.consultarSaldo(101));
    }
    @Test
    void canjearMillas(){
        AirMiles am1 = new AirMiles();
        am1.registrarUsuario(100);
        am1.registrarUsuarioConReferidor(200, 100);
        //canjear con Mas millas que las que tengo
        am1.canjearMillas(100, 501);
        double saldo = am1.consultarSaldo(100);
        assertEquals(500, saldo);
        //canjearMillas hasta categoria platino y confirmar millas totales
        am1.consultarCategoria(100);
        am1.acumularMillas(100, 100000000, "NoPromo");
        am1.canjearMillas(100, 100000000);
        assertEquals("Platino", am1.consultarCategoria(100));
        assertEquals(100000500, am1.buscarUsuarioPorID(100).obtenerMillasTotales());
    }
    @Test
    void promoSinTopePrefijoDePromoConTope(){
        am.registrarUsuario(200);
        am.iniciarPromocion("PromoInvierno",2.0);
        am.iniciarPromocionConTope("PromoInvernal", 5.0, 100);
        am.acumularMillas(200,20,"PromoInvernal");
        am.acumularMillas(200,10,"PromoInvierno");
        assertEquals(120.0, am.consultarSaldo(200));
    }
    @Test
    void canjearNoCambiaElMasAcumulador(){
        am.registrarUsuario(201);
        am.registrarUsuario(202);
        am.acumularMillas(201,1000, "NoPromo");
        am.acumularMillas(202, 500, "NoPromo");
        am.canjearMillas(201, 1000);
        assertEquals(0.0, am.consultarSaldo(201));
        assertEquals(201, am.elMasAcumulador());
        am.canjearMillas(202,500);
        assertEquals(201, am.elMasAcumulador());
    }

    @Test
    void acumularConDistanciaDecimal() {
        AirMiles am = new AirMiles();
        am.registrarUsuario(1);
        am.acumularMillas(1, 0.5, "NoPromo");
        assertEquals(0.5, am.consultarSaldo(1));
        am.acumularMillas(1, 0.9, "NoPromo");
        assertEquals(1.4, am.consultarSaldo(1));
    }
    
    @Test
    void rankingConUnSoloUsuario() {
        AirMiles am = new AirMiles();
        am.registrarUsuario(1);
        am.acumularMillas(1, 100, "NoPromo");
        int[] top = am.rankingMillas(5);
        assertEquals(1, top.length);
        assertEquals(1, top[0]);
    }
    @Test
    void rankingDespuesDeCanjearTodo() {
        AirMiles am = new AirMiles();
        am.registrarUsuario(1);
        am.registrarUsuario(2);
        am.acumularMillas(1, 500, "NoPromo");
        am.acumularMillas(2, 300, "NoPromo");
        am.canjearMillas(1, 500);   // 1 queda con 0
        int[] top = am.rankingMillas(2);
        assertEquals(2, top[0]);   // 2 tiene 300, 1 tiene 0
        assertEquals(1, top[1]);
    }

        @Test
    void elMasCanjeadorSoloConTransferencias() {
        AirMiles am = new AirMiles();
        am.registrarUsuario(1);
        am.registrarUsuario(2);
        am.acumularMillas(1, 1000, "NoPromo");
        am.acumularMillas(2, 1, "NoPromo");
        am.transferirMillas(1, 2, 500);   // gasto 500 (transferidas)
        assertEquals(1, am.elMasCanjeador());
        am.transferirMillas(2, 1, 501);
        assertEquals(2, am.elMasCanjeador());   
        
    }
    @Test
    void parExclusivoIdaYVuelta() { // para verificar q al transferiri ida y vuelta no se duplica el par  no se rompe la exclusividad
        AirMiles am = new AirMiles();
        am.registrarUsuario(1);
        am.registrarUsuario(2);
        am.acumularMillas(1, 1000, "NoPromo");
        am.acumularMillas(2, 1000, "NoPromo");
        am.transferirMillas(1, 2, 100);
        am.transferirMillas(2, 1, 50);
        int[][] pares = am.paresTransferidoresExclusivos();
        assertEquals(1, pares.length);
        int a = Math.min(pares[0][0], pares[0][1]);
        int b = Math.max(pares[0][0], pares[0][1]);
        assertEquals(1, a);
        assertEquals(2, b);
    }
        @Test
        void parExclusivoConTransferenciaRepetida() {
            AirMiles am = new AirMiles();
            am.registrarUsuario(1);
                am.registrarUsuario(2);
            am.acumularMillas(1, 1000, "NoPromo");
            am.transferirMillas(1, 2, 670);
            am.transferirMillas(1, 2, 67);   // repetida
            int[][] pares = am.paresTransferidoresExclusivos();
            assertEquals(1, pares.length);   // confirma q sigue siendo exclusivo 
    }


    @Test
    void unUsuarioRecibeDeDosPersonasDistintas() {
        am.registrarUsuario(1);
        am.registrarUsuario(2);
        am.registrarUsuario(3);

        am.acumularMillas(1, 500, "NoPromo");
        am.acumularMillas(2, 500, "NoPromo");

        am.transferirMillas(1, 3, 100);
        am.transferirMillas(2, 3, 100);

        assertEquals(0, am.paresTransferidoresExclusivos().length);//confirma que se rompio la exclusividad
    }

    @Test
    void rankingParcialYDespuesActualizacionDeSaldos() {
        am.registrarUsuario(1);
        am.registrarUsuario(2);
        am.registrarUsuario(3);

        am.acumularMillas(1, 100, "NoPromo");
        am.acumularMillas(2, 300, "NoPromo");
        am.acumularMillas(3, 200, "NoPromo");

        assertArrayEquals(new int[]{2}, am.rankingMillas(1));

        // El usuario 3 supera al usuario 2.
        am.acumularMillas(3, 200, "NoPromo");

        assertArrayEquals(new int[]{3, 2, 1}, am.rankingMillas(3));
    }

    @Test
    void elMasAcumuladorNoEsElDeMayorSaldoDisponible() {
        am.registrarUsuario(1);
        am.registrarUsuario(2);

        am.acumularMillas(1, 1000, "NoPromo");
        am.acumularMillas(2, 500, "NoPromo");

        // El usuario 1 transfiere 800 al usuario 2.
        am.transferirMillas(1, 2, 800);

        assertEquals(200.0, am.consultarSaldo(1));
        assertEquals(1300.0, am.consultarSaldo(2));

        // El usuario 1 sigue siendo quien más millas acumuló históricamente.
        assertEquals(1, am.elMasAcumulador());
    }

    @Test
    void rankingSeReordenaCuandoUnUsuarioRecibeYDespuesCanjea() {
        AirMiles am = new AirMiles();

        am.registrarUsuario(1);
        am.registrarUsuario(2);
        am.registrarUsuario(3);

        am.acumularMillas(1, 900, "NoPromo");
        am.acumularMillas(2, 500, "NoPromo");
        am.acumularMillas(3, 100, "NoPromo");

        // Saldos: 1=200, 2=500, 3=800
        am.transferirMillas(1, 3, 700);

        assertArrayEquals(new int[]{3, 2, 1}, am.rankingMillas(3));

        // El usuario 3 canjea y pasa de 800 a 50.
        am.canjearMillas(3, 750);

        // Saldos: 1=200, 2=500, 3=50
        assertArrayEquals(new int[]{2, 1, 3}, am.rankingMillas(3));
    }

    @Test
    void bonoDeReferidorActualizaElMasAcumulador() {
        AirMiles am = new AirMiles();

        am.registrarUsuario(1);
        am.registrarUsuario(2);

        am.acumularMillas(1, 600, "NoPromo");
        am.acumularMillas(2, 800, "NoPromo");

        assertEquals(2, am.elMasAcumulador());

        // El usuario 1 recibe 500 millas por referir al usuario 3.
        am.registrarUsuarioConReferidor(3, 1);

        // Totales: usuario 1 = 1100, usuario 2 = 800, usuario 3 = 500.
        assertEquals(1, am.elMasAcumulador());
    }
}
