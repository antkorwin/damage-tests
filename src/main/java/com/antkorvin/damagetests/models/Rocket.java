package com.antkorvin.damagetests.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Created by Korovin Anatolii on 11.11.17.
 *
 * Rocket entity
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rocket extends BaseEntity {

    @Column(unique = true)
    private String name;

    private String launchCode;

    @Builder
    public Rocket(UUID id, String name, String launchCode){
        super(id);
        this.setName(name);
        this.setLaunchCode(launchCode);
    }
}
