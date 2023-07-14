package model;




import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Archivio {

	private EntityManager em;

	public Archivio(EntityManager em) {
		super();
		this.em = em;
	}

	public boolean aggiungi(ArticoloCartaceo a) {

		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.persist(a);
			t.commit();
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			t.rollback();
			return false;
		}
	}

	public boolean rimuoviArticolo(String codice) {
		ArticoloCartaceo a = em.find(ArticoloCartaceo.class, UUID.fromString(codice));

		log.info("Trovato articolo da rimuovere: " + a);
		if (a == null) {
			return false;
		}
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.remove(a);
			t.commit();
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			t.rollback();
			return false;
		}
	}

	public ArticoloCartaceo findByISBN(String codice) {
		return em.find(ArticoloCartaceo.class, UUID.fromString(codice));
	}

	public List<ArticoloCartaceo> findByYear(int anno) {
		TypedQuery<ArticoloCartaceo> q = em.createQuery(
				"SELECT a FROM  ArticoloCartaceo a WHERE a.annoPubblicazione = :anno", ArticoloCartaceo.class);
		q.setParameter("anno", anno);
		return q.getResultList();
	}

	public List<ArticoloCartaceo> findByTitle(String titolo) {
		TypedQuery<ArticoloCartaceo> q = em.createQuery(
				"SELECT a FROM  ArticoloCartaceo a WHERE a.titolo LIKE CONCAT(:param, '%')", ArticoloCartaceo.class);
		q.setParameter("param", titolo);
		return q.getResultList();
	}

	public List<ArticoloCartaceo> inprestito(int numeroTessera) {
		TypedQuery<ArticoloCartaceo> q = em.createQuery(
				"SELECT a FROM  Prestito p JOIN p.elementoPrestato a JOIN p.utente u WHERE u.numeroDiTessera = :param AND p.dataRestituzioneEffettiva IS NULL",
				ArticoloCartaceo.class);
		q.setParameter("param", numeroTessera);
		return q.getResultList();
	}

	public List<Libro> findByAuthor(String autore) {
		TypedQuery<Libro> q = em.createQuery("SELECT a FROM  Libro a WHERE a.autore = :param", Libro.class);
		q.setParameter("param", autore);
		return q.getResultList();
	}

	public boolean presente(ArticoloCartaceo a) {
		TypedQuery<ArticoloCartaceo> q = em.createNamedQuery("presente", ArticoloCartaceo.class);
		q.setParameter("t", a.getTitolo());
		return q.getResultList().size() > 0;
	}

	public List<Prestito> getScaduti() {
		TypedQuery<Prestito> q = em.createQuery(
				"SELECT p FROM Prestito p WHERE p.dataRestituzionePrevista < :param AND p.dataRestituzioneEffettiva IS NULL",
				Prestito.class);
		q.setParameter("param", LocalDate.now());
		return q.getResultList();
	}

}
