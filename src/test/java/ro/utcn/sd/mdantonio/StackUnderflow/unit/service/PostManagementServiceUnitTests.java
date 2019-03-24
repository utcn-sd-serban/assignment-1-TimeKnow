package ro.utcn.sd.mdantonio.StackUnderflow.unit.service;

import org.junit.Assert;
import org.junit.Test;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.*;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.InvalidPermissionException;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.*;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.Memory.RepositoryFactoryMemory;
import ro.utcn.sd.mdantonio.StackUnderflow.service.PostManagementService;

import java.util.Calendar;

import static ro.utcn.sd.mdantonio.StackUnderflow.entities.StackUnderflowConstants.*;

@SuppressWarnings("ALL")
public class PostManagementServiceUnitTests {
    private static RepositoryFactory createMockedFactory() {
        RepositoryFactory factory = new RepositoryFactoryMemory();
        UnderflowUserRepository userRepository = factory.createUnderflowUserRepository();
        PostRepository postRepository = factory.createPostRepository();
        TagRepository tagRepository = factory.createTagRepository();
        PostTagRepository postTagRepository = factory.createPostTagRepository();
        VoteRepository voteRepository = factory.createVoteRepository();

        if(userRepository.findAll().isEmpty()) {
            userRepository.save(new UnderflowUser("superuser",
                    "superuser", "superuser@gmail.com", false, ADMIN));
            userRepository.save(new UnderflowUser("t",
                    "t", "t@gmail.com", false, USER));
        }

        if(postRepository.findAll().isEmpty()) {
            postRepository.save(new Post(QUESTIONID, userRepository.findAll().get(0).getUserid(),
                    null, "Test Question 1", "Test Question number 1?", Calendar.getInstance().getTime())); //0
            postRepository.save(new Post(ANSWERID, userRepository.findAll().get(1).getUserid(),
                    null, "Test Question 2", "Test Question number 2?", Calendar.getInstance().getTime()));
        }

        if(tagRepository.findAll().isEmpty()){
            tagRepository.save(new Tag("Test1"));
            tagRepository.save(new Tag("Test2"));
            tagRepository.save(new Tag("Test3"));
            tagRepository.save(new Tag("Test4"));
            tagRepository.save(new Tag("Test5"));
        }

        if(postTagRepository.findAll().isEmpty()){
            postTagRepository.save(new PostTag(postRepository.findAll().get(0).getPostid(),tagRepository.findAll().get(0).getTagid()));
            postTagRepository.save(new PostTag(postRepository.findAll().get(0).getPostid(),tagRepository.findAll().get(1).getTagid()));
            postTagRepository.save(new PostTag(postRepository.findAll().get(0).getPostid(),tagRepository.findAll().get(2).getTagid()));
            postTagRepository.save(new PostTag(postRepository.findAll().get(1).getPostid(),tagRepository.findAll().get(2).getTagid()));
        }

        if(voteRepository.findAll().isEmpty()){
            voteRepository.save(new Vote(userRepository.findAll().get(1).getUserid(), postRepository.findAll().get(0).getPostid(), false));//0
            voteRepository.save(new Vote(userRepository.findAll().get(1).getUserid(), postRepository.findAll().get(1).getPostid(), true));//1
        }
        return factory;
    }

    @Test
    public void addPost(){
        RepositoryFactory factory = createMockedFactory();
        PostManagementService postManagementService = new PostManagementService(factory);
        int authorId = factory.createUnderflowUserRepository().findAll().get(0).getUserid();
        Post post = postManagementService.addPost(QUESTIONID, authorId, null, "Ta", "da", Calendar.getInstance().getTime());
        boolean exists = factory.createPostRepository().findAll().stream().anyMatch(x->x.getPostid().equals(post.getPostid()));
        Assert.assertEquals(exists, true);
    }

    @Test
    public void updatePost(){
        RepositoryFactory factory = createMockedFactory();
        PostManagementService postManagementService = new PostManagementService(factory);
        postManagementService.updatePost(factory.createUnderflowUserRepository().findAll().get(0).getUserid(),
                factory.createPostRepository().findAll().get(0).getPostid(), "new", "new");
        Post post = factory.createPostRepository().findAll().get(0);
        Assert.assertEquals(post.getTitle(), "new");
        Assert.assertEquals(post.getBody(), "new");
    }

    @Test(expected = InvalidPermissionException.class)
    public void updatePostInvalidPermissionException(){
        RepositoryFactory factory = createMockedFactory();
        PostManagementService postManagementService = new PostManagementService(factory);
        postManagementService.updatePost(factory.createUnderflowUserRepository().findAll().get(1).getUserid(),
                factory.createPostRepository().findAll().get(0).getPostid(), "new", "new");
    }

    @Test
    public void removePost(){
        RepositoryFactory factory = createMockedFactory();
        PostManagementService postManagementService = new PostManagementService(factory);
        int postId = factory.createPostRepository().findAll().get(0).getPostid();
        postManagementService.removePost(factory.createUnderflowUserRepository().findAll().get(0).getUserid(), postId);
        boolean exists = factory.createPostRepository().findAll().stream().anyMatch(x->x.getPostid().equals(postId));
        Assert.assertEquals(exists, false);
    }

    @Test(expected = InvalidPermissionException.class)
    public void removePostInvalidPermissionException(){
        RepositoryFactory factory = createMockedFactory();
        PostManagementService postManagementService = new PostManagementService(factory);
        int postId = factory.createPostRepository().findAll().get(0).getPostid();
        postManagementService.removePost(factory.createUnderflowUserRepository().findAll().get(1).getUserid(), postId);
    }

    @Test
    public void scoreTest(){
        RepositoryFactory factory = createMockedFactory();
        PostManagementService postManagementService = new PostManagementService(factory);
        Assert.assertEquals(postManagementService.listQuestions().get(0).getScore(), -1);
    }


}
