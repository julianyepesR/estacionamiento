package estacionamiento.modelo.servicios;

import estacionamiento.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.enumeraciones.TipoVehiculoEnum;
import estacionamiento.modelo.entidad.VehiculoEntity;
import estacionamiento.modelo.interfaces.PersistenciaInterface;
import estacionamiento.modelo.repositorio.VehiculoPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PersistenciaImplementacion implements PersistenciaInterface {

    @Autowired
    public VehiculoPersistence vehiculoPersistence;

    @Override
    public VehiculoEntity obtenerVehiculoEntity(String placa) {
        return vehiculoPersistence.obtenerVehiculoPorPlaca(EstadoVehiculoEnum.EN_DEUDA,placa);
    }

    @Override
    public List<VehiculoEntity>
    obtenerListaDeVehiculos() {
        return vehiculoPersistence.obtenerTodosLosVehiculos(EstadoVehiculoEnum.EN_DEUDA);
    }

    @Override
    public List<VehiculoEntity> obtenerListaDeMotos() {
        return vehiculoPersistence.obtenerTodasLasMotos(TipoVehiculoEnum.MOTO,EstadoVehiculoEnum.EN_DEUDA);
    }

    @Override
    public List<VehiculoEntity> obtenerListaDeCarros() {
        return vehiculoPersistence.obtenerTodosLosCarros(TipoVehiculoEnum.CARRO,EstadoVehiculoEnum.EN_DEUDA);
    }

    @Override
    public void agregarVehiculo(VehiculoEntity vehiculoEntity) {
        vehiculoPersistence.save(vehiculoEntity);
    }

    @Override
    public void cambiarEstadoDeVehiculo(VehiculoEntity vehiculoEntity, long costo) {
        vehiculoEntity.setEstadoActual(EstadoVehiculoEnum.FUERA);
        vehiculoEntity.setCosto(costo);
        vehiculoEntity.setFechaDeSalida(new Date());
        vehiculoPersistence.save(vehiculoEntity);
    }
}
