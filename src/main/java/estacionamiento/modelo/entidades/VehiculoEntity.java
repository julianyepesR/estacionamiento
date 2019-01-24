package estacionamiento.modelo.entidades;

import estacionamiento.transversal.enumeraciones.EstadoVehiculoEnum;
import estacionamiento.transversal.enumeraciones.TipoVehiculoEnum;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Vehiculo")
public class VehiculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private TipoVehiculoEnum tipoDeVehiculo;

    private EstadoVehiculoEnum estadoActual;

    private int cilindraje;

    private Date fechaDeEntrada;

    private Date fechaDeSalida;

    private String placa;

    private long costo;

    public VehiculoEntity(){}

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoVehiculoEnum getTipoDeVehiculo() {
        return tipoDeVehiculo;
    }

    public void setTipoDeVehiculo(TipoVehiculoEnum tipoDeVehiculo) {
        this.tipoDeVehiculo = tipoDeVehiculo;
    }

    public EstadoVehiculoEnum getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoVehiculoEnum estadoActual) {
        this.estadoActual = estadoActual;
    }

    public int getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(int cilindraje) {
        this.cilindraje = cilindraje;
    }

    public Date getFechaDeEntrada() {
        return fechaDeEntrada;
    }

    public void setFechaDeEntrada(Date fechaDeEntrada) {
        this.fechaDeEntrada = fechaDeEntrada;
    }

    public Date getFechaDeSalida() {
        return fechaDeSalida;
    }

    public void setFechaDeSalida(Date fechaDeSalida) {
        this.fechaDeSalida = fechaDeSalida;
    }

    public long getCosto() {
        return costo;
    }

    public void setCosto(long costo) {
        this.costo = costo;
    }

    public VehiculoEntity(int cilingraje, long costo, String placa, Date fechaDeSalida, Date fechaDeEntrada, EstadoVehiculoEnum estadoActual, TipoVehiculoEnum tipoDeVehiculo){
        this.cilindraje = cilingraje;
        this.costo = costo;
        this.placa = placa;
        this.fechaDeSalida = fechaDeSalida;
        this.fechaDeEntrada = fechaDeEntrada;
        this.estadoActual = estadoActual;
        this.tipoDeVehiculo = tipoDeVehiculo;
    }
}
