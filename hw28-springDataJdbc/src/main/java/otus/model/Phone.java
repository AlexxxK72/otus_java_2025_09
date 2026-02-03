package otus.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
@Data
@NoArgsConstructor
public class Phone {
    @Id
    private Long id;

    private String number;

    private Long clientId;
}
