package estacionamiento.dominio.controladores;

import estacionamiento.dominio.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.dominio.enumeraciones.VehiculoEnum;
import estacionamiento.dominio.modulos.Vehiculo;
import estacionamiento.persistencia.entidad.VehiculoEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import estacionamiento.persistencia.repositorio.VehiculoPersistence;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class VehiculoController {

	private static final String A = "A";
	private static final String CREACION_EXITOSA = "El vehiculo con placas <b>{0}</b> ha ingresado al sistema exitosamente";
	private static final String CREACION_FALLIDA = "El vehiculo con placas <b>{0}</b> no ha ingresado al sistema, revise el formulario por favor";
	private static final String PLACA_REPETIDA = "El vehiculo con placas <b>{0}</b> ya se encuentra en el estacionamiento";
	private static final String SIN_AUTORIZACION = "El vehiculo con placas <b>{0}</b> no está autorizada para ingresar al estacionamiento";
	private static final String PARQUEADERO_LLENO = "Parqueradero lleno, no es posible ingresar otro vehiculo de tipo {0}";
	private static final String CARRO = "Carro";
	private static final String MOTO = "Moto";
	private static final String VEHICULO = "Vehiculo";
	private static final String PLACA = "placa";
	private static final String TIPO_DE_VEHICULO = "tipoDeVehiculo";
	private static final String CILINDRAJE = "cilindraje";

	@Autowired
	private VehiculoPersistence vehiculoPersistence;

	public VehiculoController(VehiculoPersistence vehiculoPersistence) {
		this.vehiculoPersistence = vehiculoPersistence;
	}

	@PostMapping("/ingresoDeVehiculo")
	public String ingresoDeVehiculo(@RequestBody String body){
		JSONObject bodyPost = new JSONObject(body);
		String tipoVehiculo =  bodyPost.get(TIPO_DE_VEHICULO).toString();
		String placa =  bodyPost.get(PLACA).toString();
		int cilindraje = Integer.valueOf(bodyPost.get(CILINDRAJE).toString());

		if(validacionCapacidad(tipoVehiculo)){
			return MessageFormat.format(PARQUEADERO_LLENO,tipoVehiculo);
		}

		if(validacionDePrimeraLetra(placa)){
			return MessageFormat.format(SIN_AUTORIZACION,placa);
		}

		if(vehiculoPersistence.obtenerVehiculo(placa) != null){
			return MessageFormat.format(PLACA_REPETIDA,placa);
		}
		return agregarVehiculo(tipoVehiculo, cilindraje, placa);
	}

	@PostMapping("/calcularCosto")
	public String calcularCosto(@RequestBody String body) {
		JSONObject bodyPost = new JSONObject(body);
		Vehiculo vehiculo = vehiculoPersistence.obtenerVehiculo(bodyPost.get(PLACA).toString());
		if(vehiculo==null){
			return "0";
		}
		long costo = calcularTotal(vehiculo);
        cambioDeEstadoVehiculo(vehiculo,costo);
		return String.valueOf(costo);
	}

	@GetMapping("/")
    public String cargarPaginaInicial(){
        List<Vehiculo> listaDeVehiculos = vehiculoPersistence.obtenerListaDeVehiculos();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(VEHICULO, crearJson(listaDeVehiculos));
		return crearJson(listaDeVehiculos);
    }

    private String crearJson(List<Vehiculo> listaDeVehiculos){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
		JSONArray arr = new JSONArray();
		int id = 1;
		for(Vehiculo vehiculo : listaDeVehiculos) {
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
            return vehiculoPersistence.obtenerListaDeCarros().size()==20;
        }else{
            return vehiculoPersistence.obtenerListaDeMotos().size()==10;
        }
    }

	public String agregarVehiculo(String tipoVehiculo, int cilindraje, String placa){
		if(CARRO.equals(tipoVehiculo)){
			Vehiculo carro = new Vehiculo();
			carro.setCilindraje(cilindraje);
			carro.setPlaca(placa);
			carro.setTipoDeVehiculo(VehiculoEnum.CARRO);
			carro.setFechaDeEntrada(new Date());
			carro.setEstadoActual(EstadoVehiculoEnum.EN_DEUDA);
			vehiculoPersistence.agregarVehiculo(carro);
			return MessageFormat.format(CREACION_EXITOSA,placa);
		} else if(MOTO.equals(tipoVehiculo)){
			Vehiculo moto = new Vehiculo();
			moto.setCilindraje(cilindraje);
			moto.setPlaca(placa);
			moto.setTipoDeVehiculo(VehiculoEnum.MOTO);
			moto.setFechaDeEntrada(new Date());
			moto.setEstadoActual(EstadoVehiculoEnum.EN_DEUDA);
			vehiculoPersistence.agregarVehiculo(moto);
			return MessageFormat.format(CREACION_EXITOSA,placa);
		} else {
			return MessageFormat.format(CREACION_FALLIDA,placa);
		}
	}

    private void cambioDeEstadoVehiculo(Vehiculo vehiculo, long costo){
	    VehiculoEntity vehiculoEntity = vehiculoPersistence.obtenerVehiculoEntity(vehiculo.getPlaca());
	    vehiculoPersistence.cambiarEstadoDeVehiculo(vehiculoEntity,costo);
    }

	private long calcularTotal(Vehiculo vehiculo){
		long tiempoMilisegundos = (new Date()).getTime() - vehiculo.getFechaDeEntrada().getTime();
		long tiempoHoras = tiempoMilisegundos/3600000 == 0? 1 : tiempoMilisegundos/3600000;
		long dias = calcularDias(tiempoHoras);
		long horas = calcularHoras(tiempoHoras);
		if(vehiculo.getTipoDeVehiculo()==VehiculoEnum.CARRO){
			return 8000*dias + 1000*horas;
		}else{
			long costo = 4000*dias + 500*horas;
			if(vehiculo.getCilindraje() > 500){
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
