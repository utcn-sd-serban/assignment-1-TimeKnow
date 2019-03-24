package ro.utcn.sd.mdantonio.StackUnderflow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.*;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.InvalidActionException;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.InvalidPermissionException;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.ObjectNotFoundExpection;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostRepository;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.RepositoryFactory;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.VoteRepository;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ro.utcn.sd.mdantonio.StackUnderflow.entities.StackUnderflowConstants.*;

@Service
@RequiredArgsConstructor
public class PostManagementService implements ManagementService {
    private final RepositoryFactory repositoryFactory;
    @Transactional
    public List<Post> listQuestions(){
        List<Post> questions =  repositoryFactory.createPostRepository().findAll().stream().
                filter(x->x.getPosttypeid()==QUESTIONID).collect(Collectors.toList());
        questions.forEach(x->x.setScore(calculatePostScore(x)));
        return questions.stream().sorted((x, y)->Long.compare(y.getScore(), x.getScore())).collect(Collectors.toList());
    }

    @Transactional
    public List<Post> listQuestionResponses(int questionId){
        PostRepository postRepository = repositoryFactory.createPostRepository();
        List<Post> answers = postRepository.findAll().stream().filter(x->x.getParentid().
                equals(new Integer(questionId))).collect(Collectors.toList());
        answers.forEach(x->x.setScore(calculatePostScore(x)));
        return answers.stream().sorted((x,y)->Long.compare(y.getScore(), x.getScore())).collect(Collectors.toList());
    }

    @Transactional
    public Post addPost(int postTypeId, int authorId, Integer parentId, String title, String body, Date creationDate)
        throws ObjectNotFoundExpection{
        PostRepository postRepository = repositoryFactory.createPostRepository();

        if(postTypeId==ANSWERID)
            postRepository.findById(parentId).orElseThrow(ObjectNotFoundExpection::new);
        repositoryFactory.createUnderflowUserRepository().findById(authorId).orElseThrow(ObjectNotFoundExpection::new);

        return postRepository.save(new Post(postTypeId, authorId, parentId, title, body, creationDate));
    }

    @Transactional
    public Vote votePost(int currentUserId, int postId, boolean isUpvote)
            throws ObjectNotFoundExpection, InvalidActionException{
        Post post = repositoryFactory.createPostRepository().findById(postId).orElseThrow(ObjectNotFoundExpection::new);
        UnderflowUser user = repositoryFactory.createUnderflowUserRepository().findById(currentUserId).
                orElseThrow(ObjectNotFoundExpection::new);


        if(user.getUserid().equals(post.getAuthorid()))
            throw new InvalidActionException();

        return repositoryFactory.createVoteRepository().save(new Vote(currentUserId, postId, isUpvote));
    }

    @Transactional
    public List<Post> listPostByTitle(String title){
        List<Post> postList =  repositoryFactory.createPostRepository().findAll().stream()
                .filter(x->x.getPosttypeid().equals(QUESTIONID))
                .filter(x->x.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
        postList.forEach(x->x.setScore(calculatePostScore(x)));
        return postList.stream().sorted((x,y)->Long.compare(y.getScore(), x.getScore())).collect(Collectors.toList());
    }

    @Transactional
    public List<Post> listPostByTag(int tagId){
        List<Integer> postIdList = repositoryFactory.createPostTagRepository().findAll().
                stream().filter(x->x.getTagid()==tagId).map(PostTag::getPostid).collect(Collectors.toList());
        List<Post> postList = repositoryFactory.createPostRepository().findAll().stream().
                filter(x->postIdList.contains(x.getPostid())).collect(Collectors.toList());
        postList.forEach(x->x.setScore(calculatePostScore(x)));
        return postList.stream().sorted((x,y)->Long.compare(y.getScore(), x.getScore())).collect(Collectors.toList());
    }

    @Transactional
    public Post updatePost(int currentUserId, int postId, String title, String body)
            throws ObjectNotFoundExpection, InvalidPermissionException{
        PostRepository postRepository = repositoryFactory.createPostRepository();
        Post post = postRepository.findById(postId).orElseThrow(ObjectNotFoundExpection::new);
        UnderflowUser user = repositoryFactory.createUnderflowUserRepository().findById(currentUserId).
                orElseThrow(ObjectNotFoundExpection::new);

        if(!user.getPermission().equals(ADMIN) && !post.getAuthorid().equals(user.getUserid()))
            throw new InvalidPermissionException();

        post.setBody(body);
        post.setTitle(title);
        if(post.getPosttypeid().equals(QUESTIONID))
            post.setParentid(null);

        return postRepository.save(post);
    }

    @Transactional
    public void removePost(int currentUserId, int postId)
            throws ObjectNotFoundExpection, InvalidPermissionException{
        UnderflowUser user = repositoryFactory.createUnderflowUserRepository().findById(currentUserId).orElseThrow(ObjectNotFoundExpection::new);
        PostRepository postRepository = repositoryFactory.createPostRepository();
        Post post = postRepository.findById(postId).orElseThrow(ObjectNotFoundExpection::new);

        if(!user.getPermission().equals(ADMIN) && !(post.getAuthorid().equals(user.getUserid()) && post.getPosttypeid().equals(ANSWERID)))
            throw new InvalidPermissionException();

        postRepository.remove(post);
    }

    @Transactional
    public Post findPostById(int postId){
        Post post =  repositoryFactory.createPostRepository().findById(postId).orElseThrow(ObjectNotFoundExpection::new);
        post.setScore(calculatePostScore(post));
        return post;
    }

    private long calculatePostScore(Post post){
        VoteRepository voteRepository = repositoryFactory.createVoteRepository();
        List<Vote> votes = voteRepository.findAll().stream().filter(x-> x.getPostid().equals(post.getPostid())).
                collect(Collectors.toList());
        long upVotes = votes.stream().filter(Vote::isUpvote).count();
        long downVotes = votes.stream().filter(x->!x.isUpvote()).count();
        return upVotes-downVotes;
    }
}
