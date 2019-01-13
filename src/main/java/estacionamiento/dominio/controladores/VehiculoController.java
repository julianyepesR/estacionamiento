package estacionamiento.dominio.controladores;

import estacionamiento.dominio.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.dominio.enumeraciones.VehiculoEnum;
import estacionamiento.dominio.modulos.Vehiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import estacionamiento.persistencia.repositorio.VehiculoPersistence;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@RestController
public class VehiculoController {

	private static final String CREACION_EXITOSA = "El vehiculo con placa {0} se ha ingresado al sistema exitosamente";
	private static final String CREACION_FALLIDA = "El vehiculo con placa {0} no se ha ingresado al sistema, revise el formulario por favor";

	@Autowired
	private VehiculoPersistence vehiculoPersistence;
	
	public VehiculoController(VehiculoPersistence vehiculoPersistence) {
		this.vehiculoPersistence = vehiculoPersistence;
	}

	@GetMapping("/ingresoDeVehiculo/{cliente}/{placa}/{cilindraje}")
	public String ingresoDeVehiculo(@PathVariable String cliente, @PathVariable String placa, @PathVariable int cilindraje){
		Vehiculo vehiculo = new Vehiculo();
		String mensaje;
		if(cilindraje == 0){
			vehiculo.setCilindraje(cilindraje);
			vehiculo.setCliente(cliente);
			vehiculo.setPlaca(placa);
			vehiculo.setTipoDeVehiculo(VehiculoEnum.CARRO);
			vehiculo.setFechaDeEntrada(new Date());
			vehiculo.setEstadoActual(EstadoVehiculoEnum.EN_DEUDA);
		}else{
			if(validacionDeMotos()){
				vehiculo.setCilindraje(cilindraje);
				vehiculo.setCliente(cliente);
				vehiculo.setPlaca(placa);
				vehiculo.setTipoDeVehiculo(VehiculoEnum.MOTO);
				vehiculo.setFechaDeEntrada(new Date());
				vehiculo.setEstadoActual(EstadoVehiculoEnum.EN_DEUDA);
			}
		}
		if(vehiculo!=null){
			vehiculoPersistence.agregarVehiculo(vehiculo);
			System.out.println(vehiculoPersistence.obtenerVehiculo(placa));
			mensaje = MessageFormat.format(CREACION_EXITOSA,placa);
		}else{
			mensaje = MessageFormat.format(CREACION_FALLIDA,placa);
		}

		System.out.println(mensaje);
		return mensaje;
	}

	private Boolean validacionDeMotos(){
		return false;
	}

	@RequestMapping("/")
	public String getString() {
		String mensaje = "asdasdasdas";
		System.out.print(mensaje);
		return mensaje;
	}

	@RequestMapping("/todos")
	public String getTodos() {
		List<Vehiculo> listaDeVehiculos = vehiculoPersistence.obtenerListaDeVehiculos();
		String mensaje = "";
		for(Vehiculo vehiculo : listaDeVehiculos){
			mensaje += vehiculo.getCliente() + vehiculo.getPlaca();
			mensaje += "\n";
		}

		System.out.print(mensaje);
		return mensaje;
	}
	
	@RequestMapping("/hola")
	public String getOtherString() {
		String mensaje = "Hoooolaaaa";
		System.out.print(mensaje);
		return mensaje;
	}
	
}
