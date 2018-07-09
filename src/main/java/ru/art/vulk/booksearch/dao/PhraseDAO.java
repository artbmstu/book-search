package ru.art.vulk.booksearch.dao;

import org.springframework.stereotype.Repository;
import ru.art.vulk.booksearch.praseEntity.PhraseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class PhraseDAO implements IPhraseDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<PhraseEntity> getAllPhrases() {
        String hql = "from BlogEntity as be ORDER BY be.id";
        return (List<PhraseEntity>) entityManager.createQuery(hql).getResultList();
    }

    @Override
    public void addPhrase(List<PhraseEntity> phrase) {
        entityManager.persist(phrase);
    }
}
