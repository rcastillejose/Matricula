package configuracion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;


public class ConfiguracionSegura {
	// Fichero en el que se encuentran las propiedades
	private final String FICHERO = "default.config.properties";

	private LinkedProperties prop;
	private String key = "ieslavereda.es";
	Map<String, String> propiedadesSeguras;

	// Propiedades para la BD
	private String host;
	private String port;
	private String user;
	private String password;
	private String database;

	// Propiedades para LDAP
	private String ldapUsername;
	private String ldapPassword;
	private String servername;
	private String shema_base;
	
	// Propiedades para Gmail
	private String applicationName;
	private String nameFrom;
	private String mailFrom;
	private String mailSmtpReply;
	private String mailAccessToken;
	private String mailClientId;
	private String mailClientSecret;
	private String mailRefreshToken;
	

	/**
	 * Genera un objeto para el almacenaje de propiedades, utilizando el fichero
	 * default.config.properties
	 */
	public ConfiguracionSegura() {

		propiedadesSeguras = new HashMap<String, String>();
		propiedadesSeguras.put("password", "is.password.encrypted");
		propiedadesSeguras.put("ldapPassword", "is.ldapPassword.encrypted");
		propiedadesSeguras.put("mailAccessToken","is.mailAccessToken.encrypted");
		propiedadesSeguras.put("mailClientId", "is.mailClientId.encrypted");
		propiedadesSeguras.put("mailClientSecret", "is.mailClientSecret.encrypted");
		propiedadesSeguras.put("mailRefreshToken", "is.mailRefreshToken.encrypted");

		prop = new LinkedProperties();
		OutputStream output = null;

		File fc = new File(FICHERO);

		if (!fc.isFile()) {
			try {

				output = new FileOutputStream(FICHERO);

				// Propiedades de la conexion a la Base de datos
				prop.setProperty("host", "127.0.0.1");
				prop.setProperty("port", "3306");
				prop.setProperty("database", "database");
				prop.setProperty("user", "user");
				prop.setProperty("password", "password");
				prop.setProperty("is.password.encripted", "false");

				// Propiedades para LDAP
				prop.setProperty("ldapUsername", "cn=admin,dc=ieslavereda,dc=local");
				prop.setProperty("ldapPassword", "passwordForLDAP");
				prop.setProperty("is.ldapPassword.encrypted", "false");
				prop.setProperty("servername", "ldap://10.0.0.243:389");
				prop.setProperty("shema_base", "dc=ieslavereda,dc=local");

				// Propiedades para Gmail
				prop.setProperty("applicationName", "applicationName");
				prop.setProperty("nameFrom", "nameFrom");
				prop.setProperty("mail.from", "mail.from");
				prop.setProperty("mail.smtp.reply", "mail.smtp.reply");
				prop.setProperty("mailClientId", "mailClientId");
				prop.setProperty("mailClientSecret", "mailClientSecret");
				prop.setProperty("mailRefreshToken", "mailRefreshToken");
				
				
				// save properties to project root folder
				prop.store(output, null);

			} catch (IOException io) {
				io.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		// Cargamos las propiedades
		cargar();
	}

	/**
	 * Guarda la configuracion almacenada en el fichero
	 * default.config.properties
	 * 
	 * @return True si se ha podido almacenar.
	 */
	public boolean guardar() {
		boolean guardado = false;

		OutputStream output = null;

		if (new File(FICHERO).exists()) {
			try {

				output = new FileOutputStream(FICHERO);
				prop = new LinkedProperties();

				// Propiedades para la BD
				prop.setProperty("host", host);
				prop.setProperty("port", port);
				prop.setProperty("database", database);
				prop.setProperty("user", user);
				prop.setProperty("password", encrypt(password));
				prop.setProperty("is.password.encripted", "false");

				// Propiedades para LDAP
				prop.setProperty("ldapUsername", ldapUsername);
				prop.setProperty("ldapPassword", encrypt(ldapPassword));
				prop.setProperty("is.ldapPassword.encrypted", "false");
				prop.setProperty("servername", servername);
				prop.setProperty("shema_base", shema_base);

				// Propiedades para Gmail
				prop.setProperty("applicationName", applicationName);
				prop.setProperty("nameFrom", nameFrom);
				prop.setProperty("mail.from", mailFrom);
				prop.setProperty("mail.smtp.reply", mailSmtpReply);
				prop.setProperty("mailAccessToken", encrypt(mailAccessToken));
				prop.setProperty("is.mailAccessToken.encrypted", "true");
				prop.setProperty("mailClientId", encrypt(mailClientId));
				prop.setProperty("is.mailClientId.encrypted", "true");
				prop.setProperty("mailClientSecret", encrypt(mailClientSecret));
				prop.setProperty("is.mailClientSecret.encrypted", "true");
				prop.setProperty("mailRefreshToken", encrypt(mailRefreshToken));
				prop.setProperty("is.mailRefreshToken.encrypted", "true");
				
				// save properties to project root folder
				prop.store(output, null);

				// Encriptamos passwords
				for (String property : propiedadesSeguras.keySet())
					encryptPropertyValue(property, propiedadesSeguras.get(property));

				guardado = true;

				return guardado;

			} catch (IOException io) {
				io.printStackTrace();
				return guardado;
			} catch (ConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
		return guardado;
	}

	private void cargar() {

		try {
			prop = new LinkedProperties();
			InputStream input = null;

			// Encriptamos las claves si fuera necesario antes de leer las
			// propiedades
			for (String property : propiedadesSeguras.keySet())
				encryptPropertyValue(property, propiedadesSeguras.get(property));

			// Cargar archivo propiedades
			input = new FileInputStream(FICHERO);
			prop.load(input);

			// Obtener propiedades de la base de datos
			this.host = prop.getProperty("host");
			this.port = prop.getProperty("port");
			this.user = prop.getProperty("user");

			this.password = decryptPropertyValue("password");

			this.database = prop.getProperty("database");

			// Obtener propiedades de LDAP
			this.ldapUsername = prop.getProperty("ldapUsername");
			this.ldapPassword = decryptPropertyValue("ldapPassword");
			this.servername = prop.getProperty("servername");
			this.shema_base = prop.getProperty("shema_base");

			// Obtener propiedades de Gmail
			this.applicationName = prop.getProperty("applicationName");
			this.nameFrom = prop.getProperty("nameFrom");
			this.mailFrom = prop.getProperty("mail.from");
			this.mailSmtpReply = prop.getProperty("mail.smtp.reply");
			this.mailAccessToken = decryptPropertyValue("mailAccessToken");
			this.mailClientId = decryptPropertyValue("mailClientId");
			this.mailClientSecret = decryptPropertyValue("mailClientSecret");
			this.mailRefreshToken = decryptPropertyValue("mailRefreshToken");
			
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void encryptPropertyValue(String propertyKey, String isPropertyKeyEncrypted) throws ConfigurationException {

		// Apache Commons Configuration
		PropertiesConfiguration prop = new PropertiesConfiguration(FICHERO);

		// Retrieve boolean properties value to see if password is already
		// encrypted or not
		String isEncrypted = prop.getString(isPropertyKeyEncrypted);

		// Check if password is encrypted?
		if (isEncrypted.equals("false")) {
			String tmpPwd = prop.getString(propertyKey);
			String encryptedPassword = encrypt(tmpPwd);

			// Overwrite password with encrypted password in the properties file
			// using Apache Commons Cinfiguration library
			prop.setProperty(propertyKey, encryptedPassword);
			// Set the boolean flag to true to indicate future encryption
			// operation that password is already encrypted
			prop.setProperty(isPropertyKeyEncrypted, "true");
			// Save the properties file
			prop.save(FICHERO);
		}
	}

	private String encrypt(String tmpPwd) {
		// Encrypt
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(key);
		String encryptedPassword = encryptor.encrypt(tmpPwd);
		return encryptedPassword;
	}

	private String decryptPropertyValue(String propertyKey) throws ConfigurationException {
		// System.out.println("Starting decryption");
		PropertiesConfiguration prop = new PropertiesConfiguration(FICHERO);
		String encryptedPropertyValue = prop.getString(propertyKey);

		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(key);
		String decryptedPropertyValue = encryptor.decrypt(encryptedPropertyValue);

		return decryptedPropertyValue;
	}

	/**
	 * @return the prop
	 */
	public Properties getProp() {
		return prop;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param database
	 *            the database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * @return the ldapUsername
	 */
	public String getLdapUsername() {
		return ldapUsername;
	}

	/**
	 * @return the ldapPassword
	 */
	public String getLdapPassword() {
		return ldapPassword;
	}

	/**
	 * @return the servername
	 */
	public String getServername() {
		return servername;
	}

	/**
	 * @return the shema_base
	 */
	public String getShema_base() {
		return shema_base;
	}

	/**
	 * @param ldapUsername
	 *            the ldapUsername to set
	 */
	public void setLdapUsername(String ldapUsername) {
		this.ldapUsername = ldapUsername;
	}

	/**
	 * @param ldapPassword
	 *            the ldapPassword to set
	 */
	public void setLdapPassword(String ldapPassword) {
		this.ldapPassword = ldapPassword;
	}

	/**
	 * @param servername
	 *            the servername to set
	 */
	public void setServername(String servername) {
		this.servername = servername;
	}

	/**
	 * @param shema_base
	 *            the shema_base to set
	 */
	public void setShema_base(String shema_base) {
		this.shema_base = shema_base;
	}

	/**
	 * @return el parámetro applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName Pasamos el parametro applicationName para cambiarlo
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return el parámetro nameFrom
	 */
	public String getNameFrom() {
		return nameFrom;
	}

	/**
	 * @param nameFrom Pasamos el parametro nameFrom para cambiarlo
	 */
	public void setNameFrom(String nameFrom) {
		this.nameFrom = nameFrom;
	}

	/**
	 * @return el parámetro mailFrom
	 */
	public String getMailFrom() {
		return mailFrom;
	}

	/**
	 * @param mailFrom Pasamos el parametro mailFrom para cambiarlo
	 */
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	/**
	 * @return el parámetro mailSmtpReply
	 */
	public String getMailSmtpReply() {
		return mailSmtpReply;
	}

	/**
	 * @param mailSmtpReply Pasamos el parametro mailSmtpReply para cambiarlo
	 */
	public void setMailSmtpReply(String mailSmtpReply) {
		this.mailSmtpReply = mailSmtpReply;
	}

	/**
	 * @return el parámetro mailAccessToken
	 */
	public String getMailAccessToken() {
		return mailAccessToken;
	}

	/**
	 * @param mailAccessToken Pasamos el parametro mailAccessToken para cambiarlo
	 */
	public void setMailAccessToken(String mailAccessToken) {
		this.mailAccessToken = mailAccessToken;
	}

	/**
	 * @return el parámetro mailClientId
	 */
	public String getMailClientId() {
		return mailClientId;
	}

	/**
	 * @param mailClientId Pasamos el parametro mailClientId para cambiarlo
	 */
	public void setMailClientId(String mailClientId) {
		this.mailClientId = mailClientId;
	}

	/**
	 * @return el parámetro mailClientSecret
	 */
	public String getMailClientSecret() {
		return mailClientSecret;
	}

	/**
	 * @param mailClientSecret Pasamos el parametro mailClientSecret para cambiarlo
	 */
	public void setMailClientSecret(String mailClientSecret) {
		this.mailClientSecret = mailClientSecret;
	}

	/**
	 * @return el parámetro mailRefreshToken
	 */
	public String getMailRefreshToken() {
		return mailRefreshToken;
	}

	/**
	 * @param mailRefreshToken Pasamos el parametro mailRefreshToken para cambiarlo
	 */
	public void setMailRefreshToken(String mailRefreshToken) {
		this.mailRefreshToken = mailRefreshToken;
	}
	
	
}
