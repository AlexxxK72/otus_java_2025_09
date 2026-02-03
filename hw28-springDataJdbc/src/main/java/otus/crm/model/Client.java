package otus.crm.model;

import jakarta.annotation.Nonnull;
import java.util.Set;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("client")
@Getter
public class Client implements Persistable<Long> {
    @Id
    @Nonnull
    private Long id;

    private final String name;

    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;

    @Transient
    private boolean isNew;

    public Client(Long id, String name, Address address, Set<Phone> phones, boolean isNew) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        this.isNew = isNew;
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this(id, name, address, phones, id == null);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Client{" + "id="
                + id + ", name='"
                + name + '\'' + ", address="
                + address + ", phones="
                + phones + ", isNew="
                + isNew + '}';
    }
}
