package ao.sibs.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_items")
public class Item {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "id",  columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String name;
}
