package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJDBC;

import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.Vote;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteMapper implements RowMapper<Vote> {
    @Override
    public Vote mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Vote(resultSet.getInt("voteid"), resultSet.getInt("userid"),
                resultSet.getInt("postid"), resultSet.getBoolean("upvote"));
    }
}
