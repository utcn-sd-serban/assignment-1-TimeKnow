package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJDBC;

import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Post;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet resultSet, int i) throws SQLException {

        return new Post(resultSet.getInt("postid"),
                resultSet.getInt("posttypeid"),
                resultSet.getInt("authorid"),
                resultSet.getInt("parentid"),
                resultSet.getString("title"),
                resultSet.getString("body"),
                resultSet.getDate("creationdate"));
    }
}
