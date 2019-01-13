package estacionamiento.persistencia.entidad;

import estacionamiento.dominio.enumeraciones.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Vehiculo")
@NamedQueries({
        @NamedQuery(name = "Vehiculo.findAll", query = "SELECT vehiculo from VehiculoEntity vehiculo"),
        @NamedQuery(name = "Vehiculo.findByPlaca", query = "SELECT vehiculo from VehiculoEntity vehiculo where vehiculo.placa = :placa"),
        @NamedQuery(name = "Vehiculo.findallMotos", query = "SELECT vehiculo from VehiculoEntity vehiculo where vehiculo.tipoDeVehiculo = :tipoDeVehiculo"),
        @NamedQuery(name = "Vehiculo.findallCarros", query = "SELECT vehiculo from VehiculoEntity vehiculo where vehiculo.tipoDeVehiculo = :tipoDeVehiculo")
})
public class VehiculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombreCliente;

    private VehiculoEnum tipoDeVehiculo;

    private EstadoVehiculoEnum estadoActual;

    private int cilindraje;

    private Date fechaDeEntrada;

    private Date fechaDeSalida;

    private String placa;

    private double costo;

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

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public VehiculoEnum getTipoDeVehiculo() {
        return tipoDeVehiculo;
    }

    public void setTipoDeVehiculo(VehiculoEnum tipoDeVehiculo) {
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

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}
