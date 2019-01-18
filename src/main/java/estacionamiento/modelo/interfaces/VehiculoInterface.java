package estacionamiento.modelo.interfaces;

import java.io.IOException;

public interface VehiculoInterface {

    String ingresoDeVehiculo(String body) throws IOException;

    String calcularCosto(String body);

    String cargarPaginaInicial();

}
