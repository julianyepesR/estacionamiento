package estacionamiento.persistencia.entidad;

import estacionamiento.dominio.enumeraciones.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Vehiculo")
@NamedQueries({
        @NamedQuery(name = "Vehiculo.findAll", query = "SELECT vehiculo FROM VehiculoEntity vehiculo WHERE vehiculo.estadoActual = :estadoActual"),
        @NamedQuery(name = "Vehiculo.findByPlaca", query = "SELECT vehiculo FROM VehiculoEntity vehiculo WHERE vehiculo.placa = :placa AND vehiculo.estadoActual = :estadoActual"),
        @NamedQuery(name = "Vehiculo.findallMotos", query = "SELECT vehiculo FROM VehiculoEntity vehiculo WHERE vehiculo.tipoDeVehiculo = :tipoDeVehiculo AND vehiculo.estadoActual = :estadoActual"),
        @NamedQuery(name = "Vehiculo.findallCarros", query = "SELECT vehiculo FROM VehiculoEntity vehiculo WHERE vehiculo.tipoDeVehiculo = :tipoDeVehiculo AND vehiculo.estadoActual = :estadoActual")
})
public class VehiculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private VehiculoEnum tipoDeVehiculo;

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

    public long getCosto() {
        return costo;
    }

    public void setCosto(long costo) {
        this.costo = costo;
    }
}
