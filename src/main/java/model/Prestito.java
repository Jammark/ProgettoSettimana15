package model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "prestiti")
@NoArgsConstructor
@ToString
@Getter
@Setter
//@NamedQuery(name = "prestitoPresente", query = "SELECT p FROM Prestito p WHERE p.utente.id = :utenteId AND p.elementoPrestato.codiceISBN = :articoloId")
public class Prestito {

	@Id
	@SequenceGenerator(name = "p_seq", sequenceName = "prestiti_sequence", allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "p_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_utente")
	private Utente utente;

	@ManyToOne
	@JoinColumn(name = "id_articolo")
	private ArticoloCartaceo elementoPrestato;

	@Column(name = "data_inizio_prestito")
	private LocalDate dataInizioPrestito;


	@Column(name = "data_restituzione_prev")
	private LocalDate dataRestituzionePrevista;

	@Column(name = "data_restituzione")
	private LocalDate dataRestituzioneEffettiva;

	public Prestito(Utente utente, ArticoloCartaceo elementoPrestato, LocalDate dataInizioPrestito,
			LocalDate dataRestituzioneEffettiva) {
		super();
		this.utente = utente;
		this.elementoPrestato = elementoPrestato;
		this.dataInizioPrestito = dataInizioPrestito;
		this.dataRestituzioneEffettiva = dataRestituzioneEffettiva;
	}

}
