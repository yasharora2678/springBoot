package jpql.query.Entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "user_details")
@Entity
@Data
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name")
    private String name;
    private String phone;

    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "user_address")
    // private UserAddress userAddress;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @BatchSize(size = 10) // It will be used to fetch in batches the users
    private List<UserAddress> userAddress = new ArrayList<>();
}
