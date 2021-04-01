package com.web.training.repositories;

import com.web.training.models.entities.Training;
import com.web.training.models.enums.StatusType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    Optional<Training> getByIdAndUserId(Long trainingId, Long userId);

    List<Training> getAllByUserId(Long id);

    Optional<Integer> countAllByUserIdAndStatusType(Long userId, StatusType statusType);

    List<Training> findAllByUserId(Long userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Training as t set t.statusType = :status where t.fromDate <= :date")
    void updateTrainingStatusFromDate(@Param("status") StatusType statusType, @Param("date") LocalDate localDate);

    @Transactional
    @Modifying
    @Query("update Training as t set t.statusType = :status where t.toDate < :date")
    void updateTrainingStatusToDate(@Param("status") StatusType statusType, @Param("date") LocalDate localDate);

    List<Training> getTop5ByTitleStartsWithAndIdNotInAndUser_Id(String input, List<Long> notIds, Long userId);

    List<Training> getTop5ByTitleStartsWithAndUser_Id(String input, Long userId);

    Optional<Integer> countAllByUserIdAndTitleStartingWith(Long userId, String title);
}
