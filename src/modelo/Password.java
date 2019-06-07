package modelo;

public class Password {
	
	public static final String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
	public static final String NUMEROS = "0123456789";
	public static final String ESPECIALES = "<>_?¿;:$%&*+";
	
//	Password password3 = new Password(Password.MAYUSCULAS+Password.MINUSCULAS+Password.ESPECIALES+Password.NUMEROS,12);
//	System.out.println(password3.getPasswd());
	
	
	private String passwd;
	
	public Password(String passwd) {
		this.passwd=passwd;
	}
	
	public Password(String cadena,int cantidad) {
		passwd="";
		for(int i=0;i<cantidad;i++) {
			passwd += cadena.charAt((int)(Math.random()*cadena.length()));
		}
	}
	
	public String getPasswd() {
		return passwd;
	}
	
	public static String generarPin() {
		return generarPassWord(NUMEROS,4);
	}
	
	public static String generarPassWord(String cadena, int cantidad) {
		String passwd="";
		for(int i = 0; i < cantidad ; i++) {
			passwd += cadena.charAt((int)(Math.random()*cadena.length()));
		}
		return passwd;
	}
	
	public boolean comprobarPassword(String passwd) {
		if(this.passwd.equals(passwd))return true; else return false;
	}
	
}
