package model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "libri")
//@DiscriminatorValue("Lib")
public class Libro extends ArticoloCartaceo {

	private String autore, genere;

	public Libro(String titolo, int numeroPagine, int annoPubblicazione, String autore,
			String genere) {
		super(titolo, numeroPagine, annoPubblicazione);
		this.autore = autore;
		this.genere = genere;

	}





	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	@Override
	public String toString() {
		return "Libro [autore=" + autore + ", genere=" + genere + ", Titolo=" + getTitolo() + ", CodiceISBN="
				+ getCodiceISBN() + ", NumeroPagine=" + getNumeroPagine() + ", AnnoPubblicazione="
				+ getAnnoPubblicazione() + "]";
	}

}
