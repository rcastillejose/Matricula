package configuracion;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import modelo.Alumno;
import modelo.Persona;
import modelo.Profesor;
import configuracion.ConfiguracionSegura;


public class LDAP {

	Hashtable<String, String> env = new Hashtable<String, String>();
	ConfiguracionSegura conf;

	public LDAP() {

		conf = new ConfiguracionSegura();

		env.put(DirContext.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(DirContext.PROVIDER_URL, conf.getServername() + "/" + conf.getShema_base());

		env.put(DirContext.SECURITY_AUTHENTICATION, "simple");
		env.put(DirContext.SECURITY_PRINCIPAL, conf.getLdapUsername());
		env.put(DirContext.SECURITY_CREDENTIALS, conf.getLdapPassword());
		
	}

	// Metodo que devuelve true o false si la busqueda ofrece algun resultado
	public boolean search(String searchBase, String searchFilter) {

		boolean resultado = false;

		DirContext context = null;
		try {

			context = new InitialDirContext(env);

			SearchControls ctrl = new SearchControls();
			ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);

			NamingEnumeration<?> enumeration = context.search(searchBase, searchFilter, ctrl);

			while (enumeration.hasMore()) {

				resultado = true;

				SearchResult result = (SearchResult) enumeration.next();

				Attributes attribs = result.getAttributes();
				// System.out.println("Attributes:" + attribs);
				NamingEnumeration<String> attribsIDs = attribs.getIDs();
				// System.out.println(attribsIDs);
				// loop on attributes
				// StringBuffer output = new StringBuffer();
				// int iAttr = 0;
				// String value;
				while (attribsIDs.hasMore()) {

					attribsIDs.next();
					// String attrID = attribsIDs.next();
					// System.out.println(attrID);
					// value = attribs.get(attrID).toString();
					// System.out.println("AttributeId:" + iAttr + " " + attrID
					// + " -> :"+ value.substring(value.indexOf(":") + 2));
					// NamingEnumeration values = ((BasicAttribute)
					// attribs.get(attrID)).getAll();
					// System.out.println("Naming Enumertaion Values" + values);
					// iAttr++;
				}
			}
		} catch (NameNotFoundException nnfe) {
			// nnfe.printStackTrace();
			resultado = false;
		} catch (NamingException ne) {
			ne.printStackTrace();
		} finally {
			if (context != null)
				try {
					context.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
		}
		return resultado;
	}

	/**
	 * Metodo utilizado para comprobar la identidad de un usuario
	 * 
	 * @param usuario
	 *            login del usuario a validar
	 * @param password
	 *            password del usuario
	 * @return Devuelve si es correcto el password
	 */
	public boolean autenticacionLDAP(String usuario, String password) {
		Hashtable<String, String> auth = new Hashtable<String, String>(11);
		String base = "ou=Users," + conf.getShema_base();
		String dn = "uid=" + usuario + "," + base;
		String ldapURL = conf.getServername();

		auth.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		auth.put(Context.PROVIDER_URL, ldapURL);
		auth.put(Context.SECURITY_AUTHENTICATION, "simple");
		auth.put(Context.SECURITY_PRINCIPAL, dn);
		auth.put(Context.SECURITY_CREDENTIALS, password);

		try {
			new InitialDirContext(auth);
			System.out.println("La autentificacion de " + usuario + " se realizo correctamente ante LDAP!");
			return true;
		} catch (AuthenticationException authEx) {
			System.out.println("No se ha realizado correctamente la autentificacion de " + usuario + " !");
			return false;

		} catch (NamingException namEx) {

			System.out.println("SUCEDIO ALGO!");
			System.out.println(base);
			System.out.println(dn);
			System.out.println(ldapURL);
			namEx.printStackTrace();
			return false;
		}

	}

	/**
	 * Metodo para obtener la foto de un usuario
	 * 
	 * @param uid
	 *            UID del usuario
	 * @return Foto del usuario como un vector de bytes
	 */

	public byte[] obtenerFoto(String uid) {
		byte[] foto = null;
		String searchFilter = "uid=" + uid;
		String searchBase = "ou=Users";
		DirContext context = null;
		
		try {

			context = new InitialDirContext(env);

			SearchControls ctrl = new SearchControls();
			ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);

			NamingEnumeration<?> enumeration = context.search(searchBase, searchFilter, ctrl);

			SearchResult result = (SearchResult) enumeration.next();

			Attributes attribs = result.getAttributes();

			foto = (byte[]) attribs.get("jpegPhoto").get();

		} catch (NameNotFoundException nnfe) {
			// nnfe.printStackTrace();
			nnfe.printStackTrace();
		} catch (NamingException ne) {
			ne.printStackTrace();
		} finally {
			if (context != null)
				try {
					context.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
		}
		return foto;
	}

	/**
	 * Metodo que devuelve la primera coincidencia de la busqueda de un valor
	 * 
	 * @param searchBase
	 *            Base de busqueda por ejemplo "ou=Users"
	 * @param searchFilter
	 *            Filtro de busqueda, por ejemplo "uid=jalonso"
	 * @param attributeID
	 *            Atributo del que se desea obtener su valor, por ejemplo "cn"
	 * @return Valor almacenado en LDAP en el atributo para la busqueda
	 *         realizada
	 */
	public String searchAttribute(String searchBase, String searchFilter, String attributeID) {

		DirContext context = null;
		String value = null;
		try {

			context = new InitialDirContext(env);

			SearchControls ctrl = new SearchControls();
			ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);

			NamingEnumeration<?> enumeration = context.search(searchBase, searchFilter, ctrl);

			SearchResult result = (SearchResult) enumeration.next();

			Attributes attribs = result.getAttributes();

			value = attribs.get(attributeID).toString();
			value = value.substring(value.indexOf(":") + 2);

		} catch (NameNotFoundException nnfe) {
			// nnfe.printStackTrace();
			nnfe.printStackTrace();
		} catch (NamingException ne) {
			ne.printStackTrace();
		} finally {
			if (context != null)
				try {
					context.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
		}
		return value;
	}

	/**
	 * Metodo que carga los datos de LDAP en persona en funcion de UID
	 * 
	 * @param persona
	 *            Persona donde se añadiran los valores alamacenados segun su
	 *            UID
	 * @return True si existe la persona en LDAP.
	 */
	public boolean obtenerUsuarioLDAPByUID(Persona persona) {

		boolean existe = false;
		String searchFilter = null;

		DirContext context = null;
		try {

			context = new InitialDirContext(env);

			SearchControls ctrl = new SearchControls();
			ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);

			searchFilter = "uid=" + persona.getUid();

			SearchResult result = null;
			NamingEnumeration<?> enumeration = context.search("ou=Users", searchFilter, ctrl);

			if (enumeration.hasMore()) {
				existe = true;
				result = (SearchResult) enumeration.next();
				Attributes attribs = result.getAttributes();
				rellenarDatosUsuario(persona, attribs);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (context != null)
				try {
					context.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
		}
		return existe;
	}

	/**
	 * Metodo que carga los datos de LDAP en persona en funcion de su DNI o NIA
	 * 
	 * @param persona
	 *            Persona donde se añadiran los valores alamacenados segun su
	 *            NIA(Alumno) o DNI(Profesor)
	 * @return True si existe la persona en LDAP.
	 */
	public boolean obtenerUsuarioLDAP(Persona persona) {

		boolean existe = false;
		String searchFilter = null;

		DirContext context = null;
		try {

			context = new InitialDirContext(env);

			SearchControls ctrl = new SearchControls();
			ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);

			if (persona instanceof Profesor) {
				searchFilter = "documento=" + persona.obtenerIdentificacion();
			} else {
				searchFilter = "NIA=" + persona.obtenerIdentificacion();
			}

			SearchResult result = null;
			NamingEnumeration<?> enumeration = context.search("ou=Users", searchFilter, ctrl);

			if (enumeration.hasMore()) {
				existe = true;
				result = (SearchResult) enumeration.next();
				Attributes attribs = result.getAttributes();
				rellenarDatosUsuario(persona, attribs);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (context != null)
				try {
					context.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
		}
		return existe;
	}

	// Metodo que carga los datos en un objeto persona con los attributos
	// pasados por parametro
	private void rellenarDatosUsuario(Persona persona, Attributes attribs) {
		NamingEnumeration<String> attribsIDs = null;
		attribsIDs = attribs.getIDs();
		String value;
		try {
			value = attribs.get("cn").toString();
			persona.setNombre(value.substring(value.indexOf(":") + 2));
		} catch (Exception e) {
		}

		try {
			value = attribs.get("sn").toString();
			persona.setApellidos(value.substring(value.indexOf(":") + 2));
		} catch (Exception e) {
		}
		try {
			value = attribs.get("mail").toString();
			persona.setMail(value.substring(value.indexOf(":") + 2));
		} catch (Exception e) {
		}
		try {
			value = attribs.get("uidNumber").toString();
			persona.setUidNumber(Integer.parseInt(value.substring(value.indexOf(":") + 2)));
		} catch (Exception e) {
		}
		try {
			value = attribs.get("uid").toString();
			persona.setUid(value.substring(value.indexOf(":") + 2));
		} catch (Exception e) {
		}
		try {
			value = attribs.get("gidNumber").toString();
			persona.setGidNumber(Integer.parseInt(value.substring(value.indexOf(":") + 2)));
		} catch (Exception e) {
		}
		try {
			value = attribs.get("documento").toString();
			persona.setDocumento(value.substring(value.indexOf(":") + 2));
		} catch (Exception e) {
		}
		try {
			value = attribs.get("documento").toString();
			persona.setDocumento(value.substring(value.indexOf(":") + 2));
		} catch (Exception e) {
		}
		try {
			value = attribs.get("fechaNacimiento").toString();
			persona.setFechaNacimiento(value.substring(value.indexOf(":") + 2));
		} catch (Exception e) {
		}
		try {
			Attribute at = attribs.get("jpegPhoto");
			byte[] b = (byte[]) at.get();

			persona.setFoto(b);

		} catch (Exception e) {
		}

		if (persona instanceof Profesor)
			rellenarDatosProfesor(persona, attribs);
		

		if (attribsIDs != null) {
			try {
				attribsIDs.close();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

	}

	// Metodo auxiliar de rellenarDatosUsuario
	private void rellenarDatosProfesor(Persona persona, Attributes attribs) {

		String value = null;
		Profesor p = (Profesor) persona;

		try {
			value = attribs.get("tutor").toString();
			p.setTutor(value.substring(value.indexOf(":") + 2));
		} catch (Exception e) {
		}
		try {
			value = attribs.get("telephoneNumber").toString();
			p.setTelefono1(value.substring(value.indexOf(":") + 2));
		} catch (Exception e) {
		}
		try {
			value = attribs.get("mobile").toString();
			p.setTelefono2(value.substring(value.indexOf(":") + 2));
		} catch (Exception e) {
		}
		try {
			value = attribs.get("cargo").toString();
			p.setCargo(value.substring(value.indexOf(":") + 2));
		} catch (Exception e) {
		}

		p.setPassword(p.getDocumento());
	}


	/**
	 * Comprueba si existe un login
	 * @param username	Login a comprobar
	 * @return			Devuelve un true si ese UID ya existe en LDAP.
	 * @throws NamingException
	 */
	public boolean existeLogin(String username) throws NamingException {

		String searchBase = "ou=Users";
		String searchFilter = "uid=" + username;

		DirContext context = new InitialDirContext(env);

		SearchControls ctrl = new SearchControls();
		ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<?> enumeration = context.search(searchBase, searchFilter, ctrl);

		if (enumeration.hasMore()) {
			enumeration.close();
			context.close();
			return true;
		} else {
			enumeration.close();
			context.close();
			return false;
		}
	}

	/**
	 * Obtenemos un ArrayList de personas que pertenecen a un grupo
	 * @param grupo		Grupo del que se desea obtener los diferentes usuarios, por ejemplo "Alumnos"
	 * @return			ArrayList de personas que pertenecen a un grupo.
	 */
	public ArrayList<Persona> memberOf(String grupo) {

		String searchbase = "ou=groups";
		String searchfilter = "cn=" + grupo;

		ArrayList<Persona> members = new ArrayList<Persona>();
		Persona p = null;
		String uid;
		String uidNumber;

		// the following is helpful in debugging errors //
		// env.put("com.sun.jndi.ldap.trace.ber", System.err);

		LdapContext ctx = null;
		try {
			ctx = new InitialLdapContext(env, null);

			NamingEnumeration<?> results = null;
			try {
				SearchControls controls = new SearchControls();
				controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
				results = ctx.search(searchbase, searchfilter, controls);

				while (results.hasMore()) {

					SearchResult searchResult = (SearchResult) results.next();
					Attributes attributes = searchResult.getAttributes();
					Attribute attr = attributes.get("memberUid");

				
					for (int i = 0; i < attr.size(); i++) {
						
						uidNumber = (String) attr.get(i);
						
						
						uid=searchAttribute("ou=users", "uidNumber="+uidNumber, "uid");

						if (search("uid=" + uid + ",ou=users", "objectClass=profesor")) {
							p = new Profesor();
							p.setUid(uid);
							obtenerUsuarioLDAPByUID(p);

						} 
						members.add(p);
					}
				}
			} catch (NameNotFoundException e) {
				System.out.println("El contexto base no existe.");
				e.printStackTrace();
			} catch (NamingException e) {
				throw new RuntimeException(e);
			} finally {
				if (results != null) {
					try {
						results.close();
					} catch (Exception e) {
					}
				}
				if (ctx != null) {
					try {
						ctx.close();
					} catch (Exception e) { // Never mind this.
					}
				}
			}
		} catch (

		NamingException e1) {

			e1.printStackTrace();
		}
		return members;
	}

}