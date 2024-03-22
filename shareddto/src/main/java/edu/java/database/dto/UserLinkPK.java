package edu.java.database.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserLinkPK implements Serializable {
    @Column(nullable = false)
    private Long userid;
    @Column(nullable = false)
    private Long linkid;
}
