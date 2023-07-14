package model;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@ToString
@Getter
@Setter
//@DiscriminatorColumn(name = "tipo_articolo")
@NamedQuery(name = "presente", query = "SELECT a FROM ArticoloCartaceo a WHERE a.titolo = :t")
public abstract class ArticoloCartaceo {



	private String titolo;

	@Id
	@GeneratedValue
	private UUID codiceISBN;
	@Column(name = "numero_pagine")
	private int numeroPagine;
	@Column(name = "anno_pubblicazione")
	private int annoPubblicazione;

	@OneToMany(mappedBy = "elementoPrestato", cascade = CascadeType.ALL)
	private Set<Prestito> prestiti;

	public ArticoloCartaceo(String titolo, int numeroPagine, int annoPubblicazione) {
		this.titolo = titolo;

		this.numeroPagine = numeroPagine;
		this.annoPubblicazione = annoPubblicazione;
	}



	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public UUID getCodiceISBN() {
		return codiceISBN;
	}



	public int getNumeroPagine() {
		return numeroPagine;
	}

	public void setNumeroPagine(int numeroPagine) {
		this.numeroPagine = numeroPagine;
	}

	public int getAnnoPubblicazione() {
		return annoPubblicazione;
	}

	public void setAnnoPubblicazione(int annoPubblicazione) {
		this.annoPubblicazione = annoPubblicazione;
	}


	public void setCodiceISBN(UUID codiceISBN) {
		this.codiceISBN = codiceISBN;
	}

}
