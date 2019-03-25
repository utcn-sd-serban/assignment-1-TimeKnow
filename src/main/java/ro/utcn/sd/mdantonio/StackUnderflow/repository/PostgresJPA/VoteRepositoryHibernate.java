package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJPA;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Vote;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.VoteRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class VoteRepositoryHibernate implements VoteRepository {
    private final EntityManager entityManager;

    @Override
    public List<Vote> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vote> query = builder.createQuery(Vote.class);
        query.select(query.from(Vote.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Vote save(Vote element) {
        if(element.getVoteid()==null){
            entityManager.persist(element);
            return element;
        }
        return entityManager.merge(element);
    }

    @Override
    public void remove(Vote element) {
        entityManager.remove(element);
    }

    @Override
    public Optional<Vote> findById(int id) {
        return Optional.ofNullable(entityManager.find(Vote.class, id));
    }
}
