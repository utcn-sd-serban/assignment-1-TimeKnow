package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJDBC;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.PostTag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("ALL")
@RequiredArgsConstructor
public class PostTagRepositoryJDBC implements ro.utcn.sd.mdantonio.StackUnderflow.repository.API.PostTagRepository {
    private final JdbcTemplate template;

    @Override
    public List<PostTag> findAll() {
        return template.query("SELECT * FROM post_tag", new PostTagMapper());
    }

    @Override
    public PostTag save(PostTag element) {
        if (element.getPosttagid() == null) {
            element.setPosttagid(insert(element));
        } else {
            update(element);
        }
        return element;
    }

    @Override
    public void remove(PostTag element) {
        template.update("DELETE FROM post_tag WHERE posttagid = ?", element.getPosttagid());
    }

    @Override
    public Optional<PostTag> findById(int id) {
        List<PostTag> postTagList = template.query("SELECT * FROM post_tag WHERE posttagid = ?", new PostTagMapper(), id);
        return postTagList.isEmpty() ? Optional.empty() : Optional.of(postTagList.get(0));
    }


    private int insert(PostTag element){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("post_tag");
        insert.usingGeneratedKeyColumns("posttagid");
        Map<String, Object> map = new HashMap<>();
        map.put("posttagid", element.getPosttagid());
        map.put("postid", element.getPostid());
        map.put("tagid", element.getTagid());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(PostTag element) {
        template.update("UPDATE post_tag SET postid = ?, tagid = ?  WHERE posttagid = ?",
                element.getPosttagid(), element.getPostid(), element.getTagid());
    }
}
