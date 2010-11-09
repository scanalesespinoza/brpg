/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

import extensiones.StdDungeonMonster;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import jgame.*;

/**
 *
 * @author Iarwain
 */

public class Npc extends StdDungeonMonster  {
    private short idNpc;
    private String grafNpc;
    private String nomNpc;
    private int colId;
    public String[] dialogo;
    private dbDelegate conect = new dbDelegate();
    public Calendar c1;
    public boolean cargado = false;
    private Inventario[] inv;


    public Npc(double x,double y,String name,String mediaName,int colId,int tamano,short idNpc, String[] dialogo) throws SQLException {
            super(name,true,x,y,colId,mediaName,tamano);
            this.dialogo=dialogo;
            this.idNpc=idNpc;
            this.grafNpc=mediaName;
            this.nomNpc=name;
            this.colId=colId;
            System.out.println("contruye NPC");
            //cargaInventario(idNpc);
            System.out.println("Inventario Npc Cargado");

         
    }



    public String[] obtieneDialogo(){
        /*
        String[] dialogo = {"Hola amigo",
                            "como estas",
                            "Me doy cuenta que no eres de estos lados",
                            "te advierto que es muy peligroso salir de la cuidad",
                            "hay muchas criaturas peligrosas",
                            "si no tienes donde ir puedes quedarte",
                            "mientras ayudes a mantener la cuidad en pie",
                            "todos te aceptaran sin problema",
                            "pensadolo hay mucho trabajo que hacer",
                            "acomapañame a la plaza y danos una mano",
                            "podras conocer al resto de la gente."
                            };
         * 
         */

        return dialogo;
    }

    public void comerciar(){

    }

    public void misionar(){

    }

    public void dialogar(){

    }

