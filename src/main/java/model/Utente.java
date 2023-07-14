package model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "utenti")
@NoArgsConstructor
@Getter
@Setter
@ToString
@NamedQuery(name = "presenteUtente", query = "SELECT u FROM Utente u WHERE CONCAT(u.nome , ' ' ,u.cognome) = :nomeCompleto")
public class Utente {

	@Id
	@SequenceGenerator(name = "u_seq", sequenceName = "utenti_sequence", allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "u_seq")
	private Long id;

	private String nome;
	private String cognome;
	@Column(name = "data_di_nascita")
	private LocalDate dataDiNascita;
	@Column(name = "numero_tessera")
	private int numeroDiTessera;

	public Utente(String nome, String cognome, LocalDate dataDiNascita, int numeroDiTessera) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.dataDiNascita = dataDiNascita;
		this.numeroDiTessera = numeroDiTessera;
	}

}
