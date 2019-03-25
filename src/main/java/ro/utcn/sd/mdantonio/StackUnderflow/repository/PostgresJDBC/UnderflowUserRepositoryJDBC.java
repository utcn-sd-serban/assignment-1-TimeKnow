package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJDBC;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.UnderflowUserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UnderflowUserRepositoryJDBC implements UnderflowUserRepository {
    private final JdbcTemplate template;

    @Override
    public Optional<UnderflowUser> findByUsername(String username) {
        List<UnderflowUser> underflowUserList = template.query("SELECT * FROM underflowuser WHERE username = ?",
                new UnderflowUserMapper(), username);
        return underflowUserList.isEmpty() ? Optional.empty() : Optional.of(underflowUserList.get(0));
    }

    @Override
    public List<UnderflowUser> findAll() {
        return template.query("SELECT * FROM underflowuser", new UnderflowUserMapper());
    }

    @Override
    public UnderflowUser save(UnderflowUser element) {
        if (element.getUserid() == null) {
            element.setUserid(insert(element));
        } else {
            update(element);
        }
        return element;
    }

    @Override
    public void remove(UnderflowUser element) {
        template.update("DELETE FROM underflowuser WHERE userid = ?", element.getUserid());
    }

    @Override
    public Optional<UnderflowUser> findById(int id) {
        List<UnderflowUser> underflowUserList = template.query("SELECT * FROM underflowuser WHERE userid = ?",
                new UnderflowUserMapper(), id);
        return underflowUserList.isEmpty() ? Optional.empty() : Optional.of(underflowUserList.get(0));
    }

    private int insert(UnderflowUser element){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("underflowuser");
        insert.usingGeneratedKeyColumns("userid");
        Map<String, Object> map = new HashMap<>();
        map.put("username", element.getUsername());
        map.put("password", element.getPassword());
        map.put("email", element.getEmail());
        map.put("banned", element.isBanned());
        map.put("permission", element.getPermission());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(UnderflowUser element) {
        template.update("UPDATE underflowuser SET username = ?, password = ?, email = ?, banned = ?," +
                        " permission = ?  WHERE userid = ?",
                element.getUsername(), element.getPassword(), element.getEmail(), element.isBanned(),
                element.getPermission(), element.getUserid());
    }
}