    /*public void realizaTarea(Jugador pj) throws SQLException{
        c1 = Calendar.getInstance();
        String dia = Integer.toString(c1.get(Calendar.DATE));
        String mes = Integer.toString(c1.get(Calendar.MONTH));
        String annio = Integer.toString(c1.get(Calendar.YEAR));
        System.out.println("año:"+annio);
        System.out.println("mes:"+mes);
        System.out.println("dia:"+dia);
        HashMap datosPj = new HashMap();
        //Carga datos del jugador desde la base de datos
        try{
            datosPj=conect.datosConstruyePersonaje(this.idNpc);
        }
        catch(Exception ex){
            System.out.println("Error al conectar la DB #Jugador carga datos jugador: "+ex);
        }
        int tipo=Integer.parseInt(datosPj.get("tipo").toString());
        int i;

        if(tipo==1){//si es Npc de Misiones
            try{
                datosPj=conect.datosMision(this.idNpc);
            }
            catch(Exception ex){
                System.out.println("Error al conectar la DB #Jugador carga datos jugador: "+ex);
            }
            ResultSet encargo=conect.Consulta("SELECT * FROM encargo WHERE personaje_id="+pj.getIdJugador()+" AND mision_id="+String.valueOf(datosPj.get("idMision")));
            
            if(!encargo.next()){
                System.out.println("Registrar Encargo ");
                int insertar=conect.Ejecutar("INSERT INTO encargo (personaje_id,Mision_id,created_at,rolPersonaje,updated_at) VALUES ("+pj.getIdJugador()+","+String.valueOf(datosPj.get("idMision"))+",'"+annio+"/"+mes+"/"+dia+"',0,"+null+")");
                System.out.println("Insertar encargo filas: "+insertar);

            }else{
                System.out.println("fechaFin"+encargo.getDate("fechaFin"));
                if(encargo.getDate("updated_at")==null){
                    System.out.println("fechaFin");
                    Inventario cInv = new Inventario();
                    boolean finMision=cInv.comparaItem(pj.getIdJugador(), idNpc);
                    if(finMision){
                        System.out.println("Fin mision");
                        int updateEncargo = conect.Ejecutar("Update encargo set updated_at='" + annio + "/" + mes + "/" + dia + "' WHERE Personaje_id=" + pj.getIdJugador() + " AND Mision_id=" + encargo.getShort("Mision_id"));
                        int insertInventarioPj = conect.Ejecutar("INSERT INTO inventario VALUES ("+pj.getIdJugador()+",1,1,0)");
                        
                        int retornoDelete = conect.Ejecutar("DELETE FROM inventario WHERE Personaje_id="+pj.getIdJugador()+" AND Objeto_id="+inv[0].getIdObjeto()+" AND cantidad="+inv[0].getCantidad());
                        if(retornoDelete==0){
                           Inventario[] pjInv = pj.getInv();
                           short cantidad=1;
                            for (int j = 0; j < pjInv.length; j++) {
                                if(pjInv[j].getIdObjeto()==inv[0].getIdObjeto()){
                                    cantidad= (short)(pjInv[j].getCantidad()-inv[0].getCantidad());
                                }
                           }
                           int retornoUpdate = conect.Ejecutar("Update inventario SET cantidad="+cantidad+" WHERE Personaje_id="+pj.getIdJugador()+" AND Objeto_id="+inv[0].getIdObjeto());
                           System.out.println("Filas afectadas Update cantidad: "+retornoUpdate);
                        }
                        System.out.println("Filas afectadas Delete: "+retornoDelete);
                        
                    }
                }else{
                    dialogo=null;
                    dialogo=new String[2];
                }
                try{
                    pj.cargaInventario(pj.getIdJugador());
                }catch(SQLException ex){
                    System.out.println("Error al cargar inventario Pj al menu"+ex);
                }
            }
        }else if(tipo==2){//si es Npc Comerciante

        }else if(tipo==3){//si es Npc Mob

        }
    }

    public void pideItem(short idObjeto, short cantidad){
        
    }

    /*private void cargaInventario(short idPj) throws SQLException{

        ResultSet rTamanoInventario=conect.obtieneTamanoInvetario(idPj);
        ResultSet rInventario=conect.obtieneInvetario(idPj);
        System.out.println("CargaIventario");
        if(rTamanoInventario.next()){
            inv = new Inventario[rTamanoInventario.getInt("filas")];
            //System.out.println("rInventario.getInt('filas')"+rInventario.getInt("filas"));
            //System.out.println("Largo Inv: "+inv.length);
        }
        int i=0;
        while(rInventario.next()){
            inv[i] = new Inventario();
            System.out.println("Indice: "+i);
            System.out.println("rInventario.getShort('idPersonaje')"+rInventario.getShort("idPersonaje"));
            inv[i].setIdPersonaje(rInventario.getShort("idPersonaje"));
            System.out.println("inv["+i+"].isPersonaje: "+inv[i].getIdPersonaje());

            System.out.println("rInventario.getShort('idObjeto')"+rInventario.getShort("idObjeto"));
            inv[i].setIdObjeto(rInventario.getShort("idObjeto"));
            System.out.println("rInventario.getShort('cantidad')"+rInventario.getShort("cantidad"));
            inv[i].setCantidad(rInventario.getShort("cantidad"));
            System.out.println("rInventario.getShort('estaEquipado')"+rInventario.getShort("estaEquipado"));
            inv[i].setEstaEquipado(rInventario.getShort("estaEquipado"));
            System.out.println("rInventario.getString('nombre')"+rInventario.getString("nombre"));
            inv[i].setNombre(rInventario.getString("nombre"));
            i+=1;
        }

    }*/

    @Override
    public void hit(JGObject obj){
            //System.out.println("Hit NPC!"+obj.getName());
    }

    public short getIdNpc() {
        return idNpc;
    }

    public void setIdNpc(short idNpc) {
        this.idNpc = idNpc;
    }

    public String getGrafNpc() {
        return grafNpc;
    }

    public void setGrafNpc(String grafNpc) {
        this.grafNpc = grafNpc;
    }

    public String getNomNpc() {
        return nomNpc;
    }

    public void setNomNpc(String nomNpc) {
        this.nomNpc = nomNpc;
    }

    public int getColId() {
        return colId;
    }

    public void setColId(int colId) {
        this.colId = colId;
    }

    public Inventario[] getInv() {
        return inv;
    }

    public void setInv(Inventario[] inv) {
        this.inv = inv;
    }


    
}