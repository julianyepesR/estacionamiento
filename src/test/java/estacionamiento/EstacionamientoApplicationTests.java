package estacionamiento;

import estacionamiento.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.enumeraciones.TipoVehiculoEnum;
import estacionamiento.modelo.entidad.VehiculoEntity;
import estacionamiento.modelo.servicios.PersistenciaImplementacion;
import estacionamiento.modelo.servicios.VehiculoImplementacion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EstacionamientoApplicationTests {

	private static final String PLACA_A = "AAA123";
	private static final String PLACA_B = "BBB123";
	private static final String PLACA_C = "CCC123";
	private static final int CILINDRAJE_MENOR = 500;
	private static final int CILINDRAJE_MAYOR = 501;
	private static final String TIPO_DE_VEHICULO = "Moto";
	private static final String CARRO = "Carro";

	@Autowired
	PersistenciaImplementacion persistenciaImplementacion;

	@Test
	public void agregarMoto(){
		// arrange
		VehiculoImplementacion vehiculoImplementacion = new VehiculoImplementacion();
		String jsonData = "{\"tipoDeVehiculo\": \""+TIPO_DE_VEHICULO+"\",\"cilindraje\": \""+CILINDRAJE_MENOR+"\",\"placa\": \""+PLACA_B+"\"}";
		vehiculoImplementacion.ingresoDeVehiculo(jsonData);

		// act
		VehiculoEntity vehiculoEntity1 = persistenciaImplementacion.obtenerVehiculoEntity(PLACA_B);

		// assert
		assertEquals(vehiculoEntity1.getCilindraje(),CILINDRAJE_MENOR);
		assertEquals(vehiculoEntity1.getTipoDeVehiculo(), TipoVehiculoEnum.MOTO);
		assertEquals(vehiculoEntity1.getEstadoActual(), EstadoVehiculoEnum.EN_DEUDA);
	}

}

