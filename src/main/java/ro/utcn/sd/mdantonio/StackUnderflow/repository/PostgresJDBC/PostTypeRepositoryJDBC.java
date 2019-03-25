package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJDBC;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.PostType;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostTypeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class PostTypeRepositoryJDBC implements PostTypeRepository {
    private final JdbcTemplate template;

    @Override
    public List<PostType> findAll() {
        return template.query("SELECT * FROM posttype", new PostTypeMapper());
    }

    @Override
    public PostType save(PostType element) {
        if (element.getPosttypeid() == null) {
            element.setPosttypeid(insert(element));
        } else {
            update(element);
        }
        return element;
    }

    @Override
    public void remove(PostType element) {
        template.update("DELETE FROM posttype WHERE posttypeid = ?", element.getPosttypeid());
    }

    @Override
    public Optional<PostType> findById(int id) {
        List<PostType> postTypeList = template.query("SELECT * FROM posttype WHERE posttypeid = ?", new PostTypeMapper(), id);
        return postTypeList.isEmpty() ? Optional.empty() : Optional.of(postTypeList.get(0));
    }

    private int insert(PostType element){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("posttype");
        insert.usingGeneratedKeyColumns("posttypeid");
        Map<String, Object> map = new HashMap<>();
        map.put("posttypeid", element.getPosttypeid());
        map.put("title", element.getTitle());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(PostType element) {
        template.update("UPDATE posttype SET title = ?  WHERE posttypeid = ?",
                element.getTitle(), element.getPosttypeid());
    }
}
