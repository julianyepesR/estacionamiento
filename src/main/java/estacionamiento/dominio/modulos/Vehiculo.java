package estacionamiento.dominio.modulos;

import estacionamiento.dominio.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.dominio.enumeraciones.VehiculoEnum;

import java.util.Date;

public class Vehiculo {

	 private int cilindraje;
	    private double costo;
	    private String placa;
	    private Date fechaDeSalida;
	    private Date fechaDeEntrada;
	    private EstadoVehiculoEnum estadoActual;
	    private VehiculoEnum tipoDeVehiculo;

	    public  Vehiculo(){};

	    public Vehiculo(int cilindraje, double costo, String placa, Date fechaDeSalida, Date fechaDeEntrada, EstadoVehiculoEnum estadoActual, VehiculoEnum tipoDeVehiculo) {
	        this.cilindraje = cilindraje;
	        this.costo = costo;
	        this.placa = placa;
	        this.fechaDeSalida = fechaDeSalida;
	        this.fechaDeEntrada = fechaDeEntrada;
	        this.estadoActual = estadoActual;
	        this.tipoDeVehiculo = tipoDeVehiculo;
	    }

		public int getCilindraje() {
			return cilindraje;
		}

		public void setCilindraje(int cilindraje) {
			this.cilindraje = cilindraje;
		}

		public double getCosto() {
			return costo;
		}

		public void setCosto(double costo) {
			this.costo = costo;
		}

		public String getPlaca() {
			return placa;
		}

		public void setPlaca(String placa) {
			this.placa = placa;
		}

		public Date getFechaDeSalida() {
			return fechaDeSalida;
		}

		public void setFechaDeSalida(Date fechaDeSalida) {
			this.fechaDeSalida = fechaDeSalida;
		}

		public Date getFechaDeEntrada() {
			return fechaDeEntrada;
		}

		public void setFechaDeEntrada(Date fechaDeEntrada) {
			this.fechaDeEntrada = fechaDeEntrada;
		}

		public EstadoVehiculoEnum getEstadoActual() {
			return estadoActual;
		}

		public void setEstadoActual(EstadoVehiculoEnum estadoActual) {
			this.estadoActual = estadoActual;
		}

		public VehiculoEnum getTipoDeVehiculo() {
			return tipoDeVehiculo;
		}

		public void setTipoDeVehiculo(VehiculoEnum tipoDeVehiculo) {
			this.tipoDeVehiculo = tipoDeVehiculo;
		}
	    
	    
	
}
