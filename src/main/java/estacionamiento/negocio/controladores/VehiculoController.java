package estacionamiento.negocio.controladores;

import estacionamiento.negocio.interfaces.VehiculoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehiculoController {

	@Autowired
	private VehiculoInterface vehiculoInterface;

	@PostMapping("/ingresoDeVehiculo")
	public String ingresoDeVehiculo(@RequestBody String body) {
		return vehiculoInterface.ingresoDeVehiculo(body);
	}

	@PostMapping("/calcularCosto")
	public String calcularCosto(@RequestBody String body) { return vehiculoInterface.calcularCosto(body); }

	@GetMapping("/")
    public String cargarPaginaInicial(){
        return vehiculoInterface.cargarPaginaInicial();
    }

	@GetMapping("/TRM")
	public String obtenerTRM() { return vehiculoInterface.obtenerTRM(); }

}
