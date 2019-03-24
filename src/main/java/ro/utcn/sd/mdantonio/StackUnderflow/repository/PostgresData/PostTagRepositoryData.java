package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresData;

import org.springframework.data.repository.Repository;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.PostTag;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostTagRepository;

public interface PostTagRepositoryData extends Repository<PostTag, Integer>, PostTagRepository {

    void delete(PostTag element);

    @Override
    default void remove(PostTag element){
        delete(element);
    }
}
