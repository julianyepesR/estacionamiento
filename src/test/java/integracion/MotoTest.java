package integracion;

import static org.junit.Assert.assertEquals;

import dataBuilder.TestUtils;
import estacionamiento.dominio.controladores.VehiculoController;
import estacionamiento.dominio.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.dominio.enumeraciones.VehiculoEnum;
import estacionamiento.dominio.modulos.Vehiculo;
import estacionamiento.persistencia.repositorio.VehiculoPersistence;
import estacionamiento.persistencia.sistema.SistemaDePersistencia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;

public class MotoTest {

    private static final String PLACA_A = "AAA123";
    private static final String PLACA_B = "BBB123";
    private static final String PLACA_C = "CCC123";
    private static final String PLACA_D = "DDD123";
    private static final int CILINDRAJE_MENOR = 500;
    private static final int CILINDRAJE_MAYOR = 501;
    private static final String TIPO_DE_VEHICULO = "Moto";

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
    public void agregarMoto(){
        // arrange

        // act
        String jsonData = "{'tipoDeVehiculo': "+TIPO_DE_VEHICULO+",'cilindraje': "+CILINDRAJE_MENOR+",'placa': "+PLACA_B+"}";
        vehiculoController.ingresoDeVehiculo(jsonData);
        Vehiculo vehiculo = vehiculoPersistence.obtenerVehiculo(PLACA_B);

        // assert
        assertEquals(vehiculo.getCilindraje(),CILINDRAJE_MENOR);
        assertEquals(vehiculo.getTipoDeVehiculo(), VehiculoEnum.MOTO);
        assertEquals(vehiculo.getEstadoActual(), EstadoVehiculoEnum.EN_DEUDA);
    }

    @Test
    public void agregarMotoConA(){
        // arrange
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaActual = calendar.get(Calendar.DAY_OF_WEEK);

        // act
        String jsonData = "{'tipoDeVehiculo': "+TIPO_DE_VEHICULO+",'cilindraje': "+CILINDRAJE_MENOR+",'placa': "+PLACA_A+"}";
        vehiculoController.ingresoDeVehiculo(jsonData);
        Vehiculo vehiculo = vehiculoPersistence.obtenerVehiculo(PLACA_A);

        if( (diaActual == 1 || diaActual == 2) ){
            // assert
            assertEquals(vehiculo.getCilindraje(),CILINDRAJE_MENOR);
            assertEquals(vehiculo.getTipoDeVehiculo(), VehiculoEnum.MOTO);
            assertEquals(vehiculo.getEstadoActual(), EstadoVehiculoEnum.EN_DEUDA);
        } else {
            // assert
            assertNull(vehiculo);
        }
    }

    @Test
    public void calcularCostoMoto(){
        // arrange
        TestUtils testUtils = new TestUtils(vehiculoPersistence);
        testUtils.crearVehiculo(VehiculoEnum.MOTO,PLACA_C,CILINDRAJE_MAYOR,10,0);
        testUtils.crearVehiculo(VehiculoEnum.MOTO,PLACA_D,CILINDRAJE_MENOR,2,1);

        // act
        String jsonData1 = "{'placa': "+PLACA_C+"}";
        String jsonData2 = "{'placa': "+PLACA_D+"}";
        String costo = vehiculoController.calcularCosto(jsonData1);
        String costo2 = vehiculoController.calcularCosto(jsonData2);

        // assert
        assertEquals("6000",costo);
        assertEquals("5000",costo2);
    }

    @Test
    public void errorEstacionamientoLleno(){
        // arrange
        TestUtils testUtils = new TestUtils(vehiculoPersistence);
        testUtils.llenarParqueadero(VehiculoEnum.MOTO);

        // act
        String jsonData = "{'tipoDeVehiculo': "+TIPO_DE_VEHICULO+",'cilindraje': "+CILINDRAJE_MENOR+",'placa': "+PLACA_B+"}";
        vehiculoController.ingresoDeVehiculo(jsonData);
        Vehiculo vehiculo = vehiculoPersistence.obtenerVehiculo(PLACA_B);

        // assert
        assertNull(vehiculo);
    }

}
