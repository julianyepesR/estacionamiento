package dataBuilder;

import estacionamiento.dominio.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.dominio.enumeraciones.VehiculoEnum;
import estacionamiento.dominio.modulos.Vehiculo;
import estacionamiento.persistencia.repositorio.VehiculoPersistence;

import java.util.Calendar;
import java.util.Date;

public class TestUtils {

    private VehiculoPersistence vehiculoPersistence;

    public TestUtils(VehiculoPersistence vehiculoPersistence){
        this.vehiculoPersistence = vehiculoPersistence;
    }

    public void crearVehiculo(VehiculoEnum tipoDeVehiculo,String placa, int cilindraje,int horas, int dias){
        Date hoy = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hoy);
        calendar.add(Calendar.HOUR_OF_DAY,-horas);
        calendar.add(Calendar.DAY_OF_MONTH,-dias);
        Vehiculo vehiculo = new Vehiculo(cilindraje,0,placa,null,calendar.getTime(), EstadoVehiculoEnum.EN_DEUDA,tipoDeVehiculo);
        vehiculoPersistence.agregarVehiculo(vehiculo);
    }

    public void llenarParqueadero(VehiculoEnum tipoDeVehiculo){
        if(VehiculoEnum.CARRO.equals(tipoDeVehiculo)){
            for(int i = 0; i<20; i++){
                crearVehiculo(tipoDeVehiculo,"TTT1"+i,0,1,0);
            }
        }else{
            for(int i = 0; i<10; i++){
                crearVehiculo(tipoDeVehiculo,"TTT1"+i,300,1,0);
            }
        }

    }


}
