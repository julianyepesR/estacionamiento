package estacionamiento.controladores;

import estacionamiento.modelo.interfaces.VehiculoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.rmi.RemoteException;

@RestController
public class VehiculoController {

	@Autowired
	private VehiculoInterface vehiculoInterface;

	@PostMapping("/ingresoDeVehiculo")
	public String ingresoDeVehiculo(@RequestBody String body) throws IOException {
		return vehiculoInterface.ingresoDeVehiculo(body);
	}

	@PostMapping("/calcularCosto")
	public String calcularCosto(@RequestBody String body) { return vehiculoInterface.calcularCosto(body); }

	@GetMapping("/")
    public String cargarPaginaInicial(){
        return vehiculoInterface.cargarPaginaInicial();
    }

	@GetMapping("/TRM")
	public String obtenerTRM() throws RemoteException { return vehiculoInterface.obtenerTRM(); }

}
