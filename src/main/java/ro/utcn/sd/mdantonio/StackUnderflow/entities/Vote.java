package ro.utcn.sd.mdantonio.StackUnderflow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voteid;
    private Integer userid;
    private Integer postid;
    private boolean upvote;

    public Vote(Integer userid, Integer postid, boolean upvote) {
        this.userid = userid;
        this.postid = postid;
        this.upvote = upvote;
    }
}
