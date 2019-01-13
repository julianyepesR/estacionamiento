package estacionamiento.dominio.interfaces;

import estacionamiento.dominio.modulos.*;

import java.util.List;

public interface VehiculoInterface {
	
	Vehiculo obtenerVehiculo(String placa);

	List<Vehiculo> obtenerListaDeVehiculos();

	List<Vehiculo> obtenerListaDeMotos();

	List<Vehiculo> obtenerListaDeCarros();

	void agregarVehiculo(Vehiculo vehiculo);

}
