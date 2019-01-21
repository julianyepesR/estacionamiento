package estacionamiento.modelo.servicios;

import co.com.sc.nexura.superfinanciera.action.generic.services.trm.action.TCRMServicesInterfaceProxy;
import co.com.sc.nexura.superfinanciera.action.generic.services.trm.action.TcrmResponse;

import estacionamiento.ObjetosJSON.Placa;
import estacionamiento.ObjetosJSON.Vehiculo;
import estacionamiento.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.enumeraciones.TipoVehiculoEnum;
import estacionamiento.modelo.entidad.VehiculoEntity;
import estacionamiento.modelo.interfaces.VehiculoInterface;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
public class VehiculoImplementacion implements VehiculoInterface {

    private static final String A = "A";
    private static final String CREACION_EXITOSA = "El vehiculo con placas <b>{0}</b> ha ingresado al sistema exitosamente";
    private static final String CREACION_FALLIDA = "El vehiculo no ha ingresado al sistema, revise el formulario por favor";
    private static final String PLACA_REPETIDA = "El vehiculo con placas <b>{0}</b> ya se encuentra en el estacionamiento";
    private static final String SIN_AUTORIZACION = "El vehiculo con placas <b>{0}</b> no está autorizada para ingresar al estacionamiento";
    private static final String PARQUEADERO_LLENO = "Parqueradero lleno, no es posible ingresar otro vehiculo de tipo {0}";
    private static final String CARRO = "Carro";
    private static final String MOTO = "Moto";

    @Autowired
    private PersistenciaImplementacion persistenciaImplementacion;

    public VehiculoImplementacion(PersistenciaImplementacion persistenciaImplementacion){
        this.persistenciaImplementacion = persistenciaImplementacion;
    }

    @Override
    public String ingresoDeVehiculo(String body) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            Vehiculo vehiculo = objectMapper.readValue(body,Vehiculo.class);

            if(validacionCapacidad(vehiculo.getTipoDeVehiculo())){
                return MessageFormat.format(PARQUEADERO_LLENO,vehiculo.getTipoDeVehiculo());
            }

            if(validacionDePrimeraLetra(vehiculo.getPlaca())){
                return MessageFormat.format(SIN_AUTORIZACION,vehiculo.getPlaca());
            }

            if(persistenciaImplementacion.obtenerVehiculoEntity(vehiculo.getPlaca()) != null){
                return MessageFormat.format(PLACA_REPETIDA,vehiculo.getPlaca());
            }
            return agregarVehiculo(vehiculo.getTipoDeVehiculo(), Integer.valueOf(vehiculo.getCilindraje()), vehiculo.getPlaca());
        }catch (Exception e){
            return CREACION_FALLIDA;
        }
    }

    @Override
    public String calcularCosto(String body) {
        long costo = 0;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Placa placa = objectMapper.readValue(body,Placa.class);

            VehiculoEntity vehiculoEntity = persistenciaImplementacion.obtenerVehiculoEntity(placa.getPlaca());
            if(vehiculoEntity!=null){
                costo = calcularTotal(vehiculoEntity);
                cambioDeEstadoVehiculo(vehiculoEntity,costo);
            }
            return String.valueOf(costo);
        }catch (Exception e){
            return String.valueOf(costo);
        }
    }

    @Override
    public String cargarPaginaInicial() {
        List<VehiculoEntity> listaDeVehiculos = persistenciaImplementacion.obtenerListaDeVehiculos();
        return crearJson(listaDeVehiculos);
    }

    @Override
    public String obtenerTRM() throws RemoteException {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        TCRMServicesInterfaceProxy proxy = new TCRMServicesInterfaceProxy("https://www.superfinanciera.gov.co/SuperfinancieraWebServiceTRM/TCRMServicesWebService/TCRMServicesWebService?WSDL");
        TcrmResponse tcrmResponse = proxy.queryTCRM(null);
        return tcrmResponse.getValue() + " " + tcrmResponse.getUnit();
    }

    private String crearJson(List<VehiculoEntity> listaDeVehiculos){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        HashMap<String, JSONObject> map = new HashMap<>();
        JSONArray arr = new JSONArray();
        int id = 1;
        for(VehiculoEntity vehiculo : listaDeVehiculos) {
            JSONObject jsonObj=new JSONObject();
            jsonObj.put("id", id);
            jsonObj.put("Placa", vehiculo.getPlaca());
            jsonObj.put("Tipo", vehiculo.getTipoDeVehiculo().getTipoDeVehiculo());
            jsonObj.put("Fecha_Ingreso",simpleDateFormat.format(vehiculo.getFechaDeEntrada()));
            map.put("json" + id, jsonObj);
            arr.put(map.get("json" + id));
            id++;
        }
        return arr.toString();
    }

    public Boolean validacionDePrimeraLetra(String placa){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaActual = calendar.get(Calendar.DAY_OF_WEEK);
        String primeraLetra = placa.substring(0,1);
        if(A.equalsIgnoreCase(primeraLetra)){
            return !(diaActual == 1 || diaActual == 2);
        }else{
            return false;
        }
    }

    public Boolean validacionCapacidad(String tipoDeVehiculo){
        if(CARRO.equals(tipoDeVehiculo)){
            return persistenciaImplementacion.obtenerListaDeCarros().size()==20;
        }else{
            return persistenciaImplementacion.obtenerListaDeMotos().size()==10;
        }
    }

    private String agregarVehiculo(String tipoVehiculo, int cilindraje, String placa){
        if(CARRO.equals(tipoVehiculo)){
            VehiculoEntity carro = new VehiculoEntity();
            carro.setCilindraje(cilindraje);
            carro.setPlaca(placa);
            carro.setTipoDeVehiculo(TipoVehiculoEnum.CARRO);
            carro.setFechaDeEntrada(new Date());
            carro.setEstadoActual(EstadoVehiculoEnum.EN_DEUDA);
            persistenciaImplementacion.agregarVehiculo(carro);
            return MessageFormat.format(CREACION_EXITOSA,placa);
        } else if(MOTO.equals(tipoVehiculo)){
            VehiculoEntity moto = new VehiculoEntity();
            moto.setCilindraje(cilindraje);
            moto.setPlaca(placa);
            moto.setTipoDeVehiculo(TipoVehiculoEnum.MOTO);
            moto.setFechaDeEntrada(new Date());
            moto.setEstadoActual(EstadoVehiculoEnum.EN_DEUDA);
            persistenciaImplementacion.agregarVehiculo(moto);
            return MessageFormat.format(CREACION_EXITOSA,placa);
        } else {
            return CREACION_FALLIDA;
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

    public long calcularDias(long diferencia){
        long dias = 0L;
        if(diferencia >= 9){
            dias++;
            if( diferencia >= 24 ){
                dias = dias + calcularDias(diferencia-24);
            }
        }
        return dias;
    }

    public long calcularHoras(long diferencia){
        long horas = 0L;
        if(diferencia >= 9){
            if( diferencia >= 24 ){
                horas = calcularHoras(diferencia-24);
            }
        }else{
            horas = diferencia;
        }
        return horas;
    }
}
