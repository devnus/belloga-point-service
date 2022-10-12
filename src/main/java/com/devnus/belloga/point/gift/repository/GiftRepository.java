package com.devnus.belloga.point.gift.repository;

import com.devnus.belloga.point.gift.domain.Gift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    Page<Gift> findAll(Pageable pageable);
    @EntityGraph(value = "Gift.fetchGifticon", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select g from Gift g where id = :id")
    Optional<Gift> findByIdFetchGifticon(@Param("id") Long id);
}
