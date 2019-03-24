package ro.utcn.sd.mdantonio.StackUnderflow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Post;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Vote;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.BannedUserException;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.InvalidLoginException;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.InvalidPermissionException;
import ro.utcn.sd.mdantonio.StackUnderflow.exception.ObjectNotFoundExpection;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostRepository;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.RepositoryFactory;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.UnderflowUserRepository;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.VoteRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ro.utcn.sd.mdantonio.StackUnderflow.entities.StackUnderflowConstants.ADMIN;
import static ro.utcn.sd.mdantonio.StackUnderflow.entities.StackUnderflowConstants.ANSWERID;
import static ro.utcn.sd.mdantonio.StackUnderflow.entities.StackUnderflowConstants.QUESTIONID;


@Service
@RequiredArgsConstructor
public class UnderflowUserManagementService implements ManagementService {
    private final RepositoryFactory repositoryFactory;

    @Transactional
    public UnderflowUser login(String username, String password) throws BannedUserException, InvalidLoginException {
        UnderflowUser user = repositoryFactory.createUnderflowUserRepository().findByUsername(username).orElseThrow(ObjectNotFoundExpection::new);

        if(user.isBanned())
            throw new BannedUserException();
        if(!user.getPassword().equals(password))
            throw new InvalidLoginException();

        return user;
    }

    @Transactional
    public UnderflowUser addUnderflowUser(int currentUserId, String username, String password, String email, boolean banned, String permission)
            throws ObjectNotFoundExpection, InvalidPermissionException {
        UnderflowUserRepository userRepository = repositoryFactory.createUnderflowUserRepository();

        UnderflowUser currentUser = userRepository.findById(currentUserId).orElseThrow(ObjectNotFoundExpection::new);
        if(!currentUser.getPermission().equals(ADMIN))
            throw new InvalidPermissionException();

        return userRepository.save(new UnderflowUser(
                username, password, email, banned, permission));
    }

    @Transactional
    public void changeUserBannedStatus(int currentUserId, int userId, boolean banned)
            throws ObjectNotFoundExpection, InvalidPermissionException{
        UnderflowUserRepository userRepository = repositoryFactory.createUnderflowUserRepository();

        UnderflowUser currentUser = userRepository.findById(currentUserId).orElseThrow(ObjectNotFoundExpection::new);
        if(!currentUser.getPermission().equals(ADMIN))
            throw new InvalidPermissionException();

        UnderflowUser user = userRepository.findById(userId).orElseThrow(ObjectNotFoundExpection::new);
        user.setBanned(banned);
        userRepository.save(user);
    }

    @Transactional
    public void changeUserPassword(int userId, String password){
        UnderflowUserRepository userRepository = repositoryFactory.createUnderflowUserRepository();
        UnderflowUser user = userRepository.findById(userId).orElseThrow(ObjectNotFoundExpection::new);
        user.setPassword(password);
        userRepository.save(user);
    }

    @Transactional
    public void changeUserPermission(int userId, String permission){
        UnderflowUserRepository userRepository = repositoryFactory.createUnderflowUserRepository();
        UnderflowUser user = userRepository.findById(userId).orElseThrow(ObjectNotFoundExpection::new);
        user.setPermission(permission);
        userRepository.save(user);
    }

    @Transactional
    public void changeUserEmail(int userId, String email){
        UnderflowUserRepository userRepository = repositoryFactory.createUnderflowUserRepository();
        UnderflowUser user = userRepository.findById(userId).orElseThrow(ObjectNotFoundExpection::new);
        user.setPermission(email);
        userRepository.save(user);
    }

    @Transactional
    public void removeUser(int currentUserId, int userId) throws InvalidPermissionException, ObjectNotFoundExpection{
        UnderflowUserRepository userRepository = repositoryFactory.createUnderflowUserRepository();

        UnderflowUser currentUser = userRepository.findById(currentUserId).orElseThrow(ObjectNotFoundExpection::new);
        if(!currentUser.getPermission().equals(ADMIN))
            throw new InvalidPermissionException();

        UnderflowUser user = userRepository.findById(userId).orElseThrow(ObjectNotFoundExpection::new);
        userRepository.remove(user);
    }

    @Transactional
    public List<UnderflowUser> findAll(){
        List<UnderflowUser> users = repositoryFactory.createUnderflowUserRepository().findAll();
        users.forEach(x->x.setScore(calculateScore(x.getUserid())));
        return users;
    }

    private long calculateScore(int userId){
        //I am not proud of this but is short
        PostRepository postRepository = repositoryFactory.createPostRepository();
        VoteRepository voteRepository = repositoryFactory.createVoteRepository();
        List<Vote> allVotes = voteRepository.findAll();
        long userDownVotes = allVotes.stream().filter(x->x.getUserid().equals(userId)).filter(x->!x.isUpvote()).count();
        List<Post> postList = postRepository.findAll().stream().filter(x->x.getAuthorid().
                equals(userId)).collect(Collectors.toList());

        //TODO: When I have time a more 'civilized' implementation will be made
        long postScore= postList.stream().map(post->{
            Optional<Vote> postVote = allVotes.stream().filter(y->y.getPostid().equals(post.getPostid())).findFirst();
            return postVote.map(vote -> scoreLogic(post, vote)).orElse((long) 0);
        }).mapToLong(x->((Number) x).longValue()).sum();

        return postScore-userDownVotes;
    }

    private long scoreLogic(Post post, Vote vote){
        if(post.getPosttypeid().equals(QUESTIONID))
            return vote.isUpvote()? 5 : -2;
        if(post.getPosttypeid().equals(ANSWERID))
            return vote.isUpvote()? 10 : -2;
        return 0;
    }
}
