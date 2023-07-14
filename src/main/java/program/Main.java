package program;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import javax.persistence.EntityManager;

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

		List<Prestito> prestiti = em.createQuery("SELECT p FROM Prestito p", Prestito.class).getResultList();
		prestiti.forEach(prestito -> log.info("Prestiti: " + prestito));

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
