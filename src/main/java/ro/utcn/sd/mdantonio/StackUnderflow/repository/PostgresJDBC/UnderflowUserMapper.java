package ro.utcn.sd.mdantonio.StackUnderflow.repository.PostgresJDBC;

import org.springframework.jdbc.core.RowMapper;
import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UnderflowUserMapper implements RowMapper<UnderflowUser> {
    @Override
    public UnderflowUser mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UnderflowUser(resultSet.getInt("userid"), resultSet.getString("username"),
                resultSet.getString("password"), resultSet.getString("email"),
                resultSet.getBoolean("banned"), resultSet.getString("permission"));
    }
}
