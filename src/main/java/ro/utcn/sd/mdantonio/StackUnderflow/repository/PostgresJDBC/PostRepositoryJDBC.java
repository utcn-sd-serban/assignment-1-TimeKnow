package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJDBC;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Post;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("ALL")
@RequiredArgsConstructor
public class PostRepositoryJDBC implements PostRepository {

    private final JdbcTemplate template;

    @Override
    public List<Post> findAll() {
        return template.query("SELECT * FROM post", new PostMapper());
    }

    @Override
    public Post save(Post element) {
        if (element.getPostid() == null) {
            element.setPostid(insert(element));
        } else {
            update(element);
        }
        return element;
    }

    @Override
    public void remove(Post element) {
        template.update("DELETE FROM post WHERE postid = ?", element.getPostid());
    }

    @Override
    public Optional<Post> findById(int id) {
        List<Post> posts = template.query("SELECT * FROM post WHERE postid = ?", new PostMapper(), id);
        return posts.isEmpty() ? Optional.empty() : Optional.of(posts.get(0));
    }


    private int insert(Post element){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("post");
        insert.usingGeneratedKeyColumns("postid");
        Map<String, Object> map = new HashMap<>();
        map.put("posttypeid", element.getPosttypeid());
        map.put("authorid", element.getAuthorid());
        map.put("parentid", element.getParentid());
        map.put("title",element.getTitle());
        map.put("body", element.getBody());
        map.put("creationdate", element.getCreationDate());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(Post element) {
        template.update("UPDATE post SET posttypeid = ?, authorid = ?, parentid = ?, " +
                            "title = ?, body = ?, creationdate = ?  WHERE postid = ?",
                element.getPosttypeid(), element.getAuthorid(), element.getParentid(), element.getTitle(),
                element.getBody(), element.getCreationDate(), element.getPostid());
    }
}
