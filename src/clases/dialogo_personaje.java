/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gerald
 */
public class dialogo_personaje {

    private HashMap<Short, UnDialogo> dialogos = new HashMap<Short, UnDialogo>();//Contiene los dialogos que dice el npc
    private dbDelegate conexion;
    private short id_texto_concurrente, primer_id;
    private ArrayList<Short> indices = new ArrayList<Short>();
    private boolean hayTexto = false;

    public dialogo_personaje() {
    }

    public short getId_texto_concurrente() {
        return id_texto_concurrente;
    }

    public void setId_texto_concurrente(short id_texto_concurrente) {
        this.id_texto_concurrente = id_texto_concurrente;
    }

    public void cargarDialogos(short id) {
        this.conexion = new dbDelegate();
        String StrSql = "SELECT txt.id, txt.texto,dial.texto_siguiente_id FROM dialogo_personaje dial, texto txt "
                + "WHERE dial.texto_id = txt.id "
                + "   AND dial.personaje_id =" + id;
        try {
            ResultSet res = conexion.Consulta(StrSql);
            byte i = 0;
            while (res.next()) {
                UnDialogo dialogo = new UnDialogo();
                dialogo.setIdTexto(res.getShort("txt.id"));
                dialogo.setIdTextoSiguiente(res.getShort("dial.texto_siguiente_id"));
                dialogo.setTexto(new Texto(res.getShort("txt.id"), res.getString("txt.texto")));
                dialogos.put(dialogo.getIdTexto(), dialogo);
                indices.add(i, dialogo.getIdTexto());
                i += 1;
                hayTexto = true;
            }
            //pongo el primer indice =)
            if (hayTexto) {
                id_texto_concurrente = indices.get(0);
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->dialogo_personaje , método->cargarDialogos() " + ex);
        }

    }

    public HashMap<Short, UnDialogo> getDialogos() {
        return dialogos;
    }

    public void setDialogos(HashMap<Short, UnDialogo> dialogos) {
        this.dialogos = dialogos;
    }

    public void nextTexto() {
        //si hay texto
        if (hayTexto) {
            //si el id de texto siguiente es null (o cero) lo envio al primero
            if (this.dialogos.get(id_texto_concurrente).getIdTextoSiguiente() != 0) {
                setId_texto_concurrente(this.dialogos.get(id_texto_concurrente).getIdTextoSiguiente());
            } else {
                setId_texto_concurrente(this.indices.get(0));// lo mandamos al primero
            }
        }
    }

    public String getParrafo() {
        String tex;
        if (hayTexto) {
            tex = this.dialogos.get(id_texto_concurrente).getTexto().getTexto();
            this.getDialogos().get(id_texto_concurrente).aumentarVeces_habladas();
        } else {
            tex = "Disculpa, estoy ocupado gastando aire";
        }
        return tex;
    }

    public class UnDialogo {

        private short idTexto;
        private Texto texto;
        private short idTextoSiguiente;
        private short veces_hablados;

        public UnDialogo(short idTexto, Texto texto) {
            this.idTexto = idTexto;
            this.texto = texto;
        }

        private UnDialogo() {
        }

        public short getVeces_hablados() {
            return veces_hablados;
        }

        public void setVeces_hablados(short veces_hablados) {
            this.veces_hablados = veces_hablados;
        }

        public short getIdTextoSiguiente() {
            return idTextoSiguiente;
        }

        public void setIdTextoSiguiente(short idTextoSiguiente) {
            this.idTextoSiguiente = idTextoSiguiente;
        }

        public short getIdTexto() {
            return idTexto;
        }

        public void setIdTexto(short idTexto) {
            this.idTexto = idTexto;
        }

        public Texto getTexto() {
            return texto;
        }

        public void setTexto(Texto texto) {
            this.texto = texto;
        }

        private void aumentarVeces_habladas() {
            veces_hablados +=  1;
        }
    }
}
