package com.web.training.repositories;

import com.web.training.models.entities.Weight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightRepository extends JpaRepository<Weight, Long> {

    List<Weight> findAllByUserId(Long userId);

    List<Weight> getTop2ByUserIdOrderByDateDesc(Long userId);

    Weight getTop1ByUser_IdOrderByWeightDesc(Long userId);

    Weight getTop1ByUser_IdOrderByWeightAsc(Long userId);
}
