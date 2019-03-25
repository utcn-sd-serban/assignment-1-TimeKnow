package ro.utcn.sd.mdantonio.StackUnderflow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="posttype")
public class PostType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer posttypeid;
    private String title;

    public PostType(String title) {
        this.title = title;
    }
}
