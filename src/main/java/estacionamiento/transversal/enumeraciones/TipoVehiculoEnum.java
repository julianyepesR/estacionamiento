package estacionamiento.transversal.enumeraciones;

public enum TipoVehiculoEnum {
	CARRO("Carro"),MOTO("Moto");

    private String tipoDeVehiculo;

    TipoVehiculoEnum(String tipoDeVehiculo){
        this.tipoDeVehiculo = tipoDeVehiculo;
    }

    public String getTipoDeVehiculo() {
        return tipoDeVehiculo;
    }
}
