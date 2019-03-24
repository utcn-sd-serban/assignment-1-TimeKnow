package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJPA;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Post;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostRepositoryHibernate implements PostRepository {
    private final EntityManager entityManager;

    @Override
    public List<Post> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> query = builder.createQuery(Post.class);
        query.select(query.from(Post.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Post save(Post element) {
        if(element.getPostid()==null){
            entityManager.persist(element);
            return element;
        }
        return entityManager.merge(element);
    }

    @Override
    public void remove(Post element) {
        entityManager.remove(element);
    }

    @Override
    public Optional<Post> findById(int id) {
        return Optional.ofNullable(entityManager.find(Post.class, id));
    }
}
