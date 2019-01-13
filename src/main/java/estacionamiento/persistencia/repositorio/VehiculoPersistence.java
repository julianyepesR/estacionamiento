package estacionamiento.persistencia.repositorio;

import estacionamiento.dominio.enumeraciones.VehiculoEnum;
import estacionamiento.persistencia.builder.VehiculoBuilder;
import estacionamiento.persistencia.entidad.VehiculoEntity;
import org.springframework.stereotype.Component;
import estacionamiento.dominio.interfaces.VehiculoInterface;
import estacionamiento.dominio.modulos.Vehiculo;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

@Component
@Transactional
public class VehiculoPersistence implements VehiculoInterface{

    private static final String FIND_BY_PLACA = "Vehiculo.findByPlaca";
    private static final String FIND_ALL = "Vehiculo.findAll";
    private static final String FIND_ALL_MOTOS = "Vehiculo.findallMotos";
    private static final String FIND_ALL_CARROS = "Vehiculo.findallCarros";

    private EntityManager entityManager;

    public VehiculoPersistence(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Vehiculo obtenerVehiculo(String placa){
        Query query = entityManager.createNamedQuery(FIND_BY_PLACA).setParameter("placa",placa);
        VehiculoEntity vehiculoEntity = (VehiculoEntity) query.getSingleResult();
        return VehiculoBuilder.convertirADominio(vehiculoEntity);
    }

    @Override
    public List<Vehiculo> obtenerListaDeVehiculos(){
        List<Vehiculo> listaDeVehiculos = new LinkedList<>();
        Query query = entityManager.createNamedQuery(FIND_ALL);
        List<VehiculoEntity> vehiculoEntityList = (List<VehiculoEntity>) query.getResultList();
        for(VehiculoEntity vehiculoEntity : vehiculoEntityList){
            listaDeVehiculos.add(VehiculoBuilder.convertirADominio(vehiculoEntity));
        }
        return listaDeVehiculos;
    }

    @Override
    public List<Vehiculo> obtenerListaDeMotos(){
        List<Vehiculo> listaDeMotos = new LinkedList<>();
        Query query = entityManager.createNamedQuery(FIND_ALL_MOTOS).setParameter("tipoDeVehiculo", VehiculoEnum.MOTO);
        List<VehiculoEntity> vehiculoEntityList = (List<VehiculoEntity>) query.getResultList();
        for(VehiculoEntity vehiculoEntity : vehiculoEntityList){
            listaDeMotos.add(VehiculoBuilder.convertirADominio(vehiculoEntity));
        }
        return listaDeMotos;
    }

    @Override
    public List<Vehiculo> obtenerListaDeCarros(){
        List<Vehiculo> listaDeCarros = new LinkedList<>();
        Query query = entityManager.createNamedQuery(FIND_ALL_CARROS).setParameter("tipoDeVehiculo", VehiculoEnum.CARRO);
        List<VehiculoEntity> vehiculoEntityList = (List<VehiculoEntity>) query.getResultList();
        for(VehiculoEntity vehiculoEntity : vehiculoEntityList){
            listaDeCarros.add(VehiculoBuilder.convertirADominio(vehiculoEntity));
        }
        return listaDeCarros;
    }

    @Override
    public void agregarVehiculo(Vehiculo vehiculo){
        entityManager.persist(VehiculoBuilder.convertirAEntidad(vehiculo));
    }
}
