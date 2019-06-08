package baseDeDatos;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
	
	public LinkedHashSet<Integer> obtenerCursos(){
		LinkedHashSet<Integer> resultado = new LinkedHashSet<Integer>();
		
		String query = "SELECT cursoYear FROM Curso ORDER BY cursoYear DESC";
		
		System.out.print(query);
		try(Connection con = conectar();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(query)){
						
			while (rs.next()) {
				resultado.add(rs.getInt("cursoYear"));
			}
			
			
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		}
		
		return resultado;
		
	}
	
	
	public HashSet<LocalDate> obtenerDias(int curso) {
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
		return update("email="+"'"+email+"'","Reserva","reserva_dia='"+dia+"' AND reserva_hora='"+hora+"'");
		
	}
	
	public boolean modificarReserva(String email, LocalDate dia, LocalTime hora) {
		boolean actualizado =false;
		actualizado=update("email=NULL","Reserva","email='"+email+"'");
		if(actualizado==true)
			update("email="+"'"+email+"'","Reserva","reserva_dia='"+dia+"' AND reserva_hora='"+hora+"'");
		
		return actualizado;

	}
	
	public boolean eliminarReserva(String email) {
		return anularReserva(email);
	}
	
	public String obtenerMensajeCorreo() {
		String sql = "SELECT Cuerpo FROM Mensaje WHERE tipoMensaje='correo'"; 
		try(Connection con = conectar(); 
				Statement stm=con.createStatement();
				ResultSet rs = stm.executeQuery(sql);){
			
			rs.next();
			return rs.getString(1);
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean insertarAlumno(Alumno a,String passwd) {
		
		return insert("nombre,apellidos,email,login,passwd","Alumno","'"+a.getNombre()+"','"+a.getApellidos()+"','"+a.getEmail()+"','"+a.getLogin()+"',PASSWORD('"+passwd+"')");
		
	}
	
	//****************************************************************PARA ADMINISTRADOR********************************
	
	public boolean insertarCurso(int curso) {
		return insert("cursoYear","Curso",String.valueOf(curso));
		
	}
	
	public boolean addPeriodo(Date diaInicio,Date diaFin,Time horaInicio,Time horaFin, Time intervalo, int cursoYear) {
		return crearPeriodo(diaInicio,diaFin,horaInicio,horaFin,intervalo,cursoYear);
	}
	
	public Map<Integer,Periodo> obtenerPeriodos(int curso){
		
		Map<Integer,Periodo> resultadoSalida = new LinkedHashMap<Integer,Periodo>();
		Map<Integer,ArrayList<Object>> resultadoBD=new LinkedHashMap<Integer,ArrayList<Object>>();
		
		resultadoBD = select("idPeriodo,dia_inicio,dia_fin,hora_inicio,hora_fin,intervalo,habilitado","Periodo","cursoYear="+curso+" ORDER BY idPeriodo DESC","idPeriodo");
		
		LocalDate dia_inicio;
		LocalDate dia_fin;
		LocalTime hora_inicio;
		LocalTime hora_fin;
		LocalTime intervalo;
		boolean habilitado;
		
	
		
		for(Integer key : resultadoBD.keySet()) {
			
			dia_inicio = LocalDate.parse(String.valueOf(resultadoBD.get(key).get(1)));
			dia_fin = LocalDate.parse(String.valueOf(resultadoBD.get(key).get(2)));
			hora_inicio = LocalTime.parse(String.valueOf(resultadoBD.get(key).get(3)));
			hora_fin = LocalTime.parse(String.valueOf(resultadoBD.get(key).get(4)));
			intervalo = LocalTime.parse(String.valueOf(resultadoBD.get(key).get(5)));
			habilitado =(boolean) resultadoBD.get(key).get(6);
			
			
			resultadoSalida.put(key, new Periodo(key,dia_inicio,dia_fin,hora_inicio,hora_fin,intervalo,habilitado,curso));
		}
		
		return resultadoSalida;
	}
	 
	public Periodo obtenerPeriodo(int idPeriodo) {
		Periodo resultado=null;
		String query = "SELECT idPeriodo,dia_inicio,dia_fin,hora_inicio,hora_fin,intervalo,habilitado,cursoYear FROM Periodo WHERE idPeriodo = "+idPeriodo;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm");
		LocalDate dia_inicio;
		LocalDate dia_fin;
		LocalTime hora_inicio;
		LocalTime hora_fin;
		LocalTime intervalo;
		boolean habilitado;
		String auxCurso;
		int cursoYear;
		
		try (Connection con = conectar();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(query);){
			
			
			rs.next();
			
				dia_inicio = rs.getDate("dia_inicio").toLocalDate();
				dia_fin = rs.getDate("dia_fin").toLocalDate();
				hora_inicio = LocalTime.parse(String.valueOf(rs.getTime("hora_inicio").toLocalTime()), dtf);
				hora_fin  =LocalTime.parse(String.valueOf(rs.getTime("hora_fin").toLocalTime()), dtf);
				intervalo = LocalTime.parse(String.valueOf(rs.getTime("intervalo").toLocalTime()), dtf);
				habilitado = rs.getBoolean("habilitado");
				auxCurso =rs.getString("cursoYear").toString();
				cursoYear=Integer.parseInt(auxCurso.substring(0,4));
				
				
				resultado= new Periodo(idPeriodo,dia_inicio,dia_fin,hora_inicio,hora_fin,intervalo,habilitado,cursoYear);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return resultado;
	}
	
}
	
	

