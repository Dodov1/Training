package com.web.training.repositories;

import com.web.training.models.entities.Trainer;
import com.web.training.models.entities.User;
import com.web.training.models.enums.RelationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Optional<Trainer> getByIdAndStatus(Long trainerId, RelationStatus status);

    List<Trainer> getAllByStatus(RelationStatus status);

    Optional<Trainer> getByUser(User user);

    Optional<Trainer> getByUser_UsernameAndStatus(String username, RelationStatus status);

    List<Trainer> getTop5ByUser_UsernameContainingAndIdNotInAndStatus(String input, List<Long> notIds, RelationStatus status);

    List<Trainer> getTop5ByUser_UsernameContainingAndStatus(String input, RelationStatus status);

}
