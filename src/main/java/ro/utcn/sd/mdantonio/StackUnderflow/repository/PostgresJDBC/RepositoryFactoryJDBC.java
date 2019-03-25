package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJDBC;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.*;


@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "a1.repository-type", havingValue = "JDBC")
public class RepositoryFactoryJDBC implements RepositoryFactory {
    private final JdbcTemplate template;


    @Override
    public PostRepository createPostRepository() {
        return new PostRepositoryJDBC(template);
    }

    @Override
    public PostTypeRepository createPostTypeRepository() {
        return new PostTypeRepositoryJDBC(template);
    }

    @Override
    public TagRepository createTagRepository() {
        return new TagRepositoryJDBC(template);
    }

    @Override
    public UnderflowUserRepository createUnderflowUserRepository() {
        return new UnderflowUserRepositoryJDBC(template);
    }

    @Override
    public VoteRepository createVoteRepository() {
        return new VoteRepositoryJDBC(template);
    }

    @Override
    public PostTagRepository createPostTagRepository() {
        return new PostTagRepositoryJDBC(template);
    }
}
