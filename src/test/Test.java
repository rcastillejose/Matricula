package test;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import configuracion.ConfiguracionSegura;
import configuracion.LDAP;
import modelo.Persona;
import modelo.Profesor;


public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ConfiguracionSegura conf = new ConfiguracionSegura();
		
		ArrayList<Persona> listado = new ArrayList<Persona>();
		
		LDAP ldap = new LDAP();
	
		//Listar miembros de un grupo
		listado = ldap.memberOf("Docente");
		
		System.out.println(listado);
		
		Profesor docente = new Profesor();
		docente.setUid("jfajardo");
		ldap.obtenerUsuarioLDAPByUID(docente);
				
		// Autentificamos el usuario		
		System.out.println("Es correcto el password:"+ldap.autenticacionLDAP("jfajardo", "1207"));;
		
		// Pertenece a un grupo
		System.out.println("Pertenece al grupo Docente: "+ldap.search("cn=Docente,ou=groups","memberUid="+docente.getUidNumber()));




				
	}


}
