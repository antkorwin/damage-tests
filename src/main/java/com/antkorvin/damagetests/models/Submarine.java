package com.antkorvin.damagetests.models;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Korovin Anatolii on 14.11.17.
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
public class Submarine extends BaseEntity {

    private String name;

    private int power;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "submarine")
    private List<Rocket> rocketList = new ArrayList<>();


    @Override
    public String toString() {
        return "Submarine{" +
               "id="+getId() +", "+
               "name='" + name + '\'' +
               ", power=" + power +
               '}';
    }
}
