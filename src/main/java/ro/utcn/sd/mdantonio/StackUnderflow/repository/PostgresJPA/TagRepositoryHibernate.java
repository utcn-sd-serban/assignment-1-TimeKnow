package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJPA;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Tag;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.TagRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TagRepositoryHibernate implements TagRepository {
    private final EntityManager entityManager;

    @Override
    public List<Tag> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        query.select(query.from(Tag.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Tag save(Tag element) {
        if(element.getTagid()==null){
            entityManager.persist(element);
            return element;
        }
        return entityManager.merge(element);
    }

    @Override
    public void remove(Tag element) {
        entityManager.remove(element);
    }

    @Override
    public Optional<Tag> findById(int id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }
}
