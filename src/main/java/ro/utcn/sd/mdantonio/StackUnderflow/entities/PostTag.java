package ro.utcn.sd.mdantonio.StackUnderflow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="post_tag")
public class PostTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer posttagid;
    private Integer postid;
    private Integer tagid;

    public PostTag(Integer postid, Integer tagid) {
        this.postid = postid;
        this.tagid = tagid;
    }
}
