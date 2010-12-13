/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerald
 */
public class Inventario {

    private short idPersonaje;
    private dbDelegate conexion;
    private HashMap<Short, Item> objetos;//contiene los objetos del personaje
    private HashMap<Short, Short> respaldoObjetos;
    private HashMap<Short, Equipo> equipo = new HashMap<Short, Equipo>();
    private Equipo parteEquipo = new Equipo();
    private short vitalidad;
    private short destreza;
    private short sabiduria;
    private short fuerza;

    public Inventario(dbDelegate conn) {
        this.objetos = new HashMap<Short, Item>();
        this.respaldoObjetos = new HashMap<Short, Short>();
        this.conexion = conn;
//      this.equipo = new HashMap<Short, Short>();
    }

//    /**
//     * Carga los objetos desde la base de datos asociados al personaje
//     * las deja en el arreglo "objetos"
//     */
//    public void cargarInventario(short id) {
////        this.conexion = new dbDelegate();
//        String StrSql = "SELECT   inv.Personaje_id idPersonaje, inv.Objeto_id idObjeto,"
//                + " inv.cantidad cantidad, inv.estaEquipado estaEquipado "
//                + "  FROM inventario inv"
//                + " WHERE inv.Personaje_id=" + id;
//        try {
//            ResultSet res = conexion.Consulta(StrSql);
//            byte i = 0;
//            while (res.next()) {
//                Item item = new Item();
//                item.setIdObjeto(res.getShort("idObjeto"));
//                item.setIdPersonaje(res.getShort("idPersonaje"));
//                item.setCantidad(res.getShort("cantidad"));
//                item.setEstaEquipado(res.getShort("estaequipado"));
//                item.setNewItem(false);
//                Objeto obj = new Objeto();
//                obj.setObjeto(item.getIdObjeto());
//                item.setObjeto(obj);
//                this.objetos.put(item.getIdObjeto(), item);
//                i += 1;
//            }
////            this.conexion.cierraDbCon();
//        } catch (Exception ex) {
//            System.out.println("Problemas en: clase->Inventario , método->cargarInventario() " + ex);
//        }
//    }
    /************************IMPORTANTE*****************************************/
    //    Para manipular correctamente la base de datos, se ha diseñado una serie de
    //    convenciones:
    //        *Al adherir un item se comprueba si ya está en el hashmap, si no esta
    //        se agrega al hashmap y se pone el valor newItem como true
    //        esto hace que, independiente de lo que sucede con sus datos en el
    //        transcurso  del juego , se guarde en la base de datos
    //
    //        *Para borrar un item , independiente si está o no en la base de datos
    //        se debe dejar la cantidad en 0, asi cada vez que se guarde, se eliminaran
    //        en la base de datos los elementos correspondientes, y se volverá a cargar
    //        el hashmap
    /***************************************************************************/
    /**
     * elimina, inserta y actualiza en la base de datos según los datos que tenga
     * en el hashmap
     */
    public void salvarInventario() {
        bdDeletes();
        bdUpdates();
        bdInserts();
    }

