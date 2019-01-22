package estacionamiento.constantes;

import org.springframework.stereotype.Component;

@Component
public class Constantes {
    public final String A = "A";
    public final String CARRO = "Carro";
    public final String CREACION_EXITOSA = "El vehiculo con placas <b>{0}</b> ha ingresado al sistema exitosamente";
    public final String CREACION_FALLIDA = "El vehiculo no ha ingresado al sistema, revise el formulario por favor";
    public final String FECHA_INGRESO = "Fecha_Ingreso";
    public final String FORMATO_DATE = "dd-MM-yyyy";
    public final String ID = "id";
    public final String JSON = "json";
    public final String MOTO = "Moto";
    public final String PLACA = "Placa";
    public final String PLACA_REPETIDA = "El vehiculo con placas <b>{0}</b> ya se encuentra en el estacionamiento";
    public final String PARQUEADERO_LLENO = "Parqueradero lleno, no es posible ingresar otro vehiculo de tipo {0}";
    public final String SIN_AUTORIZACION = "El vehiculo con placas <b>{0}</b> no está autorizada para ingresar al estacionamiento";
    public final String TIPO = "Tipo";
    public final String TRM_URL = "https://www.superfinanciera.gov.co/SuperfinancieraWebServiceTRM/TCRMServicesWebService/TCRMServicesWebService?WSDL";

}
