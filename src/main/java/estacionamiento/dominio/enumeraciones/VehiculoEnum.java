package estacionamiento.dominio.enumeraciones;

public enum VehiculoEnum {
	CARRO("Carro"),MOTO("Moto");

    private String tipoDeVehiculo;

    private VehiculoEnum(String tipoDeVehiculo){
        this.tipoDeVehiculo = tipoDeVehiculo;
    }

    public String getTipoDeVehiculo() {
        return tipoDeVehiculo;
    }
}
