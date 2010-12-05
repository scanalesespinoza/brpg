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
public class dialogo_mision {

    private HashMap<Short, UnDialogo_mision> dialogos = new HashMap<Short, UnDialogo_mision>();//Contiene los dialogos que dice el npc
    private dbDelegate conexion;
    private short id_texto_concurrente = 0, anterior_id = 0;
    public ArrayList<Short> indices = new ArrayList<Short>();

    ;
    private boolean hayTexto = false;
    private boolean terminoDialogo = false;

    public dialogo_mision() {
    }

    public short getId_texto_concurrente() {
        return id_texto_concurrente;
    }

    public void setId_texto_concurrente(short id_texto_concurrente) {
        this.id_texto_concurrente = id_texto_concurrente;
    }

    public void cargarDialogos(short id) {
        this.conexion = new dbDelegate();
        String StrSql = "SELECT txt.id, txt.texto,dial.texto_siguiente_id,dial.texto_anterior_id FROM dialogo_mision dial, texto txt "
                + "WHERE dial.texto_id = txt.id "
                + "   AND dial.mision_id =" + id;
        byte i = 0;
        try {
            ResultSet res = conexion.Consulta(StrSql);
            while (res.next()) {
                UnDialogo_mision dialogo = new UnDialogo_mision();
                dialogo.setIdTexto(res.getShort("txt.id"));
                dialogo.setIdTextoSiguiente(res.getShort("dial.texto_siguiente_id"));
                dialogo.setIdTextoAnterior(res.getShort("dial.texto_anterior_id"));
                dialogo.setTexto(new Texto(res.getShort("txt.id"), res.getString("txt.texto")));
                this.dialogos.put(dialogo.getIdTexto(), dialogo);
                this.indices.add(i, dialogo.getIdTexto());
                i++;
                hayTexto = true;
            }
            //pongo el primer indice =)
            if (hayTexto) {
                this.id_texto_concurrente = indices.get(0);
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->dialogo_mision , método->cargarDialogos() " + ex);
        }
    }

    public HashMap<Short, UnDialogo_mision> getDialogos() {
        return dialogos;
    }

    public void setDialogos(HashMap<Short, UnDialogo_mision> dialogos) {
        this.dialogos = dialogos;
    }

    public void nextTexto() {
        if (hayTexto) {
            //si el id de texto siguiente es null (o cero) termino la misión
            if (this.dialogos.get(id_texto_concurrente).getIdTextoSiguiente() != 0) {
                anterior_id = id_texto_concurrente;
                setId_texto_concurrente(this.dialogos.get(id_texto_concurrente).getIdTextoSiguiente());
            } else {
                this.terminoDialogo = true;
            }
        }
    }

    public String dialogoIniciarMision() {
        id_texto_concurrente = indices.get(0);
        return getParrafo();
    }

    public String dialogoComprobarMision() {
        id_texto_concurrente = indices.get(1);
        return getParrafo();
    }

    public String dialogoFalloMision() {
        id_texto_concurrente = indices.get(2);
        return getParrafo();
    }

    public String dialogoCumplirMision() {
        id_texto_concurrente = indices.get(3);
        return getParrafo();
    }

    public String getParrafo() {
        String tex;
        tex = this.dialogos.get((short) id_texto_concurrente).getTexto().getTexto();
        this.getDialogos().get((short) id_texto_concurrente).aumentarVeces_habladas();
        return tex;
    }

    public class UnDialogo_mision {

        private short idTexto;
        private Texto texto;
        private short idTextoSiguiente;
        private short idTextoAnterior;
        private short veces_hablados = 0;

        public short getIdTextoAnterior() {
            return idTextoAnterior;
        }

        public void setIdTextoAnterior(short idTextoAnterior) {
            this.idTextoAnterior = idTextoAnterior;
        }

        public UnDialogo_mision(short idTexto, Texto texto) {
            this.idTexto = idTexto;
            this.texto = texto;
        }

        private UnDialogo_mision() {
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
            veces_hablados += 1;
        }
    }
}
