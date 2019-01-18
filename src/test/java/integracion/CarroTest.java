package integracion;

import dataBuilder.TestUtils;
import estacionamiento.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.enumeraciones.TipoVehiculoEnum;
import estacionamiento.modelo.entidad.VehiculoEntity;
import estacionamiento.modelo.interfaces.VehiculoInterface;
import estacionamiento.modelo.servicios.PersistenciaImplementacion;
import estacionamiento.modelo.servicios.VehiculoImplementacion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class CarroTest {

    private static final String PLACA_A = "AAA321";
    private static final String PLACA_B = "BBB321";
    private static final String PLACA_C = "CCC321";
    private static final String PLACA_D = "DDD321";
    private static final int CILINDRAJE = 0;
    private static final String TIPO_DE_VEHICULO = "Carro";

    @Mock
    private VehiculoInterface vehiculoInterface;
    private PersistenciaImplementacion persistenciaImplementacion;

    @InjectMocks
    private VehiculoImplementacion vehiculoImplementacion;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void agregarCarro(){
        // arrange

        // act
        String jsonData = "{'tipoDeVehiculo': "+TIPO_DE_VEHICULO+",'cilindraje': "+CILINDRAJE+",'placa': "+PLACA_B+"}";
        vehiculoImplementacion.ingresoDeVehiculo(jsonData);
        VehiculoEntity vehiculoEntity = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_B);

        // assert
        assertEquals(vehiculoEntity.getCilindraje(),CILINDRAJE);
        assertEquals(vehiculoEntity.getTipoDeVehiculo(), TipoVehiculoEnum.CARRO);
        assertEquals(vehiculoEntity.getEstadoActual(), EstadoVehiculoEnum.EN_DEUDA);
    }

    @Test
    public void agregarCarroConA(){
        // arrange
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaActual = calendar.get(Calendar.DAY_OF_WEEK);

        // act
        String jsonData = "{'tipoDeVehiculo': "+TIPO_DE_VEHICULO+",'cilindraje': "+CILINDRAJE+",'placa': "+PLACA_A+"}";
        vehiculoImplementacion.ingresoDeVehiculo(jsonData);
        VehiculoEntity vehiculoEntity = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_A);

        if( (diaActual == 1 || diaActual == 2) ){
            // assert
            assertEquals(vehiculoEntity.getCilindraje(),CILINDRAJE);
            assertEquals(vehiculoEntity.getTipoDeVehiculo(), TipoVehiculoEnum.CARRO);
            assertEquals(vehiculoEntity.getEstadoActual(), EstadoVehiculoEnum.EN_DEUDA);
        } else {
            // assert
            assertNull(vehiculoEntity);
        }
    }

    @Test
    public void calcularCostoCarro(){
        // arrange
        TestUtils testUtils = new TestUtils(persistenciaImplementacion);
        testUtils.crearVehiculo(TipoVehiculoEnum.CARRO,PLACA_C,CILINDRAJE,3,1);
        testUtils.crearVehiculo(TipoVehiculoEnum.CARRO,PLACA_D,CILINDRAJE,8,0);

        // act
        String jsonData1 = "{'placa': "+PLACA_C+"}";
        String jsonData2 = "{'placa': "+PLACA_D+"}";
        String costo = vehiculoImplementacion.calcularCosto(jsonData1);
        String costo2 = vehiculoImplementacion.calcularCosto(jsonData2);

        // assert
        assertEquals("11000",costo);
        assertEquals("8000",costo2);
        assertNull(persistenciaImplementacion.obtenerVehiculoEntity(PLACA_C));
        assertNull(persistenciaImplementacion.obtenerVehiculoEntity(PLACA_D));
    }

    @Test
    public void errorEstacionamientoLleno(){
        // arrange
        TestUtils testUtils = new TestUtils(persistenciaImplementacion);
        testUtils.llenarParqueadero(TipoVehiculoEnum.CARRO);

        // act
        String jsonData = "{'tipoDeVehiculo': "+TIPO_DE_VEHICULO+",'cilindraje': "+CILINDRAJE+",'placa': "+PLACA_B+"}";
        vehiculoImplementacion.ingresoDeVehiculo(jsonData);
        VehiculoEntity vehiculo = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_B);

        // assert
        assertNull(vehiculo);
    }

    @Test
    public void paginaInicial(){
        // arrange
        TestUtils testUtils = new TestUtils(persistenciaImplementacion);
        testUtils.llenarParqueadero(TipoVehiculoEnum.CARRO);
        testUtils.llenarParqueadero(TipoVehiculoEnum.MOTO);

        // act
        String listadoJson = vehiculoImplementacion.cargarPaginaInicial();
        System.out.println(listadoJson);

        // assert
        assertTrue(!"".equals(listadoJson));
    }
}
