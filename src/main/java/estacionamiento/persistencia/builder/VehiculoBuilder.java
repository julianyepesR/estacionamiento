package estacionamiento.persistencia.builder;

import estacionamiento.dominio.modulos.*;
import estacionamiento.persistencia.entidad.*;

public class VehiculoBuilder {

	   public static Vehiculo convertirADominio(VehiculoEntity vehiculoEntity){
	        Vehiculo vehiculo = null;
	        if(vehiculoEntity != null){
	            vehiculo = new Vehiculo(
	                    vehiculoEntity.getCilindraje(),
	                    vehiculoEntity.getCosto(),
	                    vehiculoEntity.getPlaca(),
	                    vehiculoEntity.getFechaDeSalida(),
	                    vehiculoEntity.getFechaDeEntrada(),
	                    vehiculoEntity.getEstadoActual(),
	                    vehiculoEntity.getTipoDeVehiculo()
	            );
	        }
	        return vehiculo;
	    }

	    public static VehiculoEntity convertirAEntidad(Vehiculo vehiculo){
	        VehiculoEntity vehiculoEntity = new VehiculoEntity();

	        if(vehiculo != null){
	            vehiculoEntity.setCilindraje(vehiculo.getCilindraje());
	            vehiculoEntity.setCosto(vehiculo.getCilindraje());
	            vehiculoEntity.setEstadoActual(vehiculo.getEstadoActual());
	            vehiculoEntity.setFechaDeEntrada(vehiculo.getFechaDeEntrada());
	            vehiculoEntity.setPlaca(vehiculo.getPlaca());
	            vehiculoEntity.setTipoDeVehiculo(vehiculo.getTipoDeVehiculo());
	        }

	        return vehiculoEntity;
	    }
	
}
