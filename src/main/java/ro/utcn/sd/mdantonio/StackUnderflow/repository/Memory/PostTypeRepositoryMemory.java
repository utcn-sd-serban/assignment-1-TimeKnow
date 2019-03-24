package ro.utcn.sd.mdantonio.StackUnderflow.repository.Memory;

import ro.utcn.sd.mdantonio.StackUnderflow.entities.PostType;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostTypeRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostTypeRepositoryMemory implements PostTypeRepository {
    private final Map<Integer, PostType> data = new ConcurrentHashMap<>(new HashMap<>());
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<PostType> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public PostType save(PostType element) {
        if(element.getPosttypeid()==null){
            element.setPosttypeid(currentId.incrementAndGet());
        }
        data.put(element.getPosttypeid(), element);
        return element;
    }

    @Override
    public void remove(PostType element) {
        data.remove(element.getPosttypeid());
    }

    @Override
    public Optional<PostType> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }
}
