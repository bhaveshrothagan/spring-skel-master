package com.cepheid.cloud.skel.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@ApiModel
public class Description {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long did;
    private String name;

    public Description() {
    }

    public Description(Long did, String name) {
        this.did = did;
        this.name = name;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
