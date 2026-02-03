package otus.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
@Data
@NoArgsConstructor
public class Address {
    @Id
    private Long clientId;

    private String street;
}
