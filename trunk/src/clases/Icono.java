/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

import jgame.JGObject;

/**
 *
 * @author gerald
 */
    public class Icono extends JGObject {

        private short idObjeto;
        private short tipo;
        private double xAnt;
        private double yAnt;
        private String pertenece;

        public short getIdObjeto() {
            return idObjeto;
        }

        public void setIdObjeto(short idObjeto) {
            this.idObjeto = idObjeto;
        }

        public short getTipo() {
            return tipo;
        }

        public void setTipo(short tipo) {
            this.tipo = tipo;
        }

        public Icono(String nombre, double x, double y, String graf, short id, short tipoIcono) {
            super(nombre, true, x, y, (int) Math.pow(2, 4), graf);
            this.setIdObjeto(id);
            this.setTipo(tipoIcono);
            xAnt = x;
            yAnt = y;
            this.pertenece = pertenece;
        }

        public boolean belongTo(String string) {
            if (this.pertenece.equals(string)) {
                return true;
            }
            return false;
        }

        @Override
        public void paint() {
            x = xAnt + eng.viewXOfs();
            y = yAnt + eng.viewYOfs();
        }
    }

