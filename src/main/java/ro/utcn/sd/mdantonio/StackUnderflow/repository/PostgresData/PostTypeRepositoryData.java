package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresData;

import org.springframework.data.repository.Repository;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.PostType;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostTypeRepository;

public interface PostTypeRepositoryData extends Repository<PostType, Integer>, PostTypeRepository {
    void delete(PostType element);

    @Override
    default void remove(PostType element){
        delete(element);
    }
}
