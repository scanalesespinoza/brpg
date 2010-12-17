package clases;

import java.sql.*;
import java.util.HashMap;
/**
 *
 * @author Iarwain
 * @versio 1.0 16/09/10
 * @Descripcion: Delegador de transacciones de datos.
 */
public class dbDelegate {

   static final String bd = "db_web_tt4";
   static final String login = "root";
   static final String password = "Vicarious";
   static String url = "jdbc:mysql://felhunter.sytes.net:3306/"+bd;
   private Statement St;
   private ResultSet reg;

   public Connection conn = null;



   /*
    * dbDelegate Constructor.
    */
   public dbDelegate(){
      try {

         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(url,login,password);
         System.out.println("Conexion ´dbDelegate´ correcta");
         if (conn != null) {
            System.out.println("Conexión a base de datos "+url+" ... Ok");
         }
      }
      catch(SQLException ex) {
         System.out.println("Hubo un problema al intentar conectarse con la base de datos "+url);
      }
      catch(ClassNotFoundException ex) {
         System.out.println(ex);
      }

   }
   public void cierraDbCon() throws Exception{
        conn.close();
   }

//    public HashMap obtieneDatosPersonaje(short id){
//        System.out.println("Inicio obtiene datos personaje");
//        HashMap hs = new HashMap();
//        String StrSql = "SELECT pjuno.id id, pjuno.nombre nombre, pjuno.nivel nivel, pjuno.posicionX posX, pjuno.posicionY posY, pjdos.vitalidad vit, pjdos.destreza des, pjdos.sabiduria sab, pjdos.fuerza fue, pjdos.totalPuntosHabilidad ptosHab, pjdos.totalPuntosEstadistica ptosEst, pjdos.limiteSuperiorExperiencia limExp, pjdos.experiencia experiencia, pjdos.pesoSoportado peso, pjdos.fechaCreacion, pjdos.estaBaneado ban, pjdos.Cuenta_id cuenta FROM personaje pjuno, jugador pjdos WHERE pjuno.id="+id+" and pjdos.Personaje_id="+id;
//        try{
//            Statement st = conn.createStatement();
//            //System.out.println("Crea stamento");
//            ResultSet res = st.executeQuery(StrSql);
//            //System.out.println("ejecuta query");
//            if (res.next()) {
//              //System.out.println("consulta primera fila");
//              //System.out.println("Obtiene ID: "+res.getString("id"));
//
//              hs.put("id",res.getString("id"));
//              System.out.println("ID guardada en el hashmap");
//              hs.put("nombre",res.getString("nombre"));
//              hs.put("nivel",res.getString("nivel"));
//              hs.put("posX",res.getString("posX"));
//              hs.put("posY",res.getString("posY"));
//              hs.put("vit",res.getString("vit"));
//              hs.put("des",res.getString("des"));
//              hs.put("sab",res.getString("sab"));
//              hs.put("fue",res.getString("fue"));
//              hs.put("ptosHab",res.getString("ptosHab"));
//              hs.put("ptosEst",res.getString("ptosEst"));
//              hs.put("limExp",res.getString("limExp"));
//              hs.put("experiencia",res.getString("experiencia"));
//              hs.put("peso",res.getString("peso"));
//              hs.put("fechaCreacion",res.getString("fechaCreacion"));
//              hs.put("ban",res.getString("ban"));
//              hs.put("cuenta",res.getString("cuenta"));
//            }
//        }
//        catch(SQLException ex) {
//        System.out.println("Hubo un problema al intentar conectarse con la base de datos "+ex);
//        }
//        return hs;

//    }
    public void actualizaPersonaje(Jugador pj){

    }

//    public short[] obtienePosPersonaje(short id){
//        short pos[]=null;
//        String StrSql = "SELECT pjuno.id id, pjuno.nombre nombre, pjuno.nivel nivel, pjuno.posicionX posX, pjuno.posicionY posY, pjdos.vitalidad vit, pjdos.destreza des, pjdos.sabiduria sab, pjdos.fuerza fue, pjdos.totalPuntosHabilidad ptosHab, pjdos.totalPuntosEstadistica ptosEst, pjdos.limiteSuperiorExperiencia limExp, pjdos.experiencia experiencia, pjdos.pesoSoportado peso, pjdos.fechaCreacion, pjdos.estaBaneado ban, pjdos.Cuenta_id cuenta FROM personaje pjuno, jugador pjdos WHERE pjuno.id="+id+" and pjdos.Personaje_id="+id;
//        try{
//            Statement st = conn.createStatement();
//            ResultSet res = st.executeQuery(StrSql);
//            if(res.next()) {
//              pos[0] = res.getShort("posicionX");
//              pos[1] = res.getShort("posicionY");
//            }
//        }
//        catch(SQLException ex) {
//        System.out.println("Hubo un problema al intentar conectarse con la base de datos "+ex);
//        }
//        return pos;
//
//    }

    public HashMap datosConstruyePersonaje(short id){
        HashMap dataIni=new HashMap();
        String StrSql = "SELECT nombre, nivel, posicionX posX, posicionY posY, tipo FROM personaje WHERE id="+id;
        try{
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(StrSql);
            if (res.next()) {
              dataIni.put("nombrePj",res.getString("nombre"));
              //System.out.println("nombre: "+res.getString("nombre"));
              dataIni.put("nivelPj",res.getShort("nivel"));
              //System.out.println("nivel: "+res.getShort("nivel"));
              dataIni.put("posX",res.getShort("posX"));
              dataIni.put("posY",res.getShort("posY"));
              dataIni.put("tipo",res.getShort("tipo"));
            }
        }
        catch(SQLException ex) {
        System.out.println("Hubo un problema al intentar conectarse con la base de datos "+ex);
        }
        return dataIni;

    }

