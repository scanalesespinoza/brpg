/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

import java.sql.Date;

/**
 *
 * @author gerald
 */
public class Cuenta {
private Short idCuenta;

    private String nombre;

    private String nombreUsuario;

    private String password;

    private Date fechaRegistro;

    private boolean estaBaneado;

    private String correoElectronico;

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public boolean isEstaBaneado() {
        return estaBaneado;
    }

    public void setEstaBaneado(boolean estaBaneado) {
        this.estaBaneado = estaBaneado;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Short getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Short idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
