package integracion;

import estacionamiento.constantes.Constantes;
import testUtils.TestUtils;
import estacionamiento.EstacionamientoApplication;
import estacionamiento.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.enumeraciones.TipoVehiculoEnum;
import estacionamiento.modelo.entidad.VehiculoEntity;
import estacionamiento.modelo.servicios.PersistenciaImplementacion;
import estacionamiento.modelo.servicios.VehiculoImplementacion;
import org.junit.Before;
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
public class CarroTest {

    private static final String PLACA_A = "AAA321";
    private static final String PLACA_B = "BBB321";
    private static final String PLACA_C = "CCC321";
    private static final String PLACA_D = "DDD321";
    private static final int CILINDRAJE = 0;
    private static final String TIPO_DE_VEHICULO = "Carro";

    TestUtils testUtils;

    @Autowired
    Constantes constantes;

    @Autowired
    PersistenciaImplementacion persistenciaImplementacion;

    @Autowired
    VehiculoImplementacion vehiculoImplementacion;

    @Before
    public void iniciar(){
        testUtils = new TestUtils(persistenciaImplementacion);
        testUtils.resetDB();
    }

    @Test
    public void agregarCarro(){
        // arrange
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE+"\",\"placa\": \""+PLACA_B+"\"}";
        vehiculoImplementacion.ingresoDeVehiculo(jsonData);

        // act
        VehiculoEntity vehiculoEntityTest = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_B);

        // assert
        assertEquals(vehiculoEntityTest.getCilindraje(),CILINDRAJE);
        assertEquals(vehiculoEntityTest.getTipoDeVehiculo(), TipoVehiculoEnum.CARRO);
        assertEquals(vehiculoEntityTest.getEstadoActual(), EstadoVehiculoEnum.EN_DEUDA);
    }

    @Test
    public void agregarCarroConA(){
        // arrange
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaActual = calendar.get(Calendar.DAY_OF_WEEK);

        // act
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE+"\",\"placa\": \""+PLACA_A+"\"}";
        vehiculoImplementacion.ingresoDeVehiculo(jsonData);
        VehiculoEntity vehiculoEntity = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_A);

        if( (diaActual == 1 || diaActual == 2) ){
            // assert
            assertEquals(vehiculoEntity.getCilindraje(),CILINDRAJE);
            assertEquals(vehiculoEntity.getTipoDeVehiculo(), TipoVehiculoEnum.MOTO);
            assertEquals(vehiculoEntity.getEstadoActual(), EstadoVehiculoEnum.EN_DEUDA);
        } else {
            // assert
            assertNull(vehiculoEntity);
        }
    }

    @Test
    public void calcularCostoCarro(){
        // arrange
        testUtils.crearVehiculo(TipoVehiculoEnum.CARRO,PLACA_C,CILINDRAJE,3,1);

        // act
        String jsonData1 = "{\"placa\": \""+PLACA_C+"\"}";
        String costo = vehiculoImplementacion.calcularCosto(jsonData1);
        VehiculoEntity vehiculoEntity = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_C);

        // assert
        assertEquals("11000",costo);
        assertNull(vehiculoEntity);

    }

    @Test
    public void errorEstacionamientoLleno(){
        // arrange
        testUtils.llenarParqueadero(TipoVehiculoEnum.CARRO);

        // act
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE+"\",\"placa\": \""+PLACA_D+"\"}";
        vehiculoImplementacion.ingresoDeVehiculo(jsonData);
        VehiculoEntity vehiculoEntity = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_D);

        // assert
        assertNull(vehiculoEntity);
    }

    @Test
    public void paginaInicial(){
        // arrange
        testUtils.llenarParqueadero(TipoVehiculoEnum.CARRO);
        testUtils.llenarParqueadero(TipoVehiculoEnum.MOTO);

        // act
        String listadoJson = vehiculoImplementacion.cargarPaginaInicial();
        System.out.println(listadoJson);

        // assert
        assertTrue(!"".equals(listadoJson));
    }
}
