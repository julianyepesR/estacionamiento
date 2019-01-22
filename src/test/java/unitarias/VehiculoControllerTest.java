package unitarias;


import estacionamiento.EstacionamientoApplication;
import estacionamiento.modelo.servicios.PersistenciaImplementacion;
import estacionamiento.modelo.servicios.VehiculoImplementacion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstacionamientoApplication.class)
public class VehiculoControllerTest {

    private static final String PLACA = "ASD123";

    @Autowired
    PersistenciaImplementacion persistenciaImplementacion;

    @Autowired
    VehiculoImplementacion vehiculoImplementacion;

    @Test
    public void calculoDeDias(){
        //act
        long unDia = vehiculoImplementacion.calcularDias(25);
        long dosDias = vehiculoImplementacion.calcularDias(33);

        //assert
        assertEquals(1,unDia);
        assertEquals(2,dosDias);
    }

    @Test
    public void calculoDeHoras(){
        //act
        long seisHoras = vehiculoImplementacion.calcularHoras(30);
        long dosHoras = vehiculoImplementacion.calcularHoras(50);

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
        Boolean validacion = vehiculoImplementacion.validacionDePrimeraLetra(PLACA);

        if( (diaActual == 1 || diaActual == 2) ){
            // assert
            assertFalse(validacion);
        } else {
            // assert
            assertTrue(validacion);
        }

    }
}
