package estacionamiento.dominio.interfaces;

import estacionamiento.dominio.modulos.*;
import estacionamiento.persistencia.entidad.VehiculoEntity;

import java.util.List;

public interface VehiculoInterface {

	Vehiculo obtenerVehiculo(String placa);

	VehiculoEntity obtenerVehiculoEntity(String placa);

	List<Vehiculo> obtenerListaDeVehiculos();

	List<Vehiculo> obtenerListaDeMotos();

	List<Vehiculo> obtenerListaDeCarros();

	void agregarVehiculo(Vehiculo vehiculo);

	void cambiarEstadoDeVehiculo(VehiculoEntity vehiculoEntity,long costo);
}
