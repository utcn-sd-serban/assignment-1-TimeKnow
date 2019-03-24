package ro.utcn.sd.mdantonio.StackUnderflow.repository.Memory;

import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.UnderflowUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UnderflowUserRepositoryMemory implements UnderflowUserRepository {
    private final Map<Integer, UnderflowUser> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<UnderflowUser> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public UnderflowUser save(UnderflowUser element) {
        if(element.getUserid()==null){
            element.setUserid(currentId.incrementAndGet());
        }
        data.put(element.getUserid(), element);
        return element;
    }

    @Override
    public void remove(UnderflowUser element) {
        data.remove(element.getUserid());
    }

    @Override
    public Optional<UnderflowUser> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<UnderflowUser> findByUsername(String username) {
        return data.values().stream().filter(x->x.getUsername().equals(username)).findFirst();
    }
}
