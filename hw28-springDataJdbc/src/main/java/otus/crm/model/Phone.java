package otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
    private @Id Long id;
    private String number;

    @Setter
    private Long clientId;
}
