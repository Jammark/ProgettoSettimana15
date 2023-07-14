package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import lombok.extern.slf4j.Slf4j;
import model.Prestito;


@Slf4j
public class PrestitoDAO {
	private final EntityManager em;

	public PrestitoDAO(EntityManager em) {
		this.em = em;
	}

	public void save(Prestito p) {
		EntityTransaction t = this.em.getTransaction();
		try {
			t.begin();
			p.setDataRestituzionePrevista(p.getDataInizioPrestito().plusDays(30));
			em.persist(p);

			t.commit();

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public void delete(Prestito p) {
		EntityTransaction t = this.em.getTransaction();
		try {
			t.begin();
			em.remove(p);

			t.commit();

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public Prestito getById(Long id) {
		return em.find(Prestito.class, id);
	}
	/*
	 * public boolean presente(Prestito p) { TypedQuery<Prestito> q =
	 * em.createNamedQuery("prestitoPresente", Prestito.class); if (p.getUtente() !=
	 * null && p.getElementoPrestato() != null) { q.setParameter("utenteId",
	 * p.getUtente().getId()); q.setParameter("articoloId",
	 * p.getElementoPrestato().getCodiceISBN()); } else { return false; } return
	 * q.getResultList().size() > 0; }
	 * 
	 * public void refresh(Prestito p) { em.refresh(p); }
	 */
}
