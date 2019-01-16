package integracion;

import dataBuilder.TestUtils;
import estacionamiento.dominio.controladores.VehiculoController;
import estacionamiento.dominio.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.dominio.enumeraciones.VehiculoEnum;
import estacionamiento.dominio.modulos.Vehiculo;
import estacionamiento.persistencia.repositorio.VehiculoPersistence;
import estacionamiento.persistencia.sistema.SistemaDePersistencia;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


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

    @Autowired
    private VehiculoPersistence vehiculoPersistence;
    private VehiculoController vehiculoController;
    private SistemaDePersistencia sistemaDePersistencia;

    @Before
    public void init(){
        sistemaDePersistencia = new SistemaDePersistencia();

        vehiculoController = sistemaDePersistencia.obtenerVehiculoController();
        vehiculoPersistence = sistemaDePersistencia.obtenerVehiculoPersistence();

        sistemaDePersistencia.iniciar();
    }

    @After
    public void finish(){
        sistemaDePersistencia.finalizar();
    }

    @Test
    public void agregarCarro(){
        // arrange

        // act
        String jsonData = "{'tipoDeVehiculo': "+TIPO_DE_VEHICULO+",'cilindraje': "+CILINDRAJE+",'placa': "+PLACA_B+"}";
        vehiculoController.ingresoDeVehiculo(jsonData);
        Vehiculo vehiculo = vehiculoPersistence.obtenerVehiculo(PLACA_B);

        // assert
        assertEquals(vehiculo.getCilindraje(),CILINDRAJE);
        assertEquals(vehiculo.getTipoDeVehiculo(), VehiculoEnum.CARRO);
        assertEquals(vehiculo.getEstadoActual(), EstadoVehiculoEnum.EN_DEUDA);
    }

    @Test
    public void agregarCarroConA(){
        // arrange
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaActual = calendar.get(Calendar.DAY_OF_WEEK);

        // act
        String jsonData = "{'tipoDeVehiculo': "+TIPO_DE_VEHICULO+",'cilindraje': "+CILINDRAJE+",'placa': "+PLACA_A+"}";
        vehiculoController.ingresoDeVehiculo(jsonData);
        Vehiculo vehiculo = vehiculoPersistence.obtenerVehiculo(PLACA_A);

        if( (diaActual == 1 || diaActual == 2) ){
            // assert
            assertEquals(vehiculo.getCilindraje(),CILINDRAJE);
            assertEquals(vehiculo.getTipoDeVehiculo(), VehiculoEnum.CARRO);
            assertEquals(vehiculo.getEstadoActual(), EstadoVehiculoEnum.EN_DEUDA);
        } else {
            // assert
            assertNull(vehiculo);
        }
    }

    @Test
    public void calcularCostoCarro(){
        // arrange
        TestUtils testUtils = new TestUtils(vehiculoPersistence);
        testUtils.crearVehiculo(VehiculoEnum.CARRO,PLACA_C,CILINDRAJE,3,1);
        testUtils.crearVehiculo(VehiculoEnum.CARRO,PLACA_D,CILINDRAJE,8,0);

        // act
        String jsonData1 = "{'placa': "+PLACA_C+"}";
        String jsonData2 = "{'placa': "+PLACA_D+"}";
        String costo = vehiculoController.calcularCosto(jsonData1);
        String costo2 = vehiculoController.calcularCosto(jsonData2);

        // assert
        assertEquals("11000",costo);
        assertEquals("8000",costo2);
        assertEquals(null,vehiculoPersistence.obtenerVehiculo(PLACA_C));
        assertEquals(null,vehiculoPersistence.obtenerVehiculo(PLACA_D));
    }

    @Test
    public void errorEstacionamientoLleno(){
        // arrange
        TestUtils testUtils = new TestUtils(vehiculoPersistence);
        testUtils.llenarParqueadero(VehiculoEnum.CARRO);

        // act
        String jsonData = "{'tipoDeVehiculo': "+TIPO_DE_VEHICULO+",'cilindraje': "+CILINDRAJE+",'placa': "+PLACA_B+"}";
        vehiculoController.ingresoDeVehiculo(jsonData);
        Vehiculo vehiculo = vehiculoPersistence.obtenerVehiculo(PLACA_B);

        // assert
        assertNull(vehiculo);
    }

    @Test
    public void paginaInicial(){
        // arrange
        TestUtils testUtils = new TestUtils(vehiculoPersistence);
        testUtils.llenarParqueadero(VehiculoEnum.CARRO);
        testUtils.llenarParqueadero(VehiculoEnum.MOTO);

        // act
        String listadoJson = vehiculoController.cargarPaginaInicial();
        System.out.println(listadoJson);

        // assert
        assertTrue(!"".equals(listadoJson));
    }
}
