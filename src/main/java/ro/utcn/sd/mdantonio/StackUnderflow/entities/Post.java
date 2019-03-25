package ro.utcn.sd.mdantonio.StackUnderflow.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name="post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postid;
    private Integer posttypeid;
    private Integer authorid;
    private Integer parentid;
    private String title;
    private String body;

    @Column(name="creationdate")
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Transient
    private long score;

    public Post(Integer postid, Integer posttypeid, Integer authorid, Integer parentid, String title, String body, Date creationDate) {
        this.postid = postid;
        this.posttypeid = posttypeid;
        this.authorid = authorid;
        this.parentid = parentid;
        this.title = title;
        this.body = body;
        this.creationDate = creationDate;
    }

    public Post(Integer posttypeid, Integer authorid, Integer parentid, String title, String body, Date creationDate) {
        this.posttypeid = posttypeid;
        this.authorid = authorid;
        this.parentid = parentid;
        this.title = title;
        this.body = body;
        this.creationDate = creationDate;
    }
}
