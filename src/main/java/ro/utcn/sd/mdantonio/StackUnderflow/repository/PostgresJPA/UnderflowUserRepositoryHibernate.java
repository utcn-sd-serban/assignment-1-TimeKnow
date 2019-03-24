package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJPA;

import lombok.RequiredArgsConstructor;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.UnderflowUserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UnderflowUserRepositoryHibernate implements UnderflowUserRepository {
    private final EntityManager entityManager;


    @Override
    public Optional<UnderflowUser> findByUsername(String username) {
        //use actual Java Class Name not Database Name
        TypedQuery<UnderflowUser> query = entityManager.createQuery(
                "SELECT u FROM UnderflowUser u WHERE u.username = :username", UnderflowUser.class);

        //This implementation was forced onto me
        try {
            return Optional.ofNullable(query.setParameter("username", username).getSingleResult());
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public List<UnderflowUser> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UnderflowUser> query = builder.createQuery(UnderflowUser.class);
        query.select(query.from(UnderflowUser.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public UnderflowUser save(UnderflowUser element) {
        if(element.getUserid()==null){
            entityManager.persist(element);
            return element;
        }
        return entityManager.merge(element);
    }

    @Override
    public void remove(UnderflowUser element) {
            entityManager.remove(element);
    }

    @Override
    public Optional<UnderflowUser> findById(int id) {
        return Optional.ofNullable(entityManager.find(UnderflowUser.class, id));
    }
}
