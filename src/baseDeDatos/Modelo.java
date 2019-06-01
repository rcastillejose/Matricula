package baseDeDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import modelo.Alumno;

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
		ArrayList<String> resultadoSalida = new ArrayList<String>();
		Map<Integer,ArrayList<Object>> resultadoBD;
		String fields="cursoYear";
		resultadoBD=select("cursoYear","Curso",null);
		String descripcion;
		
		for(Integer key : resultadoBD.keySet()) {
			
			descripcion = resultadoBD.get(key).get(1).toString();
			resultadoSalida.add(descripcion);
		}
		
		return resultadoSalida;
	}
		
}
	
	
	
	

