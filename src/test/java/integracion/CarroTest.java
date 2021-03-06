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
public class CarroTest {

    private static final String PLACA_A = "AAA321";
    private static final String PLACA_B = "BBB321";
    private static final String PLACA_C = "CCC321";
    private static final String PLACA_D = "DDD321";
    private static final int CILINDRAJE = 0;
    private static final String TIPO_DE_VEHICULO = "Carro";

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
    public void agregarCarro(){
        // arrange
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE+"\",\"placa\": \""+PLACA_B+"\"}";
        vehiculoIHelper.ingresoDeVehiculo(jsonData);

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
        vehiculoIHelper.ingresoDeVehiculo(jsonData);
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
        String costo = vehiculoIHelper.calcularCosto(jsonData1);
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
        vehiculoIHelper.ingresoDeVehiculo(jsonData);
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
        String listadoJson = vehiculoIHelper.cargarPaginaInicial();
        System.out.println(listadoJson);

        // assert
        assertTrue(!"".equals(listadoJson));
    }

    @Test
    public void obtenerTRM(){
        // arrange

        // act
        String trm = vehiculoIHelper.obtenerTRM();
        // assert

        assertTrue(!"".equals(trm));
    }
}
