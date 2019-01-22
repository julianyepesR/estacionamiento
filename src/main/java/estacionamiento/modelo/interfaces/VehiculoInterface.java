package estacionamiento.modelo.interfaces;

public interface VehiculoInterface {

    String ingresoDeVehiculo(String body);

    String calcularCosto(String body);

    String cargarPaginaInicial();

    String obtenerTRM();

}
