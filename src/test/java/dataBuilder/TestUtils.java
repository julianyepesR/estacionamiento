package dataBuilder;

import estacionamiento.ObjetosJSON.Vehiculo;
import estacionamiento.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.enumeraciones.TipoVehiculoEnum;
import estacionamiento.modelo.entidad.VehiculoEntity;
import estacionamiento.modelo.servicios.PersistenciaImplementacion;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TestUtils {

    @Autowired
    private PersistenciaImplementacion persistenciaImplementacion;

    public TestUtils(PersistenciaImplementacion persistenciaImplementacion){
        this.persistenciaImplementacion = persistenciaImplementacion;
    }

    public VehiculoEntity crearVehiculo(TipoVehiculoEnum tipoDeVehiculo, String placa, int cilindraje, int horas, int dias){
        Date hoy = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hoy);
        calendar.add(Calendar.HOUR_OF_DAY,-horas);
        calendar.add(Calendar.DAY_OF_MONTH,-dias);
        VehiculoEntity vehiculoEntity = new VehiculoEntity(cilindraje,0,placa,null,calendar.getTime(), EstadoVehiculoEnum.EN_DEUDA,tipoDeVehiculo);
        persistenciaImplementacion.agregarVehiculo(vehiculoEntity);
        return vehiculoEntity;
    }

    public void llenarParqueadero(TipoVehiculoEnum tipoDeVehiculo){
        List<VehiculoEntity> listaDeVehiculos = new LinkedList<>();
        if(TipoVehiculoEnum.CARRO.equals(tipoDeVehiculo)){
            for(int i = 0; i<20; i++){
                VehiculoEntity vehiculoEntity = crearVehiculo(tipoDeVehiculo,"TTT1"+i,0,1,0);
                listaDeVehiculos.add(vehiculoEntity);
            }
        }else{
            for(int i = 0; i<10; i++){
                VehiculoEntity vehiculoEntity = crearVehiculo(tipoDeVehiculo,"TTT1"+i,300,1,0);
                listaDeVehiculos.add(vehiculoEntity);
            }
        }
        return;
    }


}
