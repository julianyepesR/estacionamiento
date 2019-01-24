package integracion;

import estacionamiento.EstacionamientoApplication;
import estacionamiento.transversal.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.transversal.enumeraciones.TipoVehiculoEnum;
import estacionamiento.modelo.entidades.VehiculoEntity;
import estacionamiento.modelo.dao.servicios.PersistenciaImplementacion;
import estacionamiento.negocio.helpers.VehiculoIHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import testUtils.TestUtils;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstacionamientoApplication.class)
public class MotoTest {

    private static final String PLACA_A = "AAA123";
    private static final String PLACA_B = "BBB123";
    private static final String PLACA_C = "CCC123";
    private static final String PLACA_D = "DDD123";
    private static final int CILINDRAJE_MENOR = 500;
    private static final int CILINDRAJE_MAYOR = 501;
    private static final String TIPO_DE_VEHICULO = "Moto";
    private static final String CARRO = "Carro";

    TestUtils testUtils;

    @Autowired
    PersistenciaImplementacion persistenciaImplementacion;

    @Autowired
    VehiculoIHelper vehiculoIHelper;

    @Before
    public void iniciar(){
        testUtils = new TestUtils(persistenciaImplementacion);
        testUtils.resetDB();
    }

    @Test
    public void agregarMoto(){
        // arrange
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE_MENOR+"\",\"placa\": \""+PLACA_B+"\"}";
        vehiculoIHelper.ingresoDeVehiculo(jsonData);

        // act
        VehiculoEntity vehiculoEntity1 = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_B);

        // assert
        assertEquals(vehiculoEntity1.getCilindraje(),CILINDRAJE_MENOR);
        assertEquals(vehiculoEntity1.getTipoDeVehiculo(), TipoVehiculoEnum.MOTO);
        assertEquals(vehiculoEntity1.getEstadoActual(), EstadoVehiculoEnum.EN_DEUDA);
    }

    @Test
    public void agregarMotoConA(){
        // arrange
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaActual = calendar.get(Calendar.DAY_OF_WEEK);

        // act
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE_MENOR+"\",\"placa\": \""+PLACA_A+"\"}";
        vehiculoIHelper.ingresoDeVehiculo(jsonData);
        VehiculoEntity vehiculoEntity = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_A);

        if( (diaActual == 1 || diaActual == 2) ){
            // assert
            assertEquals(vehiculoEntity.getCilindraje(),CILINDRAJE_MENOR);
            assertEquals(vehiculoEntity.getTipoDeVehiculo(), TipoVehiculoEnum.MOTO);
            assertEquals(vehiculoEntity.getEstadoActual(), EstadoVehiculoEnum.EN_DEUDA);
        } else {
            // assert
            assertNull(vehiculoEntity);
        }
    }

    @Test
    public void calcularCostoMoto(){
        // arrange
        testUtils.crearVehiculo(TipoVehiculoEnum.MOTO,PLACA_C,CILINDRAJE_MAYOR,10,0);

        // act
        String jsonData1 = "{\"placa\": \""+PLACA_C+"\"}";
        String costo = vehiculoIHelper.calcularCosto(jsonData1);

        // assert
        assertEquals("6000",costo);
    }

    @Test
    public void errorEstacionamientoLleno(){
        // arrange
        testUtils.llenarParqueadero(TipoVehiculoEnum.MOTO);

        // act
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE_MENOR+"\",\"placa\": \""+PLACA_D+"\"}";
        vehiculoIHelper.ingresoDeVehiculo(jsonData);
        VehiculoEntity vehiculoEntity = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_D);

        // assert
        assertNull(vehiculoEntity);
    }

    @Test
    public void validacionDeParqueaderoConEspacio(){
        //arrange
        //act
        Boolean validacionCarro = vehiculoIHelper.validacionCapacidad(CARRO);
        Boolean validacionMoto = vehiculoIHelper.validacionCapacidad(TIPO_DE_VEHICULO);

        //assert
        assertFalse(validacionCarro);
        assertFalse(validacionMoto);
    }

}
