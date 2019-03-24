package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJPA;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.PostTag;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostTagRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostTagRepositoryHibernate implements PostTagRepository {
    private final EntityManager entityManager;

    @Override
    public List<PostTag> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostTag> query = builder.createQuery(PostTag.class);
        query.select(query.from(PostTag.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public PostTag save(PostTag element) {
        if(element.getPosttagid()==null){
            entityManager.persist(element);
            return element;
        }
        return entityManager.merge(element);
    }

    @Override
    public void remove(PostTag element) {
        entityManager.remove(element);
    }

    @Override
    public Optional<PostTag> findById(int id) {
        return Optional.ofNullable(entityManager.find(PostTag.class, id));
    }
}
