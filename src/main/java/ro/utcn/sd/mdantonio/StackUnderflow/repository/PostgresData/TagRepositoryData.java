package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresData;

import org.springframework.data.repository.Repository;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Tag;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.TagRepository;

public interface TagRepositoryData extends Repository<Tag, Integer>, TagRepository {
    void delete(Tag element);

    @Override
    default void remove(Tag element){
        delete(element);
    }
}
