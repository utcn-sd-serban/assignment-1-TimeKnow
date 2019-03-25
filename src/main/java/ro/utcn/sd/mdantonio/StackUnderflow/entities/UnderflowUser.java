package ro.utcn.sd.mdantonio.StackUnderflow.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@NoArgsConstructor
@Table(name = "underflowuser")
public class UnderflowUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;
    private String username;
    private String password;
    private String email;
    private boolean banned;
    private String permission;

    @Transient
    private long score;

    public UnderflowUser(Integer userid, String username, String password, String email, boolean banned, String permission) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.banned = banned;
        this.permission = permission;
    }

    public UnderflowUser(String username, String password, String email, boolean banned, String permission) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.banned = banned;
        this.permission = permission;
    }
}
