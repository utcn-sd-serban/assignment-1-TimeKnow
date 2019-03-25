package ro.utcn.sd.mdantonio.StackUnderflow.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.*;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static ro.utcn.sd.mdantonio.StackUnderflow.entities.StackUnderflowConstants.*;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@Profile("!test")
public class Seeder implements CommandLineRunner {
    private final RepositoryFactory factory;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        UnderflowUserRepository userRepository = factory.createUnderflowUserRepository();
        PostRepository postRepository = factory.createPostRepository();
        PostTypeRepository postTypeRepository = factory.createPostTypeRepository();
        VoteRepository voteRepository = factory.createVoteRepository();
        TagRepository tagRepository = factory.createTagRepository();
        PostTagRepository postTagRepository = factory.createPostTagRepository();

        List<UnderflowUser> underflowUsers = new ArrayList<>();
        List<Post> posts = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();

        if(postTypeRepository.findAll().isEmpty()){
            QUESTIONID = postTypeRepository.save(new PostType(QUESTION)).getPosttypeid();
            ANSWERID = postTypeRepository.save(new PostType(ANSWER)).getPosttypeid();
        }
        else {
            List<PostType> postTypeList = postTypeRepository.findAll();
            QUESTIONID = postTypeList.get(0).getPosttypeid();
            ANSWERID = postTypeList.get(1).getPosttypeid();
        }

        if(userRepository.findAll().isEmpty()){
            underflowUsers.add(userRepository.save(new UnderflowUser("superuser",
                    "superuser", "superuser@gmail.com", false, ADMIN)));//0
            underflowUsers.add(userRepository.save(new UnderflowUser("test",
                    "test", "test@gmail.com", false, USER)));//1
            underflowUsers.add(userRepository.save(new UnderflowUser("bob",
                    "bob", "bob@gmail.com", false, USER)));//2
        }

        if(postRepository.findAll().isEmpty()){
            posts.add(postRepository.save(new Post(QUESTIONID, underflowUsers.get(1).getUserid(),
                    null, "Test Question 1", "Test Question number 1?", Calendar.getInstance().getTime()))); //0
            posts.add(postRepository.save(new Post(QUESTIONID, underflowUsers.get(1).getUserid(),
                    null, "Test Question 2", "Test Question number 2?", Calendar.getInstance().getTime()))); //1
            posts.add(postRepository.save(new Post(QUESTIONID, underflowUsers.get(2).getUserid(),
                    null, "Test Question 3", "Test Question number 3?", Calendar.getInstance().getTime()))); //2
            posts.add(postRepository.save(new Post(QUESTIONID, underflowUsers.get(2).getUserid(),
                    null, "Test Question 4", "Test Question number 4?", Calendar.getInstance().getTime()))); //3

            posts.add(postRepository.save(new Post(ANSWERID, underflowUsers.get(1).getUserid(),
                    posts.get(0).getPostid(), "Test Answer 1", "Test Answer number 1?", Calendar.getInstance().getTime()))); //4
            posts.add(postRepository.save(new Post(ANSWERID, underflowUsers.get(2).getUserid(),
                    posts.get(0).getPostid(), "Test Answer 2", "Test Answer number 2?", Calendar.getInstance().getTime()))); //5
            posts.add(postRepository.save(new Post(ANSWERID, underflowUsers.get(1).getUserid(),
                    posts.get(1).getPostid(), "Test Answer 3", "Test Answer number 3?", Calendar.getInstance().getTime())));//6
            posts.add(postRepository.save(new Post(ANSWERID, underflowUsers.get(2).getUserid(),
                    posts.get(1).getPostid(), "Test Answer 4", "Test Answer number 4?", Calendar.getInstance().getTime())));//7
            posts.add(postRepository.save(new Post(ANSWERID, underflowUsers.get(2).getUserid(),
                    posts.get(1).getPostid(), "Test Answer 5", "Test Answer number 5?", Calendar.getInstance().getTime())));//8
        }

        if(voteRepository.findAll().isEmpty()){
            voteRepository.save(new Vote(underflowUsers.get(1).getUserid(), posts.get(2).getPostid(), true));//0
            voteRepository.save(new Vote(underflowUsers.get(1).getUserid(), posts.get(3).getPostid(), true));//1
            voteRepository.save(new Vote(underflowUsers.get(2).getUserid(), posts.get(0).getPostid(), true));//2
            voteRepository.save(new Vote(underflowUsers.get(2).getUserid(), posts.get(4).getPostid(), true));//3
            voteRepository.save(new Vote(underflowUsers.get(2).getUserid(), posts.get(6).getPostid(), true));//4
            voteRepository.save(new Vote(underflowUsers.get(1).getUserid(), posts.get(5).getPostid(), true));//5
        }

        if(tagRepository.findAll().isEmpty()){
            tags.add(tagRepository.save(new Tag("Amazing Art")));
            tags.add(tagRepository.save(new Tag("Testing")));
            tags.add(tagRepository.save(new Tag("O.C")));
            tags.add(tagRepository.save(new Tag("Joldos Facebook Posts")));
            tags.add(tagRepository.save(new Tag("Imagination underflow")));
        }

        if(postTagRepository.findAll().isEmpty()){
            postTagRepository.save(new PostTag(posts.get(0).getPostid(),tags.get(0).getTagid()));
            postTagRepository.save(new PostTag(posts.get(0).getPostid(),tags.get(1).getTagid()));
            postTagRepository.save(new PostTag(posts.get(0).getPostid(),tags.get(2).getTagid()));
            postTagRepository.save(new PostTag(posts.get(1).getPostid(),tags.get(2).getTagid()));
            postTagRepository.save(new PostTag(posts.get(2).getPostid(),tags.get(4).getTagid()));
            postTagRepository.save(new PostTag(posts.get(3).getPostid(),tags.get(3).getTagid()));
            postTagRepository.save(new PostTag(posts.get(4).getPostid(),tags.get(3).getTagid()));
        }


    }
}
