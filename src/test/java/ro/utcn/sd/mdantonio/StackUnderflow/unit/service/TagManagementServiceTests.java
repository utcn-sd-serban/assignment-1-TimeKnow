package ro.utcn.sd.mdantonio.StackUnderflow.unit.service;

import org.junit.Assert;
import org.junit.Test;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Post;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.PostTag;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Tag;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.InvalidActionException;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.InvalidPermissionException;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.ObjectAlreadyExistsException;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.*;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.Memory.RepositoryFactoryMemory;
import ro.utcn.sd.mdantonio.StackUnderflow.service.TagManagementService;

import java.util.Calendar;
import java.util.List;

import static ro.utcn.sd.mdantonio.StackUnderflow.entities.StackUnderflowConstants.*;

@SuppressWarnings("ALL")
public class TagManagementServiceTests {
    private static RepositoryFactory createMockedFactory() {
        RepositoryFactory factory = new RepositoryFactoryMemory();
        UnderflowUserRepository userRepository = factory.createUnderflowUserRepository();
        PostRepository postRepository = factory.createPostRepository();
        TagRepository tagRepository = factory.createTagRepository();
        PostTagRepository postTagRepository = factory.createPostTagRepository();
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

        return factory;
    }

    @Test
    public void testAttachTag(){
        RepositoryFactory factory = createMockedFactory();
        TagManagementService managementService = new TagManagementService(factory);
        int currentUserId = factory.createUnderflowUserRepository().findAll().get(0).getUserid();
        int postId = factory.createPostRepository().findAll().get(0).getPostid();
        int tagId = factory.createTagRepository().findAll().get(4).getTagid();

        managementService.attachTagToPost(currentUserId, tagId, postId);
        List<PostTag> tagList = factory.createPostTagRepository().findAll();
        boolean exists = tagList.stream().
                anyMatch(x->x.getPostid().equals(postId) && x.getTagid().equals(tagId));

        Assert.assertTrue(exists);
    }

    @Test(expected = InvalidActionException.class)
    public void testAttachTagInvalidActionException(){
        RepositoryFactory factory = createMockedFactory();
        TagManagementService managementService = new TagManagementService(factory);
        int currentUserId = factory.createUnderflowUserRepository().findAll().get(0).getUserid();
        int postId = factory.createPostRepository().findAll().get(1).getPostid();
        int tagId = factory.createTagRepository().findAll().get(1).getTagid();
        managementService.attachTagToPost(currentUserId, tagId, postId);
    }

    @Test(expected = InvalidPermissionException.class)
    public void testAttachTagInvalidPermissionException(){
        RepositoryFactory factory = createMockedFactory();
        TagManagementService managementService = new TagManagementService(factory);
        int currentUserId = factory.createUnderflowUserRepository().findAll().get(1).getUserid();
        int postId = factory.createPostRepository().findAll().get(0).getPostid();
        int tagId = factory.createTagRepository().findAll().get(1).getTagid();
        managementService.attachTagToPost(currentUserId, tagId, postId);
    }

    @Test
    public void createTag(){
        RepositoryFactory factory = createMockedFactory();
        TagManagementService managementService = new TagManagementService(factory);
        int size = managementService.listAllTags().size();
        managementService.createTag("testBob");
        Assert.assertEquals(factory.createTagRepository().findAll().size(), size + 1);
    }

    @Test(expected = ObjectAlreadyExistsException.class)
    public void createTagObjectAlreadyExistsException(){
        RepositoryFactory factory = createMockedFactory();
        TagManagementService managementService = new TagManagementService(factory);
        managementService.createTag("Test1");
    }

    @Test
    public void findTagsForPost(){
        RepositoryFactory factory = createMockedFactory();
        TagManagementService managementService = new TagManagementService(factory);
        int postId = factory.createPostRepository().findAll().get(1).getPostid();
        int tagId = factory.createTagRepository().findAll().get(2).getTagid();
        Tag tag = managementService.findTagsForPost(postId).get(0);
        Assert.assertEquals(tag.getTagid().intValue(), tagId);
    }

    @Test
    public void removeTagFromPost(){
        RepositoryFactory factory = createMockedFactory();
        TagManagementService managementService = new TagManagementService(factory);
        int currentUserId = factory.createUnderflowUserRepository().findAll().get(0).getUserid();
        int tagId = factory.createTagRepository().findAll().get(1).getTagid();
        managementService.deleteTag(currentUserId, tagId);
        Assert.assertEquals(factory.createTagRepository().findById(tagId).isPresent(), false);
    }

    @Test(expected = InvalidPermissionException.class)
    public void removeTagFromPostInvalidPermissionException(){
        RepositoryFactory factory = createMockedFactory();
        TagManagementService managementService = new TagManagementService(factory);
        int currentUserId = factory.createUnderflowUserRepository().findAll().get(1).getUserid();
        int tagId = factory.createTagRepository().findAll().get(1).getTagid();
        managementService.deleteTag(currentUserId, tagId);
    }
}
