package estacionamiento.modelo.interfaces;

import estacionamiento.transversal.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.transversal.enumeraciones.TipoVehiculoEnum;
import estacionamiento.modelo.entidades.VehiculoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoPersistence extends CrudRepository<VehiculoEntity,Long> {

    @Query("SELECT vehiculo FROM VehiculoEntity vehiculo WHERE vehiculo.estadoActual = :estadoActual")
    List<VehiculoEntity> obtenerTodosLosVehiculos(@Param("estadoActual") EstadoVehiculoEnum estadoActual);

    @Query("SELECT vehiculo FROM VehiculoEntity vehiculo WHERE vehiculo.placa = :placa AND vehiculo.estadoActual = :estadoActual")
    VehiculoEntity obtenerVehiculoPorPlaca(@Param("estadoActual") EstadoVehiculoEnum estadoActual, @Param("placa") String placa);

    @Query("SELECT vehiculo FROM VehiculoEntity vehiculo WHERE vehiculo.tipoDeVehiculo = :tipoDeVehiculo AND vehiculo.estadoActual = :estadoActual")
    List<VehiculoEntity> obtenerTodasLasMotos(@Param("tipoDeVehiculo") TipoVehiculoEnum tipoVehiculoEnum, @Param("estadoActual") EstadoVehiculoEnum estadoActual);

    @Query("SELECT vehiculo FROM VehiculoEntity vehiculo WHERE vehiculo.tipoDeVehiculo = :tipoDeVehiculo AND vehiculo.estadoActual = :estadoActual")
    List<VehiculoEntity> obtenerTodosLosCarros(@Param("tipoDeVehiculo") TipoVehiculoEnum tipoVehiculoEnum, @Param("estadoActual") EstadoVehiculoEnum estadoActual);
}
