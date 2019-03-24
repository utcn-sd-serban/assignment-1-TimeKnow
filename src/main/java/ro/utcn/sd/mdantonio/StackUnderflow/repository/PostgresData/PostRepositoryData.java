package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresData;

import org.springframework.data.repository.Repository;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Post;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostRepository;

public interface PostRepositoryData extends Repository<Post, Integer>, PostRepository {

    void delete(Post element);

    @Override
    default void remove(Post element){
        delete(element);
    }
}
