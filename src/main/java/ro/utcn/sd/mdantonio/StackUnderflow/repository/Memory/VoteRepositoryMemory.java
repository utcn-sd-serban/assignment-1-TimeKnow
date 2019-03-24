package ro.utcn.sd.mdantonio.StackUnderflow.repository.Memory;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Vote;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.VoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class VoteRepositoryMemory implements VoteRepository {
    private final Map<Integer, Vote> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);
    @Override
    public List<Vote> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Vote save(Vote element) {
        if(element.getVoteid()==null){
            element.setVoteid(currentId.incrementAndGet());
        }
        data.put(element.getVoteid(), element);
        return element;
    }

    @Override
    public void remove(Vote element) {
        data.remove(element.getVoteid());
    }

    @Override
    public Optional<Vote> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }
}
