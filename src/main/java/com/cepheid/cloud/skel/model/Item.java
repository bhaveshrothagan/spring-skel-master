package com.cepheid.cloud.skel.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@ApiModel
public class Item extends AbstractEntity {

    private String name;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = Description.class, cascade = CascadeType.ALL)
    private List<Description> description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Description> getDescription() {
        if (description == null) {
            return new ArrayList<Description>();
        }
        return description;
    }

    public void setDescription(List<Description> description) {
        this.description = description;
    }
}
