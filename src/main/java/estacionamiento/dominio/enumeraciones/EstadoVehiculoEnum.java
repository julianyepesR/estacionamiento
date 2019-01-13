package estacionamiento.dominio.enumeraciones;

public enum EstadoVehiculoEnum {
	EN_DEUDA("En el parqueadero"), FUERA("Fuera del parqueadero");

    String estadoActual;

    private EstadoVehiculoEnum(String estadoActual){
        this.estadoActual = estadoActual;
    }

    public String getEstadoActual() {
        return estadoActual;
    }
}
