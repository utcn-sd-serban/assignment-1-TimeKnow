package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJDBC;

import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.PostType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostTypeMapper implements RowMapper<PostType> {
    @Override
    public PostType mapRow(ResultSet resultSet, int i) throws SQLException {
        return new PostType(resultSet.getInt("posttypeid"), resultSet.getString("title"));
    }
}
