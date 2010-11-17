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
        private short pertenece;

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

        public Icono(String nombre, double x, double y, String graf, short id, short tipoIcono, short pertenece) {
            //tipo icono :
            // 0 = habilidades (habilidad)
            // 1 = inventario (objeto)
            super(nombre, true, x, y, (int) Math.pow(2, 4), graf);
            this.setIdObjeto(id);
            this.setTipo(tipoIcono);
            xAnt = x;
            yAnt = y;
            this.pertenece = pertenece;
        }

        public boolean belongTo(short tipo) {
            if (this.pertenece == tipo) {
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

