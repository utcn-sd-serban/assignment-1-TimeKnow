package ro.utcn.sd.mdantonio.StackUnderflow.repository.API;

public interface RepositoryFactory {
    PostRepository createPostRepository();
    PostTypeRepository createPostTypeRepository();
    TagRepository createTagRepository();
    UnderflowUserRepository createUnderflowUserRepository();
    VoteRepository createVoteRepository();
    PostTagRepository createPostTagRepository();
}
