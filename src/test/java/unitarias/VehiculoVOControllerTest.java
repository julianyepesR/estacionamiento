package unitarias;


import estacionamiento.EstacionamientoApplication;
import estacionamiento.modelo.dao.servicios.PersistenciaImplementacion;
import estacionamiento.negocio.helpers.VehiculoIHelper;
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
public class VehiculoVOControllerTest {

    private static final String PLACA = "ASD123";

    @Autowired
    PersistenciaImplementacion persistenciaImplementacion;

    @Autowired
    VehiculoIHelper vehiculoIHelper;

    @Test
    public void calculoDeDias(){
        //act
        long unDia = vehiculoIHelper.calcularDias(25);
        long dosDias = vehiculoIHelper.calcularDias(33);

        //assert
        assertEquals(1,unDia);
        assertEquals(2,dosDias);
    }

    @Test
    public void calculoDeHoras(){
        //act
        long seisHoras = vehiculoIHelper.calcularHoras(30);
        long dosHoras = vehiculoIHelper.calcularHoras(50);

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
        Boolean validacion = vehiculoIHelper.validacionDePrimeraLetra(PLACA);

        if( (diaActual == 1 || diaActual == 2) ){
            // assert
            assertFalse(validacion);
        } else {
            // assert
            assertTrue(validacion);
        }

    }
}
