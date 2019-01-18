package estacionamiento.controladores;

import estacionamiento.modelo.interfaces.VehiculoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class VehiculoController {

	@Autowired
	private VehiculoInterface vehiculoInterface;

	@PostMapping("/ingresoDeVehiculo")
	public String ingresoDeVehiculo(@RequestBody String body) throws IOException {
		return vehiculoInterface.ingresoDeVehiculo(body);
	}

	@PostMapping("/calcularCosto")
	public String calcularCosto(@RequestBody String body) throws IOException {
		return vehiculoInterface.calcularCosto(body);
	}

	@GetMapping("/")
    public String cargarPaginaInicial(){
        return vehiculoInterface.cargarPaginaInicial();
    }



}