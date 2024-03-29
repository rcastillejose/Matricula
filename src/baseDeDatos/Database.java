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
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

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
			conexion = DriverManager.getConnection("jdbc:mysql://"+conf.getHost()+":"+conf.getPort()+"/"+database
					+"?useLegacyDatetimeCode=false&serverTimezone=Europe/Madrid", conf.getUser(), conf.getPassword());
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
	protected boolean insert(String fields, String table, String values) {
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
	
	
	protected boolean anularReserva(String email) {
		boolean anulada=false;
		String sql = "{CALL anular_reserva(?)}";
		
		try (Connection con = conectar();
				CallableStatement cst = con.prepareCall(sql);) {
					//int i = 0;

					cst.setString(1, email);
					

					cst.execute();
					anulada = true;
			} catch (SQLException e) {
					System.out.println(e.getErrorCode());
					System.out.println(e.getMessage());
				}
		
		return anulada;
		
	}
	
	protected boolean crearPeriodo(Date diaInicio,Date diaFin,Time horaInicio,Time horaFin, Time intervalo, int cursoYear) {
		boolean creado = false;
		String sql = "{CALL crear_periodo(?,?,?,?,?,?)}";
		System.out.println(sql);
		
		try (Connection con = conectar();
				CallableStatement cst = con.prepareCall(sql);) {
					//int i = 0;

					cst.setDate(1, diaInicio);
					cst.setDate(2, diaFin);
					cst.setTime(3, horaInicio);
					cst.setTime(4, horaFin);
					cst.setTime(5, intervalo);
					cst.setInt(6, cursoYear);
					

					cst.execute();
					creado = true;
			} catch (SQLException e) {
				e.printStackTrace();
					System.out.println(e.getErrorCode());
					JOptionPane.showMessageDialog(null,e.getErrorCode()+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
		
		return creado;
	}
	

}
