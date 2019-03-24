package ro.utcn.sd.mdantonio.StackUnderflow.repository.Memory;

import ro.utcn.sd.mdantonio.StackUnderflow.entities.PostTag;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostTagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostTagRepositoryMemory implements PostTagRepository {
    private final Map<Integer, PostTag> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);
    @Override
    public List<PostTag> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void remove(PostTag element) {
        data.remove(element.getPosttagid());
    }

    @Override
    public Optional<PostTag> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public PostTag save(PostTag element) {
        if(element.getPosttagid()==null){
            element.setPosttagid(currentId.incrementAndGet());
        }
        data.put(element.getPosttagid(), element);
        return element;
    }
}
