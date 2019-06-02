package baseDeDatos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.CallableStatement;
//import java.awt.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JOptionPane;



import configuracion.ConfiguracionSegura;
import modelo.Alumno;

public class Database {


	private String database;
	private Connection conexion;

	public Database() {

		this.database = "proyectodb";


	}
	
	/**
	 * Metodo utilizado para conectar con la base de datos
	 * @return true o false en base a si se ha podido realizar la conexion
	 */
	public Connection conectar() {
		conexion = null;
		ConfiguracionSegura conf = new ConfiguracionSegura();
		
		
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://"+conf.getHost()+":"+conf.getPort()+"/"+database+"?serverTimezone=UTC", conf.getUser(), conf.getPassword());
				if(conexion != null) {
					System.out.println("Conexion realizada");
				} 
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return conexion;
	}
	
	/**
	 * Desconectar del sistema que almacena la base de datos
	 */
	public void desconectar() {
		try {
			conexion.close();
			System.out.println("Desconexion realizada con exito");
		} catch(SQLException e ) {
			e.printStackTrace();	
		}
	}
	
	
	
	protected int loguearE(String email, String password) {
		String sql = "SELECT COUNT(*) FROM Alumno WHERE email=? AND passwd = PASSWORD(?)"; 
		try(Connection con = conectar(); 
				PreparedStatement pstm=con.prepareStatement(sql)){
			pstm.setString(1, email);
			pstm.setString(2, password);
			ResultSet rs = pstm.executeQuery();
			rs.next();
			return rs.getInt(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	
	/**
	 * Metodo para realizar un insert en una tabla
	 * @param fields columnas de la tabla
	 * @param table tabla en la que se almacenan los datos
	 * @param values valores que se van a insertar
	 * @return devuelve un booleano que indica si se han podido insertar o no
	 */
	protected boolean insertar(String fields, String table, String values) {
		boolean insertado = false;
		String sql = "INSERT INTO "+ table +  " ( " + fields + " ) VALUES ("+ values + ")";
		
		try(Connection con = conectar(); 
				Statement stm = con.createStatement();){
			stm.execute(sql);
			insertado=true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sql);
		return insertado; 
	}
	
	protected boolean insertarCliente(String nombre, String apellidos, String dni, Date fecha_nacimiento, File file) {
		boolean insertado = false;
		String sql = "INSERT INTO Cliente ( nombre,apellidos,dni,fecha_nacimiento,foto ) VALUES ( ?,?,?,?,?)";
		
		FileInputStream fis = null;
		try (Connection con = conectar();PreparedStatement pstm = con.prepareStatement(sql);){
			if (file!=null) {
					fis = new FileInputStream(file);
				
			}
			pstm.setString(1, nombre);
			pstm.setString(2, apellidos);
			pstm.setString(3, dni);
			pstm.setDate(4, fecha_nacimiento);
			if (file == null) {
				pstm.setBinaryStream(5, null);
			} else {
				pstm.setBinaryStream(5, fis, file.length());
			}
			pstm.execute();
			insertado = true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return insertado;
	}
	
	public boolean updateCliente(String nombre, String apellidos, String dni, Date fecha_nacimiento, File file,String id) {
		boolean actualizado = false;
		String sql = "UPDATE Cliente set nombre = ?,apellidos=?,dni=?,fecha_nacimiento=?";
		if (file!=null) {
			sql+=",foto=?";
		}
		sql+= "WHERE id="+id;
		FileInputStream fis = null;
		try (Connection con = conectar();PreparedStatement pstm = con.prepareStatement(sql);){
			if (file!=null) {
					fis = new FileInputStream(file);
				
			}
			pstm.setString(1, nombre);
			pstm.setString(2, apellidos);
			pstm.setString(3, dni);
			pstm.setDate(4, fecha_nacimiento);
			if (file != null) {
				pstm.setBinaryStream(5, fis, file.length());
			} 
			
			pstm.execute();
			actualizado = true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return actualizado;
	}
	
	public boolean update (String fields, String table, String where) {
		boolean actualizado = false;
	
		String sql = "UPDATE "+table+" set "+fields+" WHERE "+where;
		System.out.println(sql);
		try (Connection con = conectar();PreparedStatement pstm = con.prepareStatement(sql);){
			pstm.execute();
			actualizado = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return actualizado;
	}
	
	protected boolean editar(String fields, String table, String where) {
		boolean editado = false;
		String sql = "UPDATE "+table+ " SET "+ fields + " WHERE "+where;
		try(Connection con = conectar(); 
				Statement stm = con.createStatement();){
			stm.execute(sql);
			editado=true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return editado;
	}
	
	/**
	 * Metodo que devuelve todos los resultados de realizar un select
	 * @param fields campos a buscar
	 * @param tables tablas en las que se va a realizar la busqueda
	 * @param where condición que se utilizara en la busqueda
	 * @return devuelve un mapa con los resultados de la consulta
	 */
	protected Map<Integer, ArrayList<Object>> select(String fields, String tables, String where){
		Map<Integer,ArrayList<Object>> resultado = new LinkedHashMap<Integer, ArrayList<Object>>();
		ArrayList<Object> lista;
		
		String query = "SELECT "+ fields + " FROM "+ tables;
		if (where != null) {
			query += " WHERE "+ where;
		}
		System.out.print(query);
		try(Connection con = conectar();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(query)){
			
			int numColumnas = rs.getMetaData().getColumnCount();
			
			while (rs.next()) {
				lista = new ArrayList<Object>();
				
				for (int i = 1 ; i <= numColumnas ; i++ ) 
					lista.add(rs.getObject(i));
				
				resultado.put(rs.getInt("Id"), lista);
			}
			
			
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		}
		
		return resultado;
	}
	protected Map<Integer, ArrayList<Object>> selectPeriodo(String fields, String tables, String where){
		Map<Integer,ArrayList<Object>> resultado = new LinkedHashMap<Integer, ArrayList<Object>>();
		ArrayList<Object> lista;
		
		String query = "SELECT "+ fields + " FROM "+ tables;
		if (where != null) {
			query += " WHERE "+ where;
		}
		System.out.print(query);
		try(Connection con = conectar();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(query)){
			
			int numColumnas = rs.getMetaData().getColumnCount();
			
			while (rs.next()) {
				lista = new ArrayList<Object>();
				
				for (int i = 1 ; i <= numColumnas ; i++ ) 
					lista.add(rs.getObject(i));
				
				resultado.put(rs.getInt("idPeriodo"), lista);
			}
			
			
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		}
		
		return resultado;
	}
	protected Map<Integer, ArrayList<Object>> select(String fields, String tables, String where,String id){
		Map<Integer,ArrayList<Object>> resultado = new LinkedHashMap<Integer, ArrayList<Object>>();
		ArrayList<Object> lista;
		
		String query = "SELECT "+ fields + " FROM "+ tables;
		if (where != null) {
			query += " WHERE "+ where;
		}
		System.out.print(query);
		try(Connection con = conectar();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(query)){
			
			int numColumnas = rs.getMetaData().getColumnCount();
			
			while (rs.next()) {
				lista = new ArrayList<Object>();
				
				for (int i = 1 ; i <= numColumnas ; i++ ) 
					lista.add(rs.getObject(i));
				
				resultado.put(rs.getInt(id), lista);
			}
			
			
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		}
		
		return resultado;
	}


	
	

	
	protected boolean eliminar(String table,String where) {
		
		String sql = "DELETE FROM " +table + " WHERE " + where;
		
		try (Connection con= conectar(); Statement stm = con.createStatement();)
		{
			stm.execute(sql);
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block	
			if (e instanceof SQLIntegrityConstraintViolationException) {
				JOptionPane.showMessageDialog(null, "Se tienen que eliminar las dependencias antes", "Error", JOptionPane.ERROR_MESSAGE,null);
			}
			e.printStackTrace();
			return false;
		}
		
	}
	
	protected boolean insertarFactura(int clienteId) {
		boolean insertado=false;
		String sql = "{CALL  crear_factura(?,?)}";
		
		try (Connection con = conectar(); CallableStatement cst = con.prepareCall(sql);) {
					int i = 0;

					cst.registerOutParameter(++i, java.sql.Types.INTEGER);
					cst.setInt(++i, clienteId);

					cst.execute();

					int numFactura = cst.getInt(1);
					System.out.println("La factura creada es la numero: " + numFactura);
					insertado = true;
			} catch (SQLException e) {
					System.out.println(e.getErrorCode());
					System.out.println(e.getMessage());
				}
		
		
		
		return insertado;
		
	}
	
	protected boolean deleteFactura(int idFactura) {
		boolean eliminado=false;
		String sql = "{CALL  eliminar_factura(?)}";
		
		try (Connection con = conectar(); CallableStatement cst = con.prepareCall(sql);) {
					int i = 0;

					cst.setInt(++i, idFactura);

					cst.execute();
					eliminado = true;
			} catch (SQLException e) {
					System.out.println(e.getErrorCode());
					System.out.println(e.getMessage());
				}
		
		
		
		return eliminado;
		
	}
	
	protected boolean insertarLin_Fac(int idFactura,int idProducto, int cant) {
		boolean insertado=false;
		String sql = "{CALL  insertar_linea(?,?,?)}";
		
		try (Connection con = conectar(); CallableStatement cst = con.prepareCall(sql);) {
					int i = 0;

					cst.setInt(++i, idFactura);
					cst.setInt(++i, idProducto);
					cst.setInt(++i, cant);

					cst.execute();
					insertado = true;
			} catch (SQLException e) {
					System.out.println(e.getErrorCode());
					System.out.println(e.getMessage());
				}
		
		
		
		return insertado;
		
	}
	
	protected boolean eliminarLin_Fac(int idFactura,int linea) {
		boolean eliminado=false;
		String sql = "{CALL  eliminar_linea(?,?)}";
		
		try (Connection con = conectar(); CallableStatement cst = con.prepareCall(sql);) {
					int i = 0;

					cst.setInt(++i, idFactura);
					cst.setInt(++i, linea);
					

					cst.execute();
					eliminado = true;
			} catch (SQLException e) {
					System.out.println(e.getErrorCode());
					System.out.println(e.getMessage());
				}
		
		
		
		return eliminado;
		
	}
	

}
