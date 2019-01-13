package estacionamiento.persistencia.conexion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConexionJPA {
    private static final String ESTACIONAMIENTO = "estacionamiento";
    private static EntityManagerFactory entityManagerFactory;

    public ConexionJPA(){
        entityManagerFactory = Persistence.createEntityManagerFactory(ESTACIONAMIENTO);
    }

    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
