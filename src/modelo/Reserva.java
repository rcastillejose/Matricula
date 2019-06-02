package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {
	private int idReserva;
	private String email;
	private LocalDate reserva_dia;
	private LocalTime reserva_hora;
	private int idPeriodo;
	
	public Reserva(int idReserva, String email, LocalDate reserva_dia, LocalTime reserva_hora, int idPeriodo) {
		super();
		this.idReserva = idReserva;
		this.email = email;
		this.reserva_dia = reserva_dia;
		this.reserva_hora = reserva_hora;
		this.idPeriodo = idPeriodo;
	}

	public int getIdReserva() {
		return idReserva;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getReserva_dia() {
		return reserva_dia;
	}

	public void setReserva_dia(LocalDate reserva_dia) {
		this.reserva_dia = reserva_dia;
	}

	public LocalTime getReserva_hora() {
		return reserva_hora;
	}

	public void setReserva_hora(LocalTime reserva_hora) {
		this.reserva_hora = reserva_hora;
	}

	public int getIdPeriodo() {
		return idPeriodo;
	}
	
}
