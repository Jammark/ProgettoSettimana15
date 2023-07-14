package program;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.github.javafaker.Faker;

import lombok.extern.slf4j.Slf4j;
import model.Archivio;
import model.ArticoloCartaceo;
import model.Libro;
import model.Periodicità;
import model.Prestito;
import model.Rivista;
import model.Utente;
import util.JpaUtil;
import util.PrestitoDAO;
import util.UtenteDAO;

@Slf4j
public class Main {

	public static void main(String[] args) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		populateData(em);

	}

	private static void populateData(EntityManager em) {
		Faker faker = new Faker(new Random(24));
		List<Libro> libri = Arrays.asList(new Libro("Musica latina", 100, 1980, faker.name().name(), "musica"),
				new Libro("Musica rock", 150, 1990, faker.name().name(), "musica"),
				new Libro("Torte e dolci", 300, 2005, faker.name().name(), "cucina"),
				new Libro("Pane e biscotti", 250, 2008, faker.name().name(), "cucina"));

		log.info("Serie di libri:");
		libri.forEach(l -> log.info("Lista libri: " + l));

		List<Rivista> riviste = Arrays.asList(new Rivista("Settimana enigmistica", 70, 1995, Periodicità.SETTIMANALE),
				new Rivista("Focus", 200, 2000, Periodicità.MENSILE),
				new Rivista("Monete e banconote", 60, 1997, Periodicità.SEMESTRALE));
		log.info("Serie di riviste:");
		riviste.forEach(r -> log.info("Lista riviste: " + r));


		Archivio archivio = new Archivio(em);
		Predicate<ArticoloCartaceo> p = archivio::presente;
		p = p.negate();
		libri.stream().filter(p).forEach(archivio::aggiungi);
		riviste.stream().filter(p).forEach(archivio::aggiungi);

		Utente u1 = new Utente("Marco", "De Luca", LocalDate.of(1970, 3, 10), 345734);
		Utente u2 = new Utente("Marco", "Di Pasquale", LocalDate.of(1975, 3, 11), 784532);

		UtenteDAO daoU = new UtenteDAO(em);
		Predicate<Utente> pu = daoU::presente;
		Arrays.asList(u1, u2).stream().filter(pu.negate()).forEach(daoU::save);

		PrestitoDAO daoP = new PrestitoDAO(em);

		List<Utente> utenti = em.createQuery("SELECT u FROM Utente u", Utente.class).getResultList();
		if (utenti.size() > 1) {
			List<ArticoloCartaceo> articoli = em.createQuery("SELECT a FROM ArticoloCartaceo a", ArticoloCartaceo.class)
					.getResultList();
			savePrestiti(articoli, daoP, utenti.get(0), utenti.get(1));
		}
		Query query = em.createQuery("SELECT p FROM Prestito p", Prestito.class);
		List<Prestito> prestiti = query.getResultList();
		prestiti.forEach(prestito -> log.info("Prestiti: " + prestito));

		ArticoloCartaceo elemento1 = archivio.findByISBN(libri.get(0).getCodiceISBN().toString());
		boolean risultato = archivio.rimuoviArticolo(elemento1.getCodiceISBN().toString());
		if (risultato) {
			log.info("Articolo rimosso: " + elemento1);
		}

		List<ArticoloCartaceo> byYear = archivio.findByYear(libri.get(1).getAnnoPubblicazione());
		byYear.forEach(e -> log.info("Ricerca secondo anno: " + e));

		List<Libro> byAuthor = archivio.findByAuthor(libri.get(1).getAutore());
		byAuthor.forEach(e -> log.info("Ricerca secondo autore: " + e));

		List<ArticoloCartaceo> byTitle = archivio.findByTitle(libri.get(1).getTitolo().substring(0, 3));
		byTitle.forEach(e -> log.info("Ricerca secondo titolo: " + e));

		List<ArticoloCartaceo> inPrestito = archivio.inprestito(u1.getNumeroDiTessera());
		inPrestito.forEach(e -> log.info("Articoli in prestito: " + e));

		prestiti = query.getResultList();

		prestiti.stream().limit(7).forEach(prestito -> {
			prestito.setDataRestituzioneEffettiva(LocalDate.now().minusDays(100));
			daoP.save(prestito);
		});

		List<Prestito> daRestituire = archivio.getScaduti();
		daRestituire.forEach(prestito -> log.info("Ancora da restituire: " + prestito));

	}

	private static LocalDate getDate() {
		Random r = new Random();
		return LocalDate.of(r.nextInt(1950, 2005), r.nextInt(1, 13), r.nextInt(1, 30));
	}

	private static void savePrestiti(List<? extends ArticoloCartaceo> lista, PrestitoDAO daoP, Utente u1, Utente u2) {
		// Predicate<Prestito> pred = daoP::presente;
		// pred = pred.negate();
		lista.stream().flatMap(
				l -> Arrays.asList(new Prestito(u2, l, getDate(), null), new Prestito(u1, l, getDate(), null)).stream())
				// .filter(pred)
				.forEach(daoP::save);
	}
}
