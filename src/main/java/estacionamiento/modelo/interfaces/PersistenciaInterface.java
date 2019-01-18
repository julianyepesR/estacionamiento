package estacionamiento.modelo.interfaces;

import estacionamiento.modelo.entidad.VehiculoEntity;

import java.util.List;

public interface PersistenciaInterface {

	VehiculoEntity obtenerVehiculoEntity(String placa);

	List<VehiculoEntity> obtenerListaDeVehiculos();

	List<VehiculoEntity> obtenerListaDeMotos();

	List<VehiculoEntity> obtenerListaDeCarros();

	void agregarVehiculo(VehiculoEntity vehiculoEntity);

	void cambiarEstadoDeVehiculo(VehiculoEntity vehiculoEntity,long costo);
}
