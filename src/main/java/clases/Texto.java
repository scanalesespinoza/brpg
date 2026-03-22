/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

/**
 *
 * @author gerald
 */
public class Texto {
    private short idTexto;
    private String texto;

    public short getIdTexto() {
        return idTexto;
    }

    public void setIdTexto(short idTexto) {
        this.idTexto = idTexto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Texto() {
    }

    public Texto(short idTexto, String texto) {
        this.idTexto = idTexto;
        this.texto = texto;
    }
    
}
