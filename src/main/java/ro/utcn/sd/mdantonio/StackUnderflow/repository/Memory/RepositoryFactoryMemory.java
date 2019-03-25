package ro.utcn.sd.mdantonio.StackUnderflow.repository.Memory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.*;

@Component
@ConditionalOnProperty(name = "a1.repository-type", havingValue = "MEMORY")
public class RepositoryFactoryMemory implements RepositoryFactory {
    private final PostRepositoryMemory postRepositoryMemory = new PostRepositoryMemory();
    private final PostTypeRepositoryMemory postTypeRepositoryMemory = new PostTypeRepositoryMemory();
    private final TagRepositoryMemory tagRepositoryMemory = new TagRepositoryMemory();
    private final UnderflowUserRepositoryMemory underflowUserRepositoryMemory = new UnderflowUserRepositoryMemory();
    private final VoteRepositoryMemory voteRepositoryMemory = new VoteRepositoryMemory();
    private final PostTagRepositoryMemory postTagRepositoryMemory = new PostTagRepositoryMemory();

    @Override
    public PostRepository createPostRepository() {
        return postRepositoryMemory;
    }

    @Override
    public PostTypeRepository createPostTypeRepository() {
        return postTypeRepositoryMemory;
    }

    @Override
    public TagRepository createTagRepository() {
        return tagRepositoryMemory;
    }

    @Override
    public UnderflowUserRepository createUnderflowUserRepository() {
        return underflowUserRepositoryMemory;
    }

    @Override
    public VoteRepository createVoteRepository() {
        return voteRepositoryMemory;
    }

    @Override
    public PostTagRepository createPostTagRepository() {
        return postTagRepositoryMemory;
    }
}
