package estacionamiento.persistencia.repositorio;

import estacionamiento.dominio.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.dominio.enumeraciones.VehiculoEnum;
import estacionamiento.persistencia.builder.VehiculoBuilder;
import estacionamiento.persistencia.entidad.VehiculoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import estacionamiento.dominio.interfaces.VehiculoInterface;
import estacionamiento.dominio.modulos.Vehiculo;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
@Transactional
public class VehiculoPersistence implements VehiculoInterface{

    private static final String FIND_BY_PLACA = "Vehiculo.findByPlaca";
    private static final String FIND_ALL = "Vehiculo.findAll";
    private static final String FIND_ALL_MOTOS = "Vehiculo.findallMotos";
    private static final String FIND_ALL_CARROS = "Vehiculo.findallCarros";
    private static final EstadoVehiculoEnum ESTADO_FUERA = EstadoVehiculoEnum.EN_DEUDA;
    private static final String ESTADO_ACTUAL = "estadoActual";
    private static final String TIPO_DE_VEHICULO = "tipoDeVehiculo";

    Logger logger = LoggerFactory.getLogger(VehiculoPersistence.class);

    private EntityManager entityManager;

    public VehiculoPersistence(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Vehiculo obtenerVehiculo(String placa){

        Query query = entityManager.createNamedQuery(FIND_BY_PLACA).setParameter("placa",placa).setParameter(ESTADO_ACTUAL,ESTADO_FUERA);
        try{
            VehiculoEntity vehiculoEntity = (VehiculoEntity) query.getSingleResult();
            return VehiculoBuilder.convertirADominio(vehiculoEntity);
        }catch (Exception e){
            logger.info("El vehiculo con la placa " + placa + " no se encuentra en el estacionamiento");
            return null;
        }

    }

    @Override
    public VehiculoEntity obtenerVehiculoEntity(String placa){

        Query query = entityManager.createNamedQuery(FIND_BY_PLACA).setParameter("placa",placa).setParameter(ESTADO_ACTUAL,ESTADO_FUERA);
        try{
            return (VehiculoEntity) query.getSingleResult();
        }catch (Exception e){
            logger.info("El vehiculo con la placa " + placa + " no se encuentra en el estacionamiento");
            return null;
        }

    }

    @Override
    public List<Vehiculo> obtenerListaDeVehiculos(){
        List<Vehiculo> listaDeVehiculos = new LinkedList<>();
        Query query = entityManager.createNamedQuery(FIND_ALL).setParameter(ESTADO_ACTUAL,ESTADO_FUERA);
        try{
            List<VehiculoEntity> vehiculoEntityList = (List<VehiculoEntity>) query.getResultList();
            for(VehiculoEntity vehiculoEntity : vehiculoEntityList){
                listaDeVehiculos.add(VehiculoBuilder.convertirADominio(vehiculoEntity));
            }
            return listaDeVehiculos;
        }catch (Exception e){
            logger.info("No hay vehiculos en el parqueadero");
            return listaDeVehiculos;
        }
    }

    @Override
    public List<Vehiculo> obtenerListaDeMotos(){
        List<Vehiculo> listaDeMotos = new LinkedList<>();
        Query query = entityManager.createNamedQuery(FIND_ALL_MOTOS).setParameter(TIPO_DE_VEHICULO, VehiculoEnum.MOTO).setParameter(ESTADO_ACTUAL,ESTADO_FUERA);
        try{
            List<VehiculoEntity> vehiculoEntityList = (List<VehiculoEntity>) query.getResultList();
            for(VehiculoEntity vehiculoEntity : vehiculoEntityList){
                listaDeMotos.add(VehiculoBuilder.convertirADominio(vehiculoEntity));
            }
            return listaDeMotos;
        }catch (Exception e){
            logger.info("No hay motos en el parqueadero");
            return listaDeMotos;
        }
    }

    @Override
    public List<Vehiculo> obtenerListaDeCarros(){
        List<Vehiculo> listaDeCarros = new LinkedList<>();
        Query query = entityManager.createNamedQuery(FIND_ALL_CARROS).setParameter(TIPO_DE_VEHICULO, VehiculoEnum.CARRO).setParameter(ESTADO_ACTUAL,ESTADO_FUERA);
        try{
            List<VehiculoEntity> vehiculoEntityList = (List<VehiculoEntity>) query.getResultList();
            for(VehiculoEntity vehiculoEntity : vehiculoEntityList){
                listaDeCarros.add(VehiculoBuilder.convertirADominio(vehiculoEntity));
            }
            return listaDeCarros;
        }catch (Exception e){
            logger.info("No hay carros en el parqueadero");
            return listaDeCarros;
        }
    }

    @Override
    public void agregarVehiculo(Vehiculo vehiculo){
        entityManager.persist(VehiculoBuilder.convertirAEntidad(vehiculo));

    }

    @Override
    public void cambiarEstadoDeVehiculo(VehiculoEntity vehiculoEntity, long costo){
        vehiculoEntity.setEstadoActual(EstadoVehiculoEnum.FUERA);
        vehiculoEntity.setCosto(costo);
        vehiculoEntity.setFechaDeSalida(new Date());
        entityManager.remove(vehiculoEntity);
    }
}