    public HashMap datosMision(short id){
        HashMap dataIni=new HashMap();
        String StrSql = "SELECT * FROM mision WHERE personaje_id="+id;
        try{
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(StrSql);
            if (res.next()) {
              dataIni.put("idMision",res.getShort("id"));
              //System.out.println("nombre: "+res.getString("nombre"));
              dataIni.put("nomMision",res.getString("nombre"));
              //System.out.println("nivel: "+res.getShort("nivel"));
              dataIni.put("descMision",res.getString("descripcion"));
              dataIni.put("nivelRequerido",res.getShort("nivelRequerido"));
              dataIni.put("recompensaExp",res.getInt("recompensaExp"));
              dataIni.put("repetible",res.getShort("repetible"));
            }
        }
        catch(SQLException ex) {
        System.out.println("Hubo un problema al intentar conectarse con la base de datos "+ex);
        }
        return dataIni;

    }

    public void actualizaPosicionJugador(int id, int posicionX, int posicionY){
        System.out.println("ID :"+id);
        System.out.println("X :"+posicionX);
        System.out.println("Y :"+posicionY);
        String StrSql = "UPDATE personaje SET posicionX=" + posicionX + ", posicionY=" + posicionY + " WHERE id="+id;
        try{
            Statement st = conn.createStatement();
            int actualizadas = st.executeUpdate(StrSql);
            System.out.println("Se actualizaron "+actualizadas+" filas");

        }
        catch(SQLException ex) {
        System.out.println("Hubo un problema al intentar conectarse con la base de datos "+ex);
        }



    }
    
    public void actualizaInventario(short idPersonaje, short idItem, int cantidad,short equipado){
        String StrSql = "INSERT INTO inventario VALUES ("+idPersonaje+","+idItem+","+cantidad+","+equipado+")";
        try{
            Statement st = conn.createStatement();
            boolean insertada = st.execute(StrSql);
            System.out.println("Insersion: "+insertada);

        }
        catch(SQLException ex) {
        System.out.println("Hubo un problema al intentar conectarse con la base de datos "+ex);
        }  
    }

//    public ResultSet obtieneInvetario(short id){
//        String StrSql = "SELECT inv.Personaje_id idPersonaje, inv.Objeto_id idObjeto, inv.cantidad cantidad, obj.nombre nombre, inv.estaEquipado estaEquipado  FROM inventario inv, objeto obj WHERE inv.Personaje_id="+id+" AND inv.Objeto_id=obj.id";
//        ResultSet inventario = null;
//        try{
//            Statement st = conn.createStatement();
//            inventario = st.executeQuery(StrSql);
//
//        }
//        catch(SQLException ex) {
//        System.out.println("Hubo un problema al intentar conectarse con la base de datos "+ex);
//        }
//        return inventario;
//    }
    public ResultSet obtieneTamanoInvetario(short id){
        String StrSql = "SELECT count(Personaje_id) filas FROM inventario WHERE Personaje_id="+id+" group by Personaje_id";
        ResultSet inventario = null;
        try{
            Statement st = conn.createStatement();
            inventario = st.executeQuery(StrSql);

        }
        catch(SQLException ex) {
        System.out.println("Hubo un problema al intentar conectarse con la base de datos "+ex);
        }
        return inventario;
    }

    public ResultSet comparaInvetario(short idJugador, short idNpc){
        String StrSql = "SELECT invuno.cantidad  FROM inventario invuno, inventario invdos  WHERE invuno.Personaje_id="+idJugador+" AND invdos.Personaje_id="+idNpc+" AND invuno.Objeto_id=invdos.Objeto_id AND invuno.cantidad>invdos.cantidad-1";
        ResultSet inventario = null;
        try{
            Statement st = conn.createStatement();
            inventario = st.executeQuery(StrSql);

        }
        catch(SQLException ex) {
        System.out.println("Hubo un problema al intentar conectarse con la base de datos "+ex);
        }
        return inventario;
    }

    public void agregarItem(short idJugador, short idItem,short cantidad) throws SQLException{
        ResultSet rs = Consulta("SELECT * FROM inventario WHERE Personaje_id="+idJugador+" AND Objeto_id="+idItem);
        if(rs.next()){
            short vCantiadad = (short)(rs.getShort("cantidad")+cantidad);
            int insertInventarioPj = Ejecutar("Update inventario set cantidad="+vCantiadad+" WHERE Personaje_id="+idJugador+" AND Objeto_id="+idItem);
        }else{
            int insertInventarioPj = Ejecutar("INSERT INTO inventario VALUES ("+idJugador+","+idItem+","+cantidad+",0)");
        }
    }

    public int Ejecutar(String sql) {
        int resultado=0;
        try {
            St = conn.createStatement();
            resultado = St.executeUpdate(sql);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return (resultado);
    }

    public ResultSet Consulta(String sql) {
        String error = "";
        try {
            St = conn.createStatement();
            reg = St.executeQuery(sql);
        } catch (Exception ee) {
            error = ee.getMessage();
        }
        return (reg);
    }

}
