package unitarias;

import estacionamiento.dominio.controladores.VehiculoController;
import estacionamiento.persistencia.sistema.SistemaDePersistencia;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class VehiculoControllerTest {

    private static final String PLACA = "ASD123";
    private static final String CARRO = "Carro";
    private static final String MOTO = "Moto";

    @Autowired
    private VehiculoController vehiculoController;
    private SistemaDePersistencia sistemaDePersistencia;

    @Before
    public void init(){
        sistemaDePersistencia = new SistemaDePersistencia();
        vehiculoController = sistemaDePersistencia.obtenerVehiculoController();
        sistemaDePersistencia.iniciar();
    }

    @After
    public void finish(){
        sistemaDePersistencia.finalizar();
    }

    @Test
    public void calculoDeDias(){
        //act
        long unDia = vehiculoController.calcularDias(25);
        long dosDias = vehiculoController.calcularDias(33);

        //assert
        assertEquals(1,unDia);
        assertEquals(2,dosDias);
    }

    @Test
    public void calculoDeHoras(){
        //act
        long seisHoras = vehiculoController.calcularHoras(30);
        long dosHoras = vehiculoController.calcularHoras(50);

        //assert
        assertEquals(6,seisHoras);
        assertEquals(2,dosHoras);
    }

    @Test
    public void validacionDePrimeraLetra(){
        //arrange
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaActual = calendar.get(Calendar.DAY_OF_WEEK);

        //act
        Boolean validacion = vehiculoController.validacionDePrimeraLetra(PLACA);

        if( (diaActual == 1 || diaActual == 2) ){
            // assert
            assertFalse(validacion);
        } else {
            // assert
            assertTrue(validacion);
        }

    }

    @Test
    public void validacionDeParqueaderoConEspacio(){
        //arrange

        //act
        Boolean validacionCarro = vehiculoController.validacionCapacidad(CARRO);
        Boolean validacionMoto = vehiculoController.validacionCapacidad(MOTO);

        //assert
        assertFalse(validacionCarro);
        assertFalse(validacionMoto);
    }

}
