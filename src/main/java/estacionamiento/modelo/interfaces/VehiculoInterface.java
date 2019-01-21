package estacionamiento.modelo.interfaces;

import java.io.IOException;
import java.rmi.RemoteException;

public interface VehiculoInterface {

    String ingresoDeVehiculo(String body) throws IOException;

    String calcularCosto(String body);

    String cargarPaginaInicial();

    String obtenerTRM() throws RemoteException;

}
