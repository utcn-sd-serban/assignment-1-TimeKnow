package ro.utcn.sd.mdantonio.StackUnderflow.repository.Memory;

import ro.utcn.sd.mdantonio.StackUnderflow.entities.Tag;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TagRepositoryMemory implements TagRepository {
    private final Map<Integer, Tag> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<Tag> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Tag save(Tag element) {
        if(element.getTagid()==null){
            element.setTagid(currentId.incrementAndGet());
        }
        data.put(element.getTagid(), element);
        return element;
    }

    @Override
    public void remove(Tag element) {
        data.remove(element.getTagid());
    }

    @Override
    public Optional<Tag> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }
}
