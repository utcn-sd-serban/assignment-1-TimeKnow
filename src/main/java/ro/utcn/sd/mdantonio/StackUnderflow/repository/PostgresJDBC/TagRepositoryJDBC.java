package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJDBC;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Tag;
import ro.utcn.sd.mdantonio.StackUnderflow.repository.API.TagRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class TagRepositoryJDBC implements TagRepository {
    private final JdbcTemplate template;

    @Override
    public List<Tag> findAll() {
        return template.query("SELECT * FROM tag", new TagMapper());
    }

    @Override
    public Tag save(Tag element) {
        if (element.getTagid() == null) {
            element.setTagid(insert(element));
        } else {
            update(element);
        }
        return element;
    }

    @Override
    public void remove(Tag element) {
        template.update("DELETE FROM tag WHERE tagid = ?", element.getTagid());
    }

    @Override
    public Optional<Tag> findById(int id) {
        List<Tag> tagList = template.query("SELECT * FROM tag WHERE tagid = ?", new TagMapper(), id);
        return tagList.isEmpty() ? Optional.empty() : Optional.of(tagList.get(0));
    }


    private int insert(Tag element){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("tag");
        insert.usingGeneratedKeyColumns("tagid");
        Map<String, Object> map = new HashMap<>();
        map.put("tagid", element.getTagid());
        map.put("title", element.getTitle());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(Tag element) {
        template.update("UPDATE tag SET title = ?  WHERE tagid = ?",
                element.getTitle(), element.getTagid());
    }
}
