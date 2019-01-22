package testUtils;

import estacionamiento.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.enumeraciones.TipoVehiculoEnum;
import estacionamiento.modelo.entidad.VehiculoEntity;
import estacionamiento.modelo.servicios.PersistenciaImplementacion;

import java.util.Calendar;
import java.util.Date;

public class TestUtils {

    private PersistenciaImplementacion persistenciaImplementacion;

    public TestUtils(PersistenciaImplementacion persistenciaImplementacion){
        this.persistenciaImplementacion = persistenciaImplementacion;
    }

    public void crearVehiculo(TipoVehiculoEnum tipoDeVehiculo, String placa, int cilindraje, int horas, int dias){
        Date hoy = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hoy);
        calendar.add(Calendar.HOUR_OF_DAY,-horas);
        calendar.add(Calendar.DAY_OF_MONTH,-dias);
        VehiculoEntity vehiculoEntity = new VehiculoEntity(cilindraje,0,placa,null,calendar.getTime(), EstadoVehiculoEnum.EN_DEUDA,tipoDeVehiculo);
        persistenciaImplementacion.agregarVehiculo(vehiculoEntity);
    }

    public void llenarParqueadero(TipoVehiculoEnum tipoDeVehiculo){
        if(TipoVehiculoEnum.CARRO.equals(tipoDeVehiculo)){
            for(int i = 0; i<20; i++){
                crearVehiculo(tipoDeVehiculo,"TTT1"+i,0,1,0);
            }
        }else{
            for(int i = 0; i<10; i++){
                crearVehiculo(tipoDeVehiculo,"TTT1"+i,300,1,0);
            }
        }
    }

    public void resetDB(){ persistenciaImplementacion.vehiculoPersistence.deleteAll(); }
}
