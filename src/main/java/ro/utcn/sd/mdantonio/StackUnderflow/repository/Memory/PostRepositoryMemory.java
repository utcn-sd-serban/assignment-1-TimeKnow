package ro.utcn.sd.mdantonio.StackUnderflow.repository.Memory;

import ro.utcn.sd.mdantonio.StackUnderflow.entities.Post;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostRepositoryMemory implements PostRepository {
    private final Map<Integer, Post> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Post save(Post element) {
        if(element.getPostid()==null){
            element.setPostid(currentId.incrementAndGet());
        }
        data.put(element.getPostid(), element);
        return element;
    }

    @Override
    public void remove(Post element) {
        data.remove(element.getPostid());
    }

    @Override
    public Optional<Post> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

}
