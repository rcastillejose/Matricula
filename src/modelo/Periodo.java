package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Periodo {
	private int idPeriodo;
	private LocalDate dia_inicio;
	private LocalDate dia_fin;
	private LocalTime hora_inicio;
	private LocalTime hora_fin;
	private LocalTime intervalo;
	private boolean habilitado;
	private int cursoYear;
	
	public Periodo(int idPeriodo, LocalDate dia_inicio, LocalDate dia_fin, LocalTime hora_inicio, LocalTime hora_fin,
			LocalTime intervalo, boolean habilitado, int cursoYear) {
		super();
		this.idPeriodo = idPeriodo;
		this.dia_inicio = dia_inicio;
		this.dia_fin = dia_fin;
		this.hora_inicio = hora_inicio;
		this.hora_fin = hora_fin;
		this.intervalo = intervalo;
		this.habilitado = habilitado;
		this.cursoYear = cursoYear;
	}

	public LocalDate getDia_inicio() {
		return dia_inicio;
	}

	public void setDia_inicio(LocalDate dia_inicio) {
		this.dia_inicio = dia_inicio;
	}

	public LocalDate getDia_fin() {
		return dia_fin;
	}

	public void setDia_fin(LocalDate dia_fin) {
		this.dia_fin = dia_fin;
	}

	public LocalTime getHora_inicio() {
		return hora_inicio;
	}

	public void setHora_inicio(LocalTime hora_inicio) {
		this.hora_inicio = hora_inicio;
	}

	public LocalTime getHora_fin() {
		return hora_fin;
	}

	public void setHora_fin(LocalTime hora_fin) {
		this.hora_fin = hora_fin;
	}

	public LocalTime getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(LocalTime intervalo) {
		this.intervalo = intervalo;
	}

	public boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public int getCursoYear() {
		return cursoYear;
	}

	public void setCursoYear(int cursoYear) {
		this.cursoYear = cursoYear;
	}

	public int getIdPeriodo() {
		return idPeriodo;
	}
	
	
}
