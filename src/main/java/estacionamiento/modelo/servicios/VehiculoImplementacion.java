package estacionamiento.modelo.servicios;

import co.com.sc.nexura.superfinanciera.action.generic.services.trm.action.TCRMServicesInterfaceProxy;
import co.com.sc.nexura.superfinanciera.action.generic.services.trm.action.TcrmResponse;
import estacionamiento.ObjetosJson.PlacaJson;
import estacionamiento.ObjetosJson.Vehiculo;
import estacionamiento.constantes.Constantes;
import estacionamiento.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.enumeraciones.TipoVehiculoEnum;
import estacionamiento.modelo.entidad.VehiculoEntity;
import estacionamiento.modelo.interfaces.VehiculoInterface;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Service
@Component
public class VehiculoImplementacion implements VehiculoInterface {
    private static final Logger LOGGER = Logger.getLogger(VehiculoImplementacion.class.getName());

    @Autowired
    private PersistenciaImplementacion persistenciaImplementacion;

    @Autowired
    private Constantes constantes;

    @Override
    public String ingresoDeVehiculo(String body) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            Vehiculo vehiculo = objectMapper.readValue(body,Vehiculo.class);

            if(validacionCapacidad(vehiculo.getTipoDeVehiculo())){
                return MessageFormat.format(constantes.PARQUEADERO_LLENO,vehiculo.getTipoDeVehiculo());
            }

            if(validacionDePrimeraLetra(vehiculo.getPlaca())){
                return MessageFormat.format(constantes.SIN_AUTORIZACION,vehiculo.getPlaca());
            }

