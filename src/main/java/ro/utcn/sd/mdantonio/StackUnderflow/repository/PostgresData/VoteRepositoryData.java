package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresData;

import org.springframework.data.repository.Repository;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Vote;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.VoteRepository;

public interface VoteRepositoryData extends Repository<Vote, Integer>, VoteRepository {
    void delete(Vote element);

    @Override
    default void remove(Vote element){
        delete(element);
    }
}
