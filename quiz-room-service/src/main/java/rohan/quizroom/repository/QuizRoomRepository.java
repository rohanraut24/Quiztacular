package rohan.quizroom.repository;

import rohan.quizroom.entity.QuizRoom;
import rohan.quizroom.entity.QuizRoom.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRoomRepository extends JpaRepository<QuizRoom, Long> {

    Optional<QuizRoom> findByRoomCode(String roomCode);

    boolean existsByRoomCode(String roomCode);

    List<QuizRoom> findByStatus(RoomStatus status);

    List<QuizRoom> findByStatusAndCreatedAtBefore(RoomStatus status, LocalDateTime dateTime);

    List<QuizRoom> findByHostUserId(Long hostUserId);

    Long countByStatus(RoomStatus status);
}