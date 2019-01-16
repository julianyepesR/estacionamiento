package estacionamiento.persistencia.sistema;

import estacionamiento.dominio.controladores.VehiculoController;
import estacionamiento.persistencia.conexion.ConexionJPA;
import estacionamiento.persistencia.repositorio.VehiculoPersistence;

import javax.persistence.EntityManager;

public class SistemaDePersistencia {

    private EntityManager entityManager;

    public SistemaDePersistencia(){
        this.entityManager = new ConexionJPA().createEntityManager();
    }

    public VehiculoController obtenerVehiculoController(){
        return new VehiculoController(new VehiculoPersistence(entityManager));
    }

    public VehiculoPersistence obtenerVehiculoPersistence(){
        return new VehiculoPersistence(entityManager);
    }

    public void iniciar() {
        entityManager.getTransaction().begin();
    }

    public void finalizar() {
        entityManager.getTransaction().commit();
    }
}
