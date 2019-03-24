package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJDBC;

import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.PostTag;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostTagMapper implements RowMapper<PostTag> {
    @Override
    public PostTag mapRow(ResultSet resultSet, int i) throws SQLException {
        return new PostTag(resultSet.getInt("posttagid"), resultSet.getInt("postid"), resultSet.getInt("tagid"));
    }
}
