package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresData;

import org.springframework.data.repository.Repository;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.UnderflowUserRepository;

public interface UnderflowUserRepositoryData extends Repository<UnderflowUser, Integer>, UnderflowUserRepository {
    void delete(UnderflowUser element);

    @Override
    default void remove(UnderflowUser element){
        delete(element);
    }
}
