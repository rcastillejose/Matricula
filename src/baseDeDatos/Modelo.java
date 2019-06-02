package baseDeDatos;

import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
	
	public Map<Integer, Periodo> obtenerPeriodosReservas(String curso){
		
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
			
			Map<Integer,Periodo> resultadoSalida = new LinkedHashMap<Integer,Periodo>();
			Map<Integer,ArrayList<Object>> resultadoBD;
			
			resultadoBD=select("idPeriodo,dia_inicio,dia_fin,hora_inicio,hora_fin,intervalo,habilitado,cursoYear","Periodo","cursoYear="+curso+" AND habilitado=1");
			
			LocalDate dia_inicio;
			LocalDate dia_fin;
			LocalTime hora_inicio;
			LocalTime hora_fin;
			LocalTime intervalo;
			int habilitado;
			String cursoYear;
			
			for(Integer key : resultadoBD.keySet()) {
				
				dia_inicio = LocalDate.parse(String.valueOf(resultadoBD.get(key).get(2)),formatter);
				dia_fin = LocalDate.parse(String.valueOf(resultadoBD.get(key).get(3)),formatter);
				hora_inicio = LocalTime.parse(String.valueOf(resultadoBD.get(key).get(3)),dtf);
				hora_fin = LocalTime.parse(String.valueOf(resultadoBD.get(key).get(3)),dtf);
				intervalo = LocalTime.parse(String.valueOf(resultadoBD.get(key).get(3)),dtf);
				habilitado = 1;
				
				resultadoSalida.put(key, new Periodo(key,dia_inicio,dia_fin,hora_inicio,hora_fin,intervalo,habilitado,curso));
			}
			
			return resultadoSalida;
	}
	
	public Map<Integer,Reserva> obtenerReservas(LocalDate dia){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		
		Map<Integer,Reserva> resultadoSalida = new LinkedHashMap<Integer,Reserva>();
		Map<Integer,ArrayList<Object>> resultadoBD=null;
		
		String query = "select idPeriodo,email,reserva_dia,reserva_hora,idReserva from Reserva where reserva_dia = "+"'"+dia+"'";
		
		int idPeriodo;
		String email;
		LocalDate reserva_dia;
		LocalTime reserva_hora;
		int idReserva;
		
		
		for(Integer key : resultadoBD.keySet()) {
			
			idPeriodo = Integer.parseInt(String.valueOf(resultadoBD.get(key).get(1)));
			email = resultadoBD.get(key).get(2).toString();
			reserva_dia = LocalDate.parse(String.valueOf(resultadoBD.get(key).get(3)),formatter);
			reserva_hora = LocalTime.parse(String.valueOf(resultadoBD.get(key).get(4)),dtf);
			idReserva = Integer.parseInt(String.valueOf(resultadoBD.get(key).get(5)));
			
			resultadoSalida.put(key, new Reserva(idPeriodo,email,reserva_dia,reserva_hora,idReserva));
		}
		
		return resultadoSalida;
	}
	
}
	
	