            if(persistenciaImplementacion.obtenerVehiculoEntity(vehiculo.getPlaca()) != null){
                return MessageFormat.format(constantes.PLACA_REPETIDA,vehiculo.getPlaca());
            }
            return agregarVehiculo(vehiculo.getTipoDeVehiculo(), Integer.valueOf(vehiculo.getCilindraje()), vehiculo.getPlaca());
        }catch (Exception e){
            LOGGER.info(constantes.ERROR_CREACION_VEHICULO + "\n" + e);
            return constantes.CREACION_FALLIDA;
        }
    }

    @Override
    public String calcularCosto(String body) {
        long costo = 0;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PlacaJson placaJson = objectMapper.readValue(body, PlacaJson.class);

            VehiculoEntity vehiculoEntity = persistenciaImplementacion.obtenerVehiculoEntity(placaJson.getPlaca());
            if(vehiculoEntity!=null){
                costo = calcularTotal(vehiculoEntity);
                cambioDeEstadoVehiculo(vehiculoEntity,costo);
            }
            return String.valueOf(costo);
        }catch (Exception e){
            LOGGER.info(constantes.ERROR_CREACION_VEHICULO + "\n" + e);
            return String.valueOf(costo);
        }
    }

    @Override
    public String cargarPaginaInicial() {
        List<VehiculoEntity> listaDeVehiculos = persistenciaImplementacion.obtenerListaDeVehiculos();
        return crearJson(listaDeVehiculos);
    }

    @Override
    public String obtenerTRM(){
        try{
            TCRMServicesInterfaceProxy proxy = new TCRMServicesInterfaceProxy(constantes.TRM_URL);
            TcrmResponse tcrmResponse = proxy.queryTCRM(null);
            return tcrmResponse.getValue() + " " + tcrmResponse.getUnit();
        }catch (Exception e){
            LOGGER.info(constantes.ERROR_TRM + "\n" + e);
            return constantes.CERO;
        }
    }

    private String crearJson(List<VehiculoEntity> listaDeVehiculos){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(constantes.FORMATO_DATE);
        HashMap<String, JSONObject> map = new HashMap<>();
        JSONArray arr = new JSONArray();
        int id = 1;
        for(VehiculoEntity vehiculo : listaDeVehiculos) {
            JSONObject jsonObj=new JSONObject();
            jsonObj.put(constantes.ID, id);
            jsonObj.put(constantes.PLACA, vehiculo.getPlaca());
            jsonObj.put(constantes.TIPO, vehiculo.getTipoDeVehiculo().getTipoDeVehiculo());
            jsonObj.put(constantes.FECHA_INGRESO,simpleDateFormat.format(vehiculo.getFechaDeEntrada()));
            map.put(constantes.JSON + id, jsonObj);
            arr.put(map.get(constantes.JSON + id));
            id++;
        }
        return arr.toString();
    }

    public Boolean validacionDePrimeraLetra(String placa){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaActual = calendar.get(Calendar.DAY_OF_WEEK);
        String primeraLetra = placa.substring(0,1);
        if(constantes.A.equalsIgnoreCase(primeraLetra)){
            return !(diaActual == 1 || diaActual == 2);
        }else{
            return false;
        }
    }

    public Boolean validacionCapacidad(String tipoDeVehiculo){
        if(constantes.CARRO.equals(tipoDeVehiculo)){
            return persistenciaImplementacion.obtenerListaDeCarros().size()==20;
        }else{
            return persistenciaImplementacion.obtenerListaDeMotos().size()==10;
        }
    }

    private String agregarVehiculo(String tipoVehiculo, int cilindraje, String placa){
        if(constantes.CARRO.equals(tipoVehiculo)){
            VehiculoEntity carro = new VehiculoEntity();
            carro.setCilindraje(cilindraje);
            carro.setPlaca(placa);
            carro.setTipoDeVehiculo(TipoVehiculoEnum.CARRO);
            carro.setFechaDeEntrada(new Date());
            carro.setEstadoActual(EstadoVehiculoEnum.EN_DEUDA);
            persistenciaImplementacion.agregarVehiculo(carro);
            return MessageFormat.format(constantes.CREACION_EXITOSA,placa);
        } else if(constantes.MOTO.equals(tipoVehiculo)){
            VehiculoEntity moto = new VehiculoEntity();
            moto.setCilindraje(cilindraje);
            moto.setPlaca(placa);
            moto.setTipoDeVehiculo(TipoVehiculoEnum.MOTO);
            moto.setFechaDeEntrada(new Date());
            moto.setEstadoActual(EstadoVehiculoEnum.EN_DEUDA);
            persistenciaImplementacion.agregarVehiculo(moto);
            return MessageFormat.format(constantes.CREACION_EXITOSA,placa);
        } else {
            return constantes.CREACION_FALLIDA;
        }
    }

    private void cambioDeEstadoVehiculo(VehiculoEntity vehiculoEntity, long costo){
        persistenciaImplementacion.cambiarEstadoDeVehiculo(vehiculoEntity,costo);
    }

    private long calcularTotal(VehiculoEntity vehiculoEntity){
        long tiempoMilisegundos = (new Date()).getTime() - vehiculoEntity.getFechaDeEntrada().getTime();
        long tiempoHoras = tiempoMilisegundos/3600000 == 0? 1 : tiempoMilisegundos/3600000;
        long dias = calcularDias(tiempoHoras);
        long horas = calcularHoras(tiempoHoras);
        if(vehiculoEntity.getTipoDeVehiculo()== TipoVehiculoEnum.CARRO){
            return 8000*dias + 1000*horas;
        }else{
            long costo = 4000*dias + 500*horas;
            if(vehiculoEntity.getCilindraje() > 500){
                return costo + 2000;
            }
            return costo;
        }
    }

    public long calcularDias(long horasDiferencia){
        long dias = 0L;
        if(horasDiferencia >= 9){
            dias++;
            if( horasDiferencia > 24 ){
                dias = dias + calcularDias(horasDiferencia-24);
            }
        }
        return dias;
    }

    public long calcularHoras(long horasDiferencia){
        long horas = 0L;
        if(horasDiferencia >= 9){
            if( horasDiferencia > 24 ){
                horas = calcularHoras(horasDiferencia-24);
            }
        }else{
            horas = horasDiferencia;
        }
        return horas;
    }
}
