package edu.java.database.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Embeddable
public class UsersLinkId implements Serializable {

    @NotNull
    @Column(name = "userid", nullable = false)
    private Long userid;

    @NotNull
    @Column(name = "linkid", nullable = false)
    private Long linkid;
}
