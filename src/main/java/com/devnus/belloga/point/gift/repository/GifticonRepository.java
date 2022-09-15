package com.devnus.belloga.point.gift.repository;

import com.devnus.belloga.point.gift.domain.Gifticon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {
}