    /**
     * Busca los elementos que ya están en la base de datos (newItem == true)
     * y que no hayan sido eliminados de alguna manera (cantidad > 0 )
     * para hacer el respectivo update en la base de datos
     */
    private void bdUpdates() {
        try {
            this.conexion = new dbDelegate();
            Iterator it = this.getObjetos().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                Item item = this.getItem(Short.parseShort(e.getKey().toString()));
                if (!item.isNewItem() && item.getCantidad() > 0) {
                    String StrSql = "UPDATE inventario" + " SET cantidad = " + item.getCantidad() + "," + "     estaEquipado = " + item.getEstaEquipado() + " WHERE personaje_id = " + this.getIdPersonaje() + "   AND objeto_id = " + item.getIdObjeto();
                    conexion.Ejecutar(StrSql);
                }
            }
            this.conexion.cierraDbCon();
        } catch (Exception ex) {
            Logger.getLogger(Inventario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Si el item es nuevo y no ha sido eliminado (cantidad == 0)
     * insertar en la base de datos
     */
    private void bdInserts() {
        try {
            this.conexion = new dbDelegate();
            Iterator it = this.getObjetos().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                Item item = this.getItem(Short.parseShort(e.getKey().toString()));
                if (item.isNewItem() && item.getCantidad() > 0) {
                    String StrSql = "INSERT INTO inventario VALUES(" + this.getIdPersonaje() + "," + item.getIdObjeto() + "," + item.getCantidad() + "," + item.getEstaEquipado() + ")";
                    conexion.Ejecutar(StrSql);
                }
            }
            this.conexion.cierraDbCon();
        } catch (Exception ex) {
            Logger.getLogger(Inventario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * si el item no tiene cantidad , se borra de la base de datos
     */
    private void bdDeletes() {
        try {
            this.conexion = new dbDelegate();
            Iterator it = this.getObjetos().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                Item item = this.getItem(Short.parseShort(e.getKey().toString()));
                if (item.getCantidad() <= 0) {
                    String StrSql = "DELETE FROM inventario WHERE" + " personaje_id = " + this.getIdPersonaje() + " AND objeto_id = " + item.getIdObjeto();
                    conexion.Ejecutar(StrSql);
                }
            }
            this.conexion.cierraDbCon();
        } catch (Exception ex) {
            Logger.getLogger(Inventario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Item getItem(short idItem) {
        return this.getObjetos().get(idItem);
    }

    /**
     * Agrega un item al inventario, si el item está, se suma la cantidad
     * @param idItem
     * @param cantidad
     */
    public void agregarItem(short idItem, short cantidad, Objeto obj) {
        Item item = creaItem(idItem, cantidad, (short) 0, obj);
        if (this.comprobarItem(idItem)) {
            this.getObjetos().get(idItem).sumarCantidad(cantidad);
        } else {
            //si el item es nuevo en el hashmap, se debe ingresar en la base de datos
            //para eso se pone el valor newItem como true
            item.setNewItem(true);
            this.getObjetos().put(idItem, item);
        }
    }

    /**
     *  Agrega UN item al inventario
     * @param idItem
     */
    public void agregarItem(short idItem, Objeto obj) {
        Item item = creaItem(idItem, (short) 1, (short) 0, obj);
        if (this.comprobarItem(idItem)) {
            this.getObjetos().get(idItem).sumarCantidad((short) 1);
        } else {
            //si el item es nuevo en el hashmap, se debe ingresar en la base de datos
            //para eso  se pone el valor newItem como true
            item.setNewItem(true);
            parteEquipo = new Equipo();
            this.getObjetos().put(idItem, item);
                    parteEquipo.setEquipo(obj.getIdObjeto(), item.getEstaEquipado());
                    setFuerza(parteEquipo.getFuerza());
                    setSabiduria(parteEquipo.getSabiduria());
                    setVitalidad(parteEquipo.getVitalidad());
                    setDestreza(parteEquipo.getDestreza());

                    equipo.put(item.getIdObjeto(), parteEquipo);
        }
    }

    /**
     * Borra item según la cantidad indicada
     * @param idItem
     * @param cantidad
     */
    public void eliminarItem(short idItem, short cantidad) {
        if (tieneItem(idItem, cantidad)) {
            this.getObjetos().get(idItem).restarCantidad(cantidad);
        }
    }

    /**
     * Borra item sin importar la cantidad
     * @param idItem
     */
    public void eliminarItem(short idItem) {
        if (comprobarItem(idItem)) {
            this.getObjetos().get(idItem).setCantidad((short) 0);
        }
    }

    /**
     * Verifica si tiene el objeto en cuestión en el hashmap, lógicamente
     * para tener un item se debe tener una cantidad mínima de 1
     * Este método es para fines de implementación y solo contempla
     * si existe en el hashmap (pudiendo tener cantidad o)
     * @param idObjeto
     *
     * @return
     */
    private boolean comprobarItem(short idObjeto) {
        return this.getObjetos().containsKey(idObjeto);
    }

    /**
     * Verifica si tiene almenos un item en cuestion, este método es para
     * saber si lógicamente tiene el item (almenos una unidad)
     * @param idItem
     * @return
     */
    public boolean tieneItem(short idItem) {
        return tieneItem(idItem, (short) 1);
    }

    /**
     * Verifica si tiene la cantidad requerida del objeto en cuestión
     * @param idObjeto
     * @param cantidad
     * @return
     */
    public boolean tieneItem(short idObjeto, short cantidad) {
        return this.getObjetos().containsKey(idObjeto)
                && this.getObjetos().get(idObjeto).getCantidad() >= cantidad;

    }

    /**
     * retorna el el peso total usado por el inventario(suma de los objetos)
     * @return
     */
    public int getPesoUsado() {
        int pesoTotal = 0;
        Iterator it = this.getObjetos().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            pesoTotal += getItem(Short.parseShort(e.getKey().toString())).getObjeto().getPeso() * getItem(Short.parseShort(e.getKey().toString())).getCantidad();
        }
        return pesoTotal;
    }

    /**
     * Retorna el valor en dinero que suman los elementos
     * @return
     */
    public int getDineroTotal() {
        //objeto la lista de objetos
        int dineroTotal = 0;
        Iterator it = this.getObjetos().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            dineroTotal += getItem(Short.parseShort(e.getKey().toString())).getObjeto().getValorDinero() * getItem(Short.parseShort(e.getKey().toString())).getCantidad();
        }
        return dineroTotal;
    }

    /**
     * Crea un item privado para centralizar la creacion y favorecer la modularidad
     * @param idObjeto
     * @param cantidad
     * @param estaequipado
     * @return
     */
    private Item creaItem(short idObjeto, short cantidad, short estaequipado, Objeto obj) {
        return new Item(this.getIdPersonaje(), idObjeto, cantidad, estaequipado, obj);
    }

    /*
     * Devuelve la cantidad de objetos de un determinado tipo
     */
    public int contarItem(short idItem) {
        int cuenta = 0;
        if (tieneItem(idItem)) {
            cuenta = this.getObjetos().get(idItem).getCantidad();
        }
        return cuenta;
    }

    /**
     * Devuele la cantidad en total de todos los objetos
     * @return
     */
    public int contarTodosItems() {
        //objeto la lista de objetos
        int cuenta = 0;
        Iterator it = this.getObjetos().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            cuenta += this.getObjetos().get(Short.parseShort(e.getKey().toString())).getCantidad();
        }
        return cuenta;
    }

    public short getDestreza() {
        return destreza;
    }

    public void setDestreza(short destreza) {
        this.destreza += destreza;
    }

    public short getFuerza() {
        return fuerza;
    }

    public void setFuerza(short fuerza) {
        this.fuerza += fuerza;
    }

    public short getSabiduria() {
        return sabiduria;
    }

    public void setSabiduria(short sabiduria) {
        this.sabiduria += sabiduria;
    }

    public short getVitalidad() {
        return vitalidad;
    }

    public void setVitalidad(short vitalidad) {
        this.vitalidad += vitalidad;
    }

    public void cargarInventario(short id, HashMap<Short, Objeto> objetos) {
//        this.conexion = new dbDelegate();
        String StrSql = "SELECT   inv.Personaje_id idPersonaje, inv.Objeto_id idObjeto,"
                + " inv.cantidad cantidad, inv.estaEquipado estaEquipado "
                + "  FROM inventario inv"
                + " WHERE inv.Personaje_id=" + id;
        try {
            ResultSet res = conexion.Consulta(StrSql);
            byte i = 0;
            while (res.next()) {
                parteEquipo = new Equipo();

                Item item = new Item();
                item.setIdObjeto(res.getShort("idObjeto"));
                item.setIdPersonaje(res.getShort("idPersonaje"));
                item.setCantidad(res.getShort("cantidad"));
                item.setEstaEquipado(res.getShort("estaequipado"));

                item.setNewItem(false);
                Objeto obj = objetos.get(item.getIdObjeto());
                
                if (obj.getTipo() == 1) {
                    parteEquipo.setEquipo(obj.getIdObjeto(), item.getEstaEquipado());
                    setFuerza(parteEquipo.getFuerza());
                    setSabiduria(parteEquipo.getSabiduria());
                    setVitalidad(parteEquipo.getVitalidad());
                    setDestreza(parteEquipo.getDestreza());
                    
                    equipo.put(item.getIdObjeto(), parteEquipo);
                }

                item.setObjeto(obj);
                this.objetos.put(item.getIdObjeto(), item);
                i += 1;


            }
//            this.conexion.cierraDbCon();
        } catch (Exception ex) {
            System.out.println("Problemas en: clase->Inventario , método->cargarInventario() " + ex);
        }
    }

    public Equipo getParteEquipo() {
        return parteEquipo;
    }

    public void setParteEquipo(Equipo parteEquipo) {
        this.parteEquipo = parteEquipo;
    }

    public HashMap<Short, Equipo> getEquipo() {
        return equipo;
    }

    public void setEquipo(HashMap<Short, Equipo> equipo) {
        this.equipo = equipo;
    }

    public short getIdPersonaje() {
        return idPersonaje;
    }

    public void setIdPersonaje(short idPersonaje) {
        this.idPersonaje = idPersonaje;
    }

    public HashMap<Short, Item> getObjetos() {
        return objetos;
    }

//    private void repaldarInventario(HashMap<Short, Item> objetos) {
//        this.respaldoObjetos = (HashMap<Short, Item>) this.objetos.clone();
//
//    }
    public void restablecerInventario() {

        for (Map.Entry id : this.respaldoObjetos.entrySet()) {
            System.out.println("'" + this.objetos.get(Short.valueOf(id.getKey().toString())).cantidad);
            this.objetos.get(Short.valueOf(id.getKey().toString())).sumarCantidad(Short.valueOf(id.getValue().toString()));
            System.out.println("*" + this.objetos.get(Short.valueOf(id.getKey().toString())).cantidad);
        }

        this.respaldoObjetos = new HashMap<Short, Short>();
    }

//    public void respaldarInventario() {
//        this.respaldoObjetos = this.objetos;
//
//    }
    public void mod_despojar(short id) {
        if (this.respaldoObjetos.containsKey(id)) {
            this.respaldoObjetos.put(id, (short) (this.respaldoObjetos.get(id) + 1));
        } else {
            this.respaldoObjetos.put(id, (short) 1);
        }
        eliminarItem(id, (short) (1));
    }

    public void equipar(short id) {
        Equipo unEquipo = new Equipo();
        unEquipo = this.equipo.get(id);
        Iterator it = equipo.entrySet().iterator();

        boolean hayEquipo = false;
        boolean mismo = false;

        /*
         * Recorre el quipo mientras no encuentre algo equipado en la posicion del
         * item que se desea equipar, al revisar todo finaliza.
         */
        while (it.hasNext() && !hayEquipo) {
            Map.Entry e = (Map.Entry) it.next();
            System.out.println("quipar-----" + id);
            System.out.println("quipado-----" + Short.parseShort(e.getKey().toString()));

            if (id != Short.parseShort(e.getKey().toString()) && equipo.get(Short.parseShort(e.getKey().toString())).getEquipaEn() == unEquipo.getEquipaEn() && equipo.get(Short.parseShort(e.getKey().toString())).getEquipado() == 1) {
                hayEquipo = true;
                equipo.get(Short.parseShort(e.getKey().toString())).setEquipado((short) 0);
                this.setDestreza((short) -equipo.get(Short.parseShort(e.getKey().toString())).getDestreza());
                this.setFuerza((short) -equipo.get(Short.parseShort(e.getKey().toString())).getFuerza());
                this.setSabiduria((short) -equipo.get(Short.parseShort(e.getKey().toString())).getSabiduria());
                this.setVitalidad((short) -equipo.get(Short.parseShort(e.getKey().toString())).getVitalidad());

                this.agregarItem(Short.parseShort(e.getKey().toString()), (short) 1, objetos.get(Short.parseShort(e.getKey().toString())).getObjeto());


            }
            if (id == Short.parseShort(e.getKey().toString()) && equipo.get(Short.parseShort(e.getKey().toString())).getEquipaEn() == unEquipo.getEquipaEn() && equipo.get(Short.parseShort(e.getKey().toString())).getEquipado() == 1) {
                mismo = true;
            }
        }

        if (!mismo) {
            System.out.println("IDEEEEEEEEEEEEEEEEEEEEEEEEEEE"+equipo.containsKey(id));
            equipo.get(id).setEquipado((short) 1);
            this.setDestreza((short) equipo.get(id).getDestreza());
            this.setFuerza((short) equipo.get(id).getFuerza());
            this.setSabiduria((short) equipo.get(id).getSabiduria());
            this.setVitalidad((short) equipo.get(id).getVitalidad());
            this.eliminarItem(id, (short) 1);
        }


    }

    public void desequipar(short id) {
        if (equipo.get(id).getEquipado() == 1) {
            equipo.get(id).setEquipado((short) 0);

            this.setDestreza((short) -equipo.get(id).getDestreza());
            this.setFuerza((short) -equipo.get(id).getFuerza());
            this.setSabiduria((short) -equipo.get(id).getSabiduria());
            this.setVitalidad((short) -equipo.get(id).getVitalidad());

            this.agregarItem(id, (short) 1, objetos.get(id).getObjeto());
        }
    }

    /*
     * Al iniciar la aplicacion resta los item que estan equipados del inventario
     */
    public void cargarEquipo() {
        Iterator it = equipo.entrySet().iterator();

        boolean hayEquipo = false;
        while (it.hasNext() && !hayEquipo) {
            Map.Entry e = (Map.Entry) it.next();

            if (equipo.get(Short.parseShort(e.getKey().toString())).getEquipado() == 1) {
                this.eliminarItem(Short.parseShort(e.getKey().toString()), (short) 1);
            }


        }


    }

    public HashMap<Short, String> itemEquipados(HashMap<Short, Equipo> equipo, Personaje pj) {
        HashMap<Short, String> hmGraficos = new HashMap<Short, String>();
        Iterator it = equipo.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            if (equipo.get(Short.parseShort(e.getKey().toString())).getEquipado() == 1) {
                hmGraficos.put(equipo.get(Short.parseShort(e.getKey().toString())).getEquipaEn(), pj.getInventario().getObjetos().get(Short.parseShort(e.getKey().toString())).getObjeto().getNombreGrafico());
            }


        }
        return hmGraficos;
    }

    public void pintaEquipo() {
//            HashMap<Short, String> hmGraficos = new HashMap<Short, String>();
        Iterator it = this.equipo.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            if (equipo.get(Short.parseShort(e.getKey().toString())).getEquipado() == 1) {
//                    hmGraficos.put(equipo.get(Short.parseShort(e.getKey().toString())).getEquipaEn(), pj.getInventario().getObjetos().get(Short.parseShort(e.getKey().toString())).getObjeto().getNombreGrafico());
            }
            System.out.println("Equipa en: " + equipo.get(Short.parseShort(e.getKey().toString())).getEquipaEn() + " Grafico: " + this.getItem(Short.parseShort(e.getKey().toString())).getObjeto().getNombreGrafico());

        }
    }

    public class Item {

        private short idPersonaje;
        private short idObjeto;
        private short cantidad;
        private short estaEquipado;
        private boolean newItem;
        private boolean eliminarItem;
        private Objeto objeto;

        public Item() {
        }

        public Item(short idPersonaje, short idObjeto, short cantidad, short estaEquipado, Objeto obj) {
            this.idPersonaje = idPersonaje;
            this.idObjeto = idObjeto;
            this.cantidad = cantidad;
            this.estaEquipado = estaEquipado;
            this.objeto = obj;

        }

        public Objeto getObjeto() {
            return objeto;
        }

        public void setObjeto(Objeto objeto) {
            this.objeto = objeto;
        }

        public boolean isEliminarItem() {
            return eliminarItem;
        }

        public void setEliminarItem(boolean eliminarItem) {
            this.eliminarItem = eliminarItem;
        }

        public boolean isNewItem() {
            return newItem;
        }

        public void setNewItem(boolean newItem) {
            this.newItem = newItem;
        }

        public short getCantidad() {
            return cantidad;
        }

        public void setCantidad(short cantidad) {
            this.cantidad = cantidad;
        }

        public short getEstaEquipado() {
            return estaEquipado;
        }

        public void setEstaEquipado(short estaEquipado) {
            this.estaEquipado = estaEquipado;
        }

        public short getIdObjeto() {
            return idObjeto;
        }

        public void setIdObjeto(short idObjeto) {
            this.idObjeto = idObjeto;
        }

        public short getIdPersonaje() {
            return idPersonaje;
        }

        public void setIdPersonaje(short idPersonaje) {
            this.idPersonaje = idPersonaje;
        }

        public void sumarCantidad(short cantidad) {
            this.setCantidad((short) (this.getCantidad() + cantidad));
        }

        /**
         * Borra la cantidad indicada, si es menor que cero lo deja en cero
         *
         * @param cantidad
         */
        public void restarCantidad(short cantidad) {
            short valor = (short) (this.getCantidad() - cantidad);
            if (this.getCantidad() - cantidad < 0) {
                valor = 0;
            }
            this.setCantidad(valor);
        }
    }

    public class Equipo {

        private short vitalidad;
        private short destreza;
        private short sabiduria;
        private short fuerza;
        private short equipaEn;
        private short equipado;
//        private dbDelegate conexion;
        private Item item = new Item();

        /**
         * Contructor vacio
         */
        public Equipo() {
        }

        public void setEquipo(short id, short estaEquipado) {
//            this.conexion = new dbDelegate();
            System.out.println("new db delegate");
            String StrSql = "SELECT *"
                    + "  FROM equipo"
                    + " WHERE id_objeto=" + id;
            System.out.println("id ql-------------------------------->" + id);
            try {
                ResultSet res = conexion.Consulta(StrSql);
                if (res.next()) {
                    this.setEquipado(estaEquipado);

                    this.setVitalidad(res.getShort("vitalidad"));
                    this.setDestreza(res.getShort("destreza"));
                    this.setSabiduria(res.getShort("sabiduria"));
                    this.setFuerza(res.getShort("fuerza"));
                    this.setEquipaEn(res.getShort("equipa_en"));
                    System.out.println("equipa_en " + this.getEquipaEn());
                }
//                this.conexion.cierraDbCon();
            } catch (Exception ex) {
                System.out.println("Problemas en: clase->Inventario.Equipo , método->cargarEquipo() " + ex);
            }
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public short getEquipado() {
            return equipado;
        }

        public void setEquipado(short equipado) {
            this.equipado = equipado;
        }

        public short getDestreza() {
            return destreza;
        }

        public void setDestreza(short destreza) {
            this.destreza = destreza;
        }

        public short getEquipaEn() {
            return equipaEn;
        }

        public void setEquipaEn(short equipaEn) {
            this.equipaEn = equipaEn;
        }

        public short getFuerza() {
            return fuerza;
        }

        public void setFuerza(short fuerza) {
            this.fuerza = fuerza;
        }

        public short getSabiduria() {
            return sabiduria;
        }

        public void setSabiduria(short sabiduria) {
            this.sabiduria = sabiduria;
        }

        public short getVitalidad() {
            return vitalidad;
        }

        public void setVitalidad(short vitalidad) {
            this.vitalidad = vitalidad;
        }
    }
}
