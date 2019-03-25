package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJDBC;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Vote;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.VoteRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class VoteRepositoryJDBC implements VoteRepository {
    private final JdbcTemplate template;

    @Override
    public List<Vote> findAll() {
        return template.query("SELECT * FROM vote", new VoteMapper());
    }

    @Override
    public Vote save(Vote element) {
        if (element.getVoteid() == null) {
            element.setVoteid(insert(element));
        } else {
            update(element);
        }
        return element;
    }

    @Override
    public void remove(Vote element) {
        template.update("DELETE FROM vote WHERE voteid = ?", element.getVoteid());
    }

    @Override
    public Optional<Vote> findById(int id) {
        List<Vote> voteList = template.query("SELECT * FROM vote WHERE voteid = ?", new VoteMapper(), id);
        return voteList.isEmpty() ? Optional.empty() : Optional.of(voteList.get(0));
    }

    private int insert(Vote element){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("vote");
        insert.usingGeneratedKeyColumns("voteid");
        Map<String, Object> map = new HashMap<>();
        map.put("userid", element.getUserid());
        map.put("postid", element.getPostid());
        map.put("upvote", element.isUpvote());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(Vote element) {
        template.update("UPDATE vote SET userid = ?, postid = ?, upvote = ?  WHERE voteid = ?",
                element.getUserid(), element.getPostid(), element.isUpvote(), element.getVoteid());
    }
}
