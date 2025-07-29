package br.com.rodrigolopesdev.product_api.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "categories")
@EntityListeners(AuditingEntityListener.class)
public class Category {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;


    @OneToMany(mappedBy = "category")
    private List<Product> categorizedProducts;

    @CreatedDate()
    private Instant createdAt;


    @LastModifiedDate()
    private Instant updatedAt;

}
