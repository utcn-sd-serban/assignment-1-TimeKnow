package ro.utcn.sd.mdantonio.StackUnderflow.integration;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.*;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.*;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.*;
import ro.utcn.sd.mdantonio.StackUnderflow.service.PostManagementService;
import ro.utcn.sd.mdantonio.StackUnderflow.service.TagManagementService;
import ro.utcn.sd.mdantonio.StackUnderflow.service.UnderflowUserManagementService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.utcn.sd.mdantonio.StackUnderflow.entities.StackUnderflowConstants.*;

@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTests {

    @Autowired
    private TagManagementService tagManagementService;
    @Autowired
    private PostManagementService postManagementService;
    @Autowired
    private UnderflowUserManagementService underflowUserManagementService;
    @Autowired
    private RepositoryFactory factory;


    @Before
    public void seeder(){
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
            userRepository.save(new UnderflowUser("bob",
                    "bob", "bob@gmail.com", false, USER));//2
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
    }

    @After
    public void cleaner(){
        UnderflowUserRepository userRepository = factory.createUnderflowUserRepository();
        PostRepository postRepository = factory.createPostRepository();
        TagRepository tagRepository = factory.createTagRepository();
        PostTagRepository postTagRepository = factory.createPostTagRepository();
        VoteRepository voteRepository = factory.createVoteRepository();

        List<UnderflowUser> users = userRepository.findAll();
        List<Post> postList = postRepository.findAll();
        List<Tag> tagList = tagRepository.findAll();
        List<PostTag> postTagList = postTagRepository.findAll();
        List<Vote> voteList = voteRepository.findAll();

        for(Vote v : voteList)
            voteRepository.remove(v);
        for(PostTag pt : postTagList)
            postTagRepository.remove(pt);
        for(Post p: postList)
            postRepository.remove(p);
        for(Tag t : tagList)
            tagRepository.remove(t);
        for(UnderflowUser u : users)
            userRepository.remove(u);
    }

    @Test
    public void addPost(){
        int authorId = factory.createUnderflowUserRepository().findAll().get(0).getUserid();
        Post post = postManagementService.addPost(QUESTIONID, authorId, null, "Ta", "da", Calendar.getInstance().getTime());
        boolean exists = factory.createPostRepository().findAll().stream().anyMatch(x->x.getPostid().equals(post.getPostid()));
        Assert.assertEquals(exists, true);
    }

    @Test
    public void updatePost(){
        int postId = factory.createPostRepository().findAll().get(0).getPostid();
        postManagementService.updatePost(factory.createUnderflowUserRepository().findAll().get(0).getUserid(),postId, "new", "new");
        Post post = factory.createPostRepository().findById(postId).orElseThrow(ObjectNotFoundExpection::new);
        Assert.assertEquals(post.getTitle(), "new");
        Assert.assertEquals(post.getBody(), "new");
    }

    @Test(expected = InvalidPermissionException.class)
    public void updatePostInvalidPermissionException(){
        List<UnderflowUser> userList = factory.createUnderflowUserRepository().findAll();
        List<Post> postList = factory.createPostRepository().findAll();
        postManagementService.updatePost(userList.get(1).getUserid(),
                postList.get(0).getPostid(), "new", "new");
    }

    @Test
    public void removePost(){
        int postId = factory.createPostRepository().findAll().get(0).getPostid();
        postManagementService.removePost(factory.createUnderflowUserRepository().findAll().get(0).getUserid(), postId);
        boolean exists = factory.createPostRepository().findAll().stream().anyMatch(x->x.getPostid().equals(postId));
        Assert.assertEquals(exists, false);
    }

    @Test(expected = InvalidPermissionException.class)
    public void removePostInvalidPermissionException(){
        int postId = factory.createPostRepository().findAll().get(0).getPostid();
        postManagementService.removePost(factory.createUnderflowUserRepository().findAll().get(1).getUserid(), postId);
    }

    @Test
    public void scoreTest(){
        Assert.assertEquals(postManagementService.listQuestions().get(0).getScore(), -1);
    }

    @Test
    public void testAttachTag(){
        int currentUserId = factory.createUnderflowUserRepository().findAll().get(0).getUserid();
        int postId = factory.createPostRepository().findAll().get(0).getPostid();
        int tagId = factory.createTagRepository().findAll().get(4).getTagid();

        tagManagementService.attachTagToPost(currentUserId, tagId, postId);
        List<PostTag> tagList = factory.createPostTagRepository().findAll();
        boolean exists = tagList.stream().
                anyMatch(x->x.getPostid().equals(postId) && x.getTagid().equals(tagId));

        Assert.assertTrue(exists);
    }

    @Test(expected = InvalidActionException.class)
    public void testAttachTagInvalidActionException(){
        int currentUserId = factory.createUnderflowUserRepository().findAll().get(0).getUserid();
        int postId = factory.createPostRepository().findAll().get(1).getPostid();
        int tagId = factory.createTagRepository().findAll().get(1).getTagid();
        tagManagementService.attachTagToPost(currentUserId, tagId, postId);
    }

    @Test(expected = InvalidPermissionException.class)
    public void testAttachTagInvalidPermissionException(){
        int currentUserId = factory.createUnderflowUserRepository().findAll().get(1).getUserid();
        int postId = factory.createPostRepository().findAll().get(0).getPostid();
        int tagId = factory.createTagRepository().findAll().get(1).getTagid();
        tagManagementService.attachTagToPost(currentUserId, tagId, postId);
    }

    @Test
    public void createTag(){
        int size = tagManagementService.listAllTags().size();
        tagManagementService.createTag("testBob");
        Assert.assertEquals(factory.createTagRepository().findAll().size(), size + 1);
    }

    @Test(expected = ObjectAlreadyExistsException.class)
    public void createTagObjectAlreadyExistsException(){
        tagManagementService.createTag("Test1");
    }

    @Test
    public void findTagsForPost(){
        int postId = factory.createPostRepository().findAll().get(1).getPostid();
        int tagId = factory.createTagRepository().findAll().get(2).getTagid();
        Tag tag = tagManagementService.findTagsForPost(postId).get(0);
        Assert.assertEquals(tag.getTagid().intValue(), tagId);
    }

    @Test
    public void removeTagFromPost(){
        int currentUserId = factory.createUnderflowUserRepository().findAll().get(0).getUserid();
        int tagId = factory.createTagRepository().findAll().get(1).getTagid();
        tagManagementService.deleteTag(currentUserId, tagId);
        Assert.assertEquals(factory.createTagRepository().findById(tagId).isPresent(), false);
    }

    @Test(expected = InvalidPermissionException.class)
    public void removeTagFromPostInvalidPermissionException(){
        int currentUserId = factory.createUnderflowUserRepository().findAll().get(1).getUserid();
        int tagId = factory.createTagRepository().findAll().get(1).getTagid();
        tagManagementService.deleteTag(currentUserId, tagId);
    }

    @Test(expected = InvalidLoginException.class)
    public void testLoginInvalidPassword(){
        underflowUserManagementService.login("superuser", "invalid");
    }

    @Test
    public void testLoginValidPassword(){
        UnderflowUser user = underflowUserManagementService.login("superuser", "superuser");
        Assert.assertEquals(user, factory.createUnderflowUserRepository().findAll().get(0));
    }

    @Test
    public void testBanUser(){
        int userId = factory.createUnderflowUserRepository().findAll().get(1).getUserid();
        underflowUserManagementService.changeUserBannedStatus(factory.createUnderflowUserRepository().findAll().get(0).getUserid(),
                userId, true);
        UnderflowUser user = factory.createUnderflowUserRepository().findById(userId).get();
        Assert.assertEquals(user.isBanned(), true);
    }

    @Test(expected = InvalidPermissionException.class)
    public void testBanInvalidPermission(){
        underflowUserManagementService.changeUserBannedStatus(factory.createUnderflowUserRepository().findAll().get(2).getUserid(),
                factory.createUnderflowUserRepository().findAll().get(1).getUserid(), true);
    }

    @Test
    public void testScoreLogic(){
        Assert.assertEquals(underflowUserManagementService.findAll().get(0).getScore(), -2);
    }

    @Test
    public void removeUser(){
        int deletedUserId = factory.createUnderflowUserRepository().findAll().get(1).getUserid();
        underflowUserManagementService.removeUser(factory.createUnderflowUserRepository().findAll().get(0).getUserid(),
                deletedUserId);
        Assert.assertEquals(factory.createUnderflowUserRepository().findById(deletedUserId).isPresent(), false);
    }

    @Test(expected = InvalidPermissionException.class)
    public void removeUserInvalidPermissionException(){
        underflowUserManagementService.removeUser(factory.createUnderflowUserRepository().findAll().get(1).getUserid(),
                factory.createUnderflowUserRepository().findAll().get(1).getUserid());
    }
}
