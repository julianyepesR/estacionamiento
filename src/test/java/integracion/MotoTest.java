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
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
public class MotoTest {

    private static final String PLACA_A = "AAA123";
    private static final String PLACA_B = "BBB123";
    private static final String PLACA_C = "CCC123";
    private static final int CILINDRAJE_MENOR = 500;
    private static final int CILINDRAJE_MAYOR = 501;
    private static final String TIPO_DE_VEHICULO = "Moto";
    private static final String CARRO = "Carro";

    @Mock
    private  VehiculoPersistence vehiculoPersistence;

    @InjectMocks
    private PersistenciaImplementacion persistenciaImplementacion;

    @org.junit.Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.vehiculoPersistence = persistenciaImplementacion.vehiculoPersistence;
    }

    @Test
    public void agregarMoto(){
        // arrange
        VehiculoImplementacion vehiculoImplementacion = new VehiculoImplementacion(persistenciaImplementacion);
        TestUtils testUtils = new TestUtils(persistenciaImplementacion);
        VehiculoEntity vehiculoEntity = testUtils.crearVehiculo(TipoVehiculoEnum.MOTO,PLACA_C,CILINDRAJE_MENOR,10,0);
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE_MENOR+"\",\"placa\": \""+PLACA_B+"\"}";
        vehiculoImplementacion.ingresoDeVehiculo(jsonData);

        // act
        when(this.vehiculoPersistence.obtenerVehiculoPorPlaca(EstadoVehiculoEnum.EN_DEUDA,PLACA_B)).thenReturn(vehiculoEntity);
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
        VehiculoImplementacion vehiculoImplementacion = new VehiculoImplementacion(persistenciaImplementacion);

        // act
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE_MENOR+"\",\"placa\": \""+PLACA_A+"\"}";
        vehiculoImplementacion.ingresoDeVehiculo(jsonData);
        VehiculoEntity vehiculoEntity = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_A);

        if( !(diaActual == 1 || diaActual == 2) ){
            // assert
            assertNull(vehiculoEntity);
        }
    }

    @Test
    public void calcularCostoMoto(){
        // arrange
        TestUtils testUtils = new TestUtils(persistenciaImplementacion);
        VehiculoImplementacion vehiculoImplementacion = new VehiculoImplementacion(persistenciaImplementacion);
        VehiculoEntity vehiculoEntity = testUtils.crearVehiculo(TipoVehiculoEnum.MOTO,PLACA_C,CILINDRAJE_MAYOR,10,0);
        when(this.vehiculoPersistence.obtenerVehiculoPorPlaca(EstadoVehiculoEnum.EN_DEUDA,PLACA_C)).thenReturn(vehiculoEntity);

        // act
        String jsonData1 = "{\"placa\": \""+PLACA_C+"\"}";
        String costo = vehiculoImplementacion.calcularCosto(jsonData1);

        // assert
        assertEquals("6000",costo);
    }

    @Test
    public void errorEstacionamientoLleno(){
        // arrange
        VehiculoImplementacion vehiculoImplementacion = new VehiculoImplementacion(persistenciaImplementacion);
        TestUtils testUtils = new TestUtils(persistenciaImplementacion);
        testUtils.llenarParqueadero(TipoVehiculoEnum.MOTO);

        // act
        String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE_MENOR+"\",\"placa\": \""+PLACA_B+"\"}";
        vehiculoImplementacion.ingresoDeVehiculo(jsonData);
        VehiculoEntity vehiculoEntity = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_B);

        // assert
        assertNull(vehiculoEntity);
    }

    @Test
    public void validacionDeParqueaderoConEspacio(){
        //arrange
        VehiculoImplementacion vehiculoImplementacion = new VehiculoImplementacion(persistenciaImplementacion);
        //act
        Boolean validacionCarro = vehiculoImplementacion.validacionCapacidad(CARRO);
        Boolean validacionMoto = vehiculoImplementacion.validacionCapacidad(TIPO_DE_VEHICULO);

        //assert
        assertFalse(validacionCarro);
        assertFalse(validacionMoto);
    }

}
