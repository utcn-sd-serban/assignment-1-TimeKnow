package ro.utcn.sd.mdantonio.StackUnderflow.repository.API;



import java.util.List;
import java.util.Optional;

public interface CRUDQOperations<T> {
    List<T> findAll();
    T save(T element);
    void remove(T element);
    Optional<T> findById(int id);
}
