package integracion;

import dataBuilder.TestUtils;
import estacionamiento.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.enumeraciones.TipoVehiculoEnum;
import estacionamiento.modelo.entidad.VehiculoEntity;
import estacionamiento.modelo.repositorio.VehiculoPersistence;
import estacionamiento.modelo.servicios.PersistenciaImplementacion;
import estacionamiento.modelo.servicios.VehiculoImplementacion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class CarroTest {

    private static final String PLACA_A = "AAA321";
    private static final String PLACA_B = "BBB321";
    private static final String PLACA_C = "CCC321";
    private static final String PLACA_D = "DDD321";
    private static final int CILINDRAJE = 0;
    private static final String TIPO_DE_VEHICULO = "Carro";

    @InjectMocks
    private PersistenciaImplementacion persistenciaImplementacion;

    @Mock
    private VehiculoPersistence vehiculoPersistence;

    @Test
    public void agregarCarro(){
        // arrange
        VehiculoImplementacion vehiculoImplementacion = new VehiculoImplementacion(persistenciaImplementacion);
        TestUtils testUtils = new TestUtils(persistenciaImplementacion);
        VehiculoEntity vehiculoEntity = testUtils.crearVehiculo(TipoVehiculoEnum.CARRO,PLACA_C,CILINDRAJE,10,0);
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE+"\",\"placa\": \""+PLACA_B+"\"}";
        vehiculoImplementacion.ingresoDeVehiculo(jsonData);

        // act
        when(this.vehiculoPersistence.obtenerVehiculoPorPlaca(EstadoVehiculoEnum.EN_DEUDA,PLACA_B)).thenReturn(vehiculoEntity);
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
        VehiculoImplementacion vehiculoImplementacion = new VehiculoImplementacion(persistenciaImplementacion);

        // act
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE+"\",\"placa\": \""+PLACA_A+"\"}";
        vehiculoImplementacion.ingresoDeVehiculo(jsonData);
        VehiculoEntity vehiculoEntity = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_A);

        if( !(diaActual == 1 || diaActual == 2) ){
            // assert
            assertNull(vehiculoEntity);
        }
    }

    @Test
    public void calcularCostoCarro(){
        // arrange
        TestUtils testUtils = new TestUtils(persistenciaImplementacion);
        VehiculoImplementacion vehiculoImplementacion = new VehiculoImplementacion(persistenciaImplementacion);
        VehiculoEntity vehiculoEntity = testUtils.crearVehiculo(TipoVehiculoEnum.CARRO,PLACA_C,CILINDRAJE,3,1);
        when(this.vehiculoPersistence.obtenerVehiculoPorPlaca(EstadoVehiculoEnum.EN_DEUDA,PLACA_C)).thenReturn(vehiculoEntity);

        // act
        String jsonData1 = "{\"placa\": \""+PLACA_C+"\"}";
        String costo = vehiculoImplementacion.calcularCosto(jsonData1);

        // assert
        assertEquals("11000",costo);
    }

    @Test
    public void errorEstacionamientoLleno(){
        // arrange
        VehiculoImplementacion vehiculoImplementacion = new VehiculoImplementacion(persistenciaImplementacion);
        TestUtils testUtils = new TestUtils(persistenciaImplementacion);
        testUtils.llenarParqueadero(TipoVehiculoEnum.CARRO);

        // act
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE+"\",\"placa\": \""+PLACA_B+"\"}";
        vehiculoImplementacion.ingresoDeVehiculo(jsonData);
        VehiculoEntity vehiculo = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_B);

        // assert
        assertNull(vehiculo);
    }

    @Test
    public void paginaInicial(){
        // arrange
        VehiculoImplementacion vehiculoImplementacion = new VehiculoImplementacion(persistenciaImplementacion);
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
