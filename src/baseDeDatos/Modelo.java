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
			if (a.getEmail()!=null) {
				return a;
			} else
				return null;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return a;
		}
	}
	
	/**
	 * Metodo para obtener una lista con los cursos disponibles
	 * @return lista de cursos disponibles
	 */
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
	
	
	/**
	 * Método para obtener los dias de reserva de un curso
	 * @param curso para el que se va a realizar la reserva
	 * @return lista con los dias
	 */
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
	
	/**
	 * Metodo para obtener las reservas de un día
	 * @param dia del que se quieren mostrar las reservas
	 * @return mapa con los dias y las horas de las reservas disponibles
	 */
	public Map<Integer,Reserva> obtenerReservas(Date dia){
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
	
	/**
	 * Método que comprueba si un alumno tiene alguna reserva ya
	 * @param email del alumno
	 * @return número de reservas que ha realizado el alumno
	 */
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
	
	/**
	 * Metodo para realizar la inserción de una reserva de un alumno
	 * @param email del alumno 
	 * @param dia de la reserva
	 * @param hora de la reserva
	 * @return true si se ha realizado la reserva
	 */
	public boolean reservar(String email, LocalDate dia, LocalTime hora) {
		return update("email="+"'"+email+"'","Reserva","reserva_dia='"+dia+"' AND reserva_hora='"+hora+"'");
		
	}
	
	/**
	 * Método que se encarga de realizar una reserva
	 * @param email del alumno
	 * @param dia de la reserva nueva
	 * @param hora de la reserva nueva
	 * @return true si se ha podido modificar la reserva
	 */
	public boolean modificarReserva(String email, LocalDate dia, LocalTime hora) {
		boolean actualizado =false;
		actualizado=update("email=NULL","Reserva","email='"+email+"'");
		if(actualizado==true)
			update("email="+"'"+email+"'","Reserva","reserva_dia='"+dia+"' AND reserva_hora='"+hora+"'");
		
		return actualizado;

	}
	
	/**
	 * Elimina la reserva de un alumno
	 * @param email del alumno
	 * @return true si se ha podido eliminar
	 */
	public boolean eliminarReserva(String email) {
		return anularReserva(email);
	}
	
	/**
	 * Método para obtener el mensaje de correo que se va a enviar al alumno
	 * @return String con el mensaje
	 */
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
	
	/**
	 * Método para insertar un alumno nuevo en el registro
	 * @param a
	 * @param passwd
	 * @return
	 */
	public boolean insertarAlumno(Alumno a,String passwd) {
		return insert("nombre,apellidos,email,login,passwd","Alumno","'"+a.getNombre()+"','"+a.getApellidos()+"','"+a.getEmail()+"','"+a.getLogin()+"',PASSWORD('"+passwd+"')");
		
	}
	/**
	 * Metodo para obtener la reserva de un alumno
	 * @param email del alumno
	 * @return String con el dia y la hora de la reserva
	 */
	public String obtenerReserva(String email) {
		String sql = "SELECT reserva_dia,reserva_hora FROM Reserva WHERE email='"+email+"'"; 
		LocalDate reserva_dia;
		LocalTime reserva_hora;
		
		try(Connection con = conectar(); 
				Statement stm=con.createStatement();
				ResultSet rs = stm.executeQuery(sql);){
			
			rs.next();
			
			reserva_dia = LocalDate.parse(String.valueOf(rs.getDate("reserva_dia")));
			reserva_hora = LocalTime.parse(String.valueOf(rs.getTime("reserva_hora")));
			
			
			return "Tu reserva es el día "+reserva_dia.toString()+" A las "+reserva_hora;
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	// *******************************************************************************************
	// ************************************PARA ADMINISTRADOR*************************************
	// *******************************************************************************************
	
	/**
	 * Insertar un curso nuevo
	 * @param curso que se insertará
	 * @return true si se ha podido insertar
	 */
	public boolean insertarCurso(int curso) {
		return insert("cursoYear","Curso",String.valueOf(curso));
		
	}
	
	/**
	 * Método que añade un nuevo periodo
	 * @param diaInicio del periodo
	 * @param diaFin del periodo
	 * @param horaInicio del periodo
	 * @param horaFin del periodo
	 * @param intervalo del periodo
	 * @param cursoYear del periodo
	 * @return true si se ha podido insertar
	 */
	public boolean addPeriodo(Date diaInicio,Date diaFin,Time horaInicio,Time horaFin, Time intervalo, int cursoYear) {
		return crearPeriodo(diaInicio,diaFin,horaInicio,horaFin,intervalo,cursoYear);
	}
	
	/**
	 * Devuelve un map ordenado con los periodos de un curso
	 * @param curso del que se obtendran los periodos
	 * @return Map con los periodos
	 */
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
	 
	/**
	 * Obtiene un periodo por un idPeriodo
	 * @param idPeriodo del que se quiere obtener
	 * @return Periodo que devuelve la base de datos
	 */
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
	
	/**
	 * Metodo que modifica un periodo
	 * @param idPeriodo
	 * @param dia_inicio
	 * @param dia_fin
	 * @param hora_inicio
	 * @param hora_fin
	 * @param intervalo
	 * @param habilitado
	 * @param cursoYear
	 * @return true si se ha podido modificar el periodo
	 */
	public boolean modPeriodo(int idPeriodo,Date dia_inicio,Date dia_fin,Time hora_inicio,Time hora_fin,Time intervalo,Boolean habilitado,int cursoYear) {
		
		return update("dia_inicio='"+dia_inicio+"',dia_fin='"+dia_fin+"',hora_inicio='"+hora_inicio+"',hora_fin='"+hora_fin+
				"',intervalo="+intervalo+"',habilitado="+habilitado+",cursoYear="+cursoYear,"Periodo","idPeriodo="+idPeriodo);
		
	}
	
	/**
	 * Método para obtener las reservas de un periodo
	 * @param idPeriodo del que se quieren obtener las reservas
	 * @return Map con las reservas del periodo
	 */
	public Map<Integer,Reserva> obtenerReservasPeriodo(int idPeriodo){
	
		Map<Integer,Reserva> resultadoSalida = new LinkedHashMap<Integer,Reserva>();
		Map<Integer,ArrayList<Object>> resultadoBD=new LinkedHashMap<Integer,ArrayList<Object>>();
		
		resultadoBD = select("idReserva,reserva_dia,reserva_hora,idPeriodo,email","Reserva","idPeriodo = "+idPeriodo,"idReserva");
		
		LocalDate reserva_dia;
		LocalTime reserva_hora;
		String email;
	
		
		for(Integer key : resultadoBD.keySet()) {
			
			reserva_dia = LocalDate.parse(String.valueOf(resultadoBD.get(key).get(1)));
			reserva_hora = LocalTime.parse(String.valueOf(resultadoBD.get(key).get(2)));
			idPeriodo = Integer.parseInt(String.valueOf(resultadoBD.get(key).get(3)));
			email = String.valueOf(resultadoBD.get(key).get(4));
			
			resultadoSalida.put(key, new Reserva(key,email,reserva_dia,reserva_hora,idPeriodo));
		}
		
		return resultadoSalida;
	}
	
	/**
	 * Método que actualiza el mensaje que se les enviará a los alumnos
	 * @param mensaje 
	 * @return true si se ha podido actualizar
	 */
	public boolean updateMensaje(String mensaje) {
		return update("Cuerpo='"+mensaje+"'","Mensaje","tipoMensaje='correo'");
	}
	
}
	
	

