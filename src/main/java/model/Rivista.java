package model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "riviste")
//@DiscriminatorValue("Riv")
public class Rivista extends ArticoloCartaceo {

	@Enumerated(EnumType.STRING)
	private Periodicità periodicità;

	public Rivista(String titolo, int numeroPagine, int annoPubblicazione,
			Periodicità periodicità) {
		super(titolo, numeroPagine, annoPubblicazione);
		this.periodicità = periodicità;
	}





	public Periodicità getPeriodicità() {
		return periodicità;
	}

	public void setPeriodicità(Periodicità periodicità) {
		this.periodicità = periodicità;
	}

	@Override
	public String toString() {
		return "Rivista [periodicità=" + periodicità + ", Titolo=" + getTitolo() + ", CodiceISBN=" + getCodiceISBN()
				+ ", NumeroPagine=" + getNumeroPagine() + ", AnnoPubblicazione="
				+ getAnnoPubblicazione() + "]";
	}


}
