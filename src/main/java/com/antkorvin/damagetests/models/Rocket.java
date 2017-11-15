package com.antkorvin.damagetests.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rocket extends BaseEntity {

    @Column(unique = true)
    private String name;

    private String launchCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submarine_id")
    private Submarine submarine;

    @Builder
    public Rocket(UUID id, String name, String launchCode, Submarine submarine){
        super(id);
        this.setName(name);
        this.setLaunchCode(launchCode);
        this.setSubmarine(submarine);
    }


    @Override
    public String toString() {
        return "Rocket{" +
               "id="+getId() +", "+
               "name='" + name + '\'' +
               ", launchCode='" + launchCode + '\'' +
               '}';
    }
}
