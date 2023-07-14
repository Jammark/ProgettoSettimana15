package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import lombok.extern.slf4j.Slf4j;
import model.Utente;

@Slf4j
public class UtenteDAO {
	private final EntityManager em;

	public UtenteDAO(EntityManager em) {
		this.em = em;
	}

	public void save(Utente u) {
		EntityTransaction t = this.em.getTransaction();
		try {
			t.begin();
			em.persist(u);

			t.commit();

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public void delete(Utente u) {
		EntityTransaction t = this.em.getTransaction();
		try {
			t.begin();
			em.remove(u);

			t.commit();

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public Utente getById(Long id) {
		return em.find(Utente.class, id);
	}

	public void refresh(Utente u) {
		em.refresh(u);
	}

	public boolean presente(Utente u) {
		TypedQuery<Utente> q = em.createNamedQuery("presenteUtente", Utente.class);
		if (u.getNome() != null && u.getCognome() != null) {
			q.setParameter("nomeCompleto", u.getNome() + " " + u.getCognome());
		} else {
			q.setParameter("nomeCompleto", "");
		}
		return q.getResultList().size() > 0;
	}

}
