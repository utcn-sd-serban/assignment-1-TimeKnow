package ro.utcn.sd.mdantonio.StackUnderflow.unit.service;

import org.junit.Assert;
import org.junit.Test;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Post;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Vote;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.InvalidLoginException;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.InvalidPermissionException;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostRepository;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.RepositoryFactory;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.UnderflowUserRepository;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.VoteRepository;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.Memory.RepositoryFactoryMemory;
import ro.utcn.sd.mdantonio.StackUnderflow.service.UnderflowUserManagementService;

import java.util.Calendar;

import static ro.utcn.sd.mdantonio.StackUnderflow.entities.StackUnderflowConstants.*;
import static ro.utcn.sd.mdantonio.StackUnderflow.entities.StackUnderflowConstants.ANSWERID;

@SuppressWarnings("ALL")
public class UnderflowUserManagementServiceTests {
    private static RepositoryFactory createMockedFactory() {
        RepositoryFactory factory = new RepositoryFactoryMemory();
        UnderflowUserRepository userRepository = factory.createUnderflowUserRepository();
        PostRepository postRepository = factory.createPostRepository();
        VoteRepository voteRepository = factory.createVoteRepository();
                
        if(userRepository.findAll().isEmpty()){
            userRepository.save(new UnderflowUser("superuser",
                    "superuser", "superuser@gmail.com", false, ADMIN));//0
            userRepository.save(new UnderflowUser("test",
                    "test", "test@gmail.com", false, USER));//1
            userRepository.save(new UnderflowUser("bob",
                    "bob", "bob@gmail.com", false, USER));//2
        }

        if(postRepository.findAll().isEmpty()){
            postRepository.save(new Post(QUESTIONID, userRepository.findAll().get(0).getUserid(),
                    null, "Test Question 1", "Test Question number 1?", Calendar.getInstance().getTime()));

            postRepository.save(new Post(ANSWERID, userRepository.findAll().get(0).getUserid(),
                    postRepository.findAll().get(0).getPostid(), "Test Answer 1", "Test Answer number 1?", Calendar.getInstance().getTime()));
        }


        if(voteRepository.findAll().isEmpty()){
            voteRepository.save(new Vote(userRepository.findAll().get(1).getUserid(), postRepository.findAll().get(0).getPostid(), false));//0
            voteRepository.save(new Vote(userRepository.findAll().get(1).getUserid(), postRepository.findAll().get(1).getPostid(), true));//1
        }
        return factory;
    }

    @Test(expected = InvalidLoginException.class)
    public void testLoginInvalidPassword(){
        RepositoryFactory factory = createMockedFactory();
        UnderflowUserManagementService userManagementService = new UnderflowUserManagementService(factory);
        userManagementService.login("superuser", "invalid");
    }

    @Test
    public void testLoginValidPassword(){
        RepositoryFactory factory = createMockedFactory();
        UnderflowUserManagementService userManagementService = new UnderflowUserManagementService(factory);
        UnderflowUser user = userManagementService.login("superuser", "superuser");
        Assert.assertEquals(user, factory.createUnderflowUserRepository().findAll().get(0));
    }

    @Test
    public void testBanUser(){
        RepositoryFactory factory = createMockedFactory();
        UnderflowUserManagementService userManagementService = new UnderflowUserManagementService(factory);
        userManagementService.changeUserBannedStatus(factory.createUnderflowUserRepository().findAll().get(0).getUserid(),
                factory.createUnderflowUserRepository().findAll().get(1).getUserid(), true);
        Assert.assertEquals(factory.createUnderflowUserRepository().findAll().get(1).isBanned(), true);
    }

    @Test
    public void testUnbanUser(){
        RepositoryFactory factory = createMockedFactory();
        UnderflowUserManagementService userManagementService = new UnderflowUserManagementService(factory);
        userManagementService.changeUserBannedStatus(factory.createUnderflowUserRepository().findAll().get(0).getUserid(),
                factory.createUnderflowUserRepository().findAll().get(1).getUserid(), true);
        userManagementService.changeUserBannedStatus(factory.createUnderflowUserRepository().findAll().get(0).getUserid(),
                factory.createUnderflowUserRepository().findAll().get(1).getUserid(), false);
        Assert.assertEquals(factory.createUnderflowUserRepository().findAll().get(1).isBanned(), false);
    }

    @Test(expected = InvalidPermissionException.class)
    public void testBanInvalidPermission(){
        RepositoryFactory factory = createMockedFactory();
        UnderflowUserManagementService userManagementService = new UnderflowUserManagementService(factory);
        userManagementService.changeUserBannedStatus(factory.createUnderflowUserRepository().findAll().get(2).getUserid(),
                factory.createUnderflowUserRepository().findAll().get(1).getUserid(), true);
    }

    @Test
    public void testScoreLogic(){
        RepositoryFactory factory = createMockedFactory();
        UnderflowUserManagementService userManagementService = new UnderflowUserManagementService(factory);
        Assert.assertEquals(userManagementService.findAll().get(0).getScore(), 8);
    }

    @Test
    public void removeUser(){
        RepositoryFactory factory = createMockedFactory();
        UnderflowUserManagementService userManagementService = new UnderflowUserManagementService(factory);
        int deletedUserId = factory.createUnderflowUserRepository().findAll().get(1).getUserid();
        userManagementService.removeUser(factory.createUnderflowUserRepository().findAll().get(0).getUserid(),
                deletedUserId);
        Assert.assertEquals(factory.createUnderflowUserRepository().findById(deletedUserId).isPresent(), false);
    }

    @Test(expected = InvalidPermissionException.class)
    public void removeUserInvalidPermissionException(){
        RepositoryFactory factory = createMockedFactory();
        UnderflowUserManagementService userManagementService = new UnderflowUserManagementService(factory);
        userManagementService.removeUser(factory.createUnderflowUserRepository().findAll().get(1).getUserid(),
                factory.createUnderflowUserRepository().findAll().get(1).getUserid());
    }
}
