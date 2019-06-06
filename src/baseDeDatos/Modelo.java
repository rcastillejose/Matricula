package baseDeDatos;

import java.awt.Image;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import modelo.Alumno;
import modelo.Periodo;
import modelo.Reserva;


public class Modelo extends Database {
	/**
	 * En el modelo se realizara la estructura de las sql
	 */
	public Modelo() {
		super();
	}

	/**
	 * autentificar en la base de datos para el acceso a los alumnos
	 * @param login nombre de alumno
	 * @param passwd contraseña del alumno
	 * @return devuelve si se ha podido autentificar que los campos son correctos
	 */
	public Alumno loguear(String login, String password) {
		Alumno a = null;
		String nombre=null;
		String apellidos=null;
		String email=null;
		String loginA=null;
		
		String sql = "SELECT email,nombre,apellidos,login FROM Alumno WHERE (login=? OR email=?) AND passwd = PASSWORD(?)"; 
		try(Connection con = conectar(); 
				PreparedStatement pstm=con.prepareStatement(sql)){

			pstm.setString(1, login);
		
			pstm.setString(2, login);
			
			pstm.setString(3, password);
			
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				email = rs.getString("email");
				nombre = rs.getString("nombre");
				apellidos = rs.getString("apellidos");
				loginA=rs.getString("login");
			}
			
			a = new Alumno(email,nombre,apellidos,loginA);
			System.out.println(a);
			return a;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return a;
		}
	}
	
	public ArrayList<String> obtenerCursos(){
		ArrayList<String> resultado = new ArrayList<String>();
		
		String query = "SELECT cursoYear FROM Curso";
		
		System.out.print(query);
		try(Connection con = conectar();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(query)){
						
			while (rs.next()) {
				resultado.add(rs.getString("cursoYear"));
			}
			
			
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		}
		
		return resultado;
		
	}
	
	
	public HashSet<LocalDate> obtenerDias(String curso) {
		HashSet<LocalDate> resultado = new HashSet<LocalDate>();
		String query = "select r.reserva_dia from Reserva r join Periodo p on(r.idPeriodo=r.idPeriodo) " + 
				"where p.cursoYear="+curso+" AND p.habilitado = 1 group by r.reserva_dia";
		LocalDate dia;
		
		try (Connection con = conectar();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(query);){
			
			
			while(rs.next()) {
				dia = rs.getDate(1).toLocalDate();
				resultado.add(dia);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	public Map<Integer,Reserva> obtenerReservas(Date dia){
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		
		Map<Integer,Reserva> resultadoSalida = new LinkedHashMap<Integer,Reserva>();
		Map<Integer,ArrayList<Object>> resultadoBD=new LinkedHashMap<Integer,ArrayList<Object>>();
		
		resultadoBD = select("idReserva,reserva_dia,reserva_hora,idPeriodo","Reserva","reserva_dia = "+"'"+dia+"' AND email IS NULL","idReserva");
		
		LocalDate reserva_dia;
		LocalTime reserva_hora;
		int idPeriodo;
	
		
		for(Integer key : resultadoBD.keySet()) {
			
			reserva_dia = LocalDate.parse(String.valueOf(resultadoBD.get(key).get(1)));
			reserva_hora = LocalTime.parse(String.valueOf(resultadoBD.get(key).get(2)));
			idPeriodo = Integer.parseInt(String.valueOf(resultadoBD.get(key).get(3)));
			
			resultadoSalida.put(key, new Reserva(key,null,reserva_dia,reserva_hora,idPeriodo));
		}
		
		return resultadoSalida;
	}
	
	public int comprobarAlumno(String  email) {
		String sql = "SELECT COUNT(*) FROM Reserva WHERE email= '"+email+"'"; 
		try(Connection con = conectar(); 
				Statement stm=con.createStatement();
				ResultSet rs = stm.executeQuery(sql);){
			
			rs.next();
			return rs.getInt(1);
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public boolean reservar(String email, LocalDate dia, LocalTime hora) {
		return update("Reserva","email="+"'"+email+"'","reserva_dia='"+dia+"' AND reserva_hora='"+hora+"'");
		
	}
	
	 
	
}
	
	

