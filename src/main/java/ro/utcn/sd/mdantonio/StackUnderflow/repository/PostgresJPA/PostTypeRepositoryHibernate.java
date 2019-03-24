package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJPA;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.PostType;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostTypeRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostTypeRepositoryHibernate implements PostTypeRepository {
    private final EntityManager entityManager;

    @Override
    public List<PostType> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostType> query = builder.createQuery(PostType.class);
        query.select(query.from(PostType.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public PostType save(PostType element) {
        if(element.getPosttypeid()==null){
            entityManager.persist(element);
            return element;
        }
        return entityManager.merge(element);
    }

    @Override
    public void remove(PostType element) {
        entityManager.remove(element);
    }

    @Override
    public Optional<PostType> findById(int id) {
        return Optional.ofNullable(entityManager.find(PostType.class, id));
    }
}
