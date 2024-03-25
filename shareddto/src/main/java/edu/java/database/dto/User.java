package edu.java.database.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString

@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = "telegramId")
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegramid", nullable = false)
    private Long telegramId;

}
