package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresData;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.*;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "a1.repository-type", havingValue = "DATA")
public class RepositoryFactoryData implements RepositoryFactory {
    private final PostRepositoryData postRepositoryData;
    private final PostTypeRepository postTypeRepository;
    private final PostTagRepositoryData postTagRepositoryData;
    private final TagRepositoryData tagRepositoryData;
    private final UnderflowUserRepositoryData underflowUserRepositoryData;
    private final VoteRepositoryData voteRepositoryData;

    @Override
    public PostRepository createPostRepository() {
        return postRepositoryData;
    }

    @Override
    public PostTypeRepository createPostTypeRepository() {
        return postTypeRepository;
    }

    @Override
    public TagRepository createTagRepository() {
        return tagRepositoryData;
    }

    @Override
    public UnderflowUserRepository createUnderflowUserRepository() {
        return underflowUserRepositoryData;
    }

    @Override
    public VoteRepository createVoteRepository() {
        return voteRepositoryData;
    }

    @Override
    public PostTagRepository createPostTagRepository() {
        return postTagRepositoryData;
    }
}
