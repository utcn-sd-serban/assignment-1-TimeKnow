package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJPA;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.*;

import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "a1.repository-type", havingValue = "JPA")
public class RepositoryFactoryHibernate implements RepositoryFactory {
    private final EntityManager entityManager;

    @Override
    public PostRepository createPostRepository() {
        return new PostRepositoryHibernate(entityManager);
    }

    @Override
    public PostTypeRepository createPostTypeRepository() {
        return new PostTypeRepositoryHibernate(entityManager);
    }

    @Override
    public TagRepository createTagRepository() {
        return new TagRepositoryHibernate(entityManager);
    }

    @Override
    public UnderflowUserRepository createUnderflowUserRepository() {
        return new UnderflowUserRepositoryHibernate(entityManager);
    }

    @Override
    public VoteRepository createVoteRepository() {
        return new VoteRepositoryHibernate(entityManager);
    }

    @Override
    public PostTagRepository createPostTagRepository() {
        return new PostTagRepositoryHibernate(entityManager);
    }
}
