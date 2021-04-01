package com.web.training.repositories;

import com.web.training.models.entities.Day;
import com.web.training.models.enums.DayStatus;
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
public interface DayRepository extends JpaRepository<Day, Long> {

    @Transactional
    @Modifying
    @Query("update Day as d set d.status = :status where d.date < :date")
    void updateDayStatusCompleted(@Param("status") DayStatus statusType, @Param("date") LocalDate localDate);

    @Transactional
    @Modifying
    @Query("update Day as d set d.status = :status where d.date = :date")
    void updateDayStatusToDateInProgress(@Param("status") DayStatus statusType, @Param("date") LocalDate localDate);

    Optional<Day> findByIdAndTrainingIdAndTraining_UserId(Long dayId, Long trainingId, Long userId);

    @Transactional
    @Modifying
    void deleteAllByTrainingIdAndIdNotIn(Long trainingId, List<Long> ids);

    @Transactional
    @Modifying
    void deleteAllByTrainingId(Long trainingId);
}
