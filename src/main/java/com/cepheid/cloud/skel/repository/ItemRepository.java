package com.cepheid.cloud.skel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cepheid.cloud.skel.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;


public interface ItemRepository extends JpaRepository<Item, Long> {
}
