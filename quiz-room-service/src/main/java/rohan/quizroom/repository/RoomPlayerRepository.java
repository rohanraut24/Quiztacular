package rohan.quizroom.repository;


import rohan.quizroom.entity.RoomPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomPlayerRepository extends JpaRepository<RoomPlayer, Long> {

    List<RoomPlayer> findByRoomId(Long roomId);

    Optional<RoomPlayer> findByRoomIdAndUserId(Long roomId, Long userId);

    Optional<RoomPlayer> findByRoomIdAndUsername(Long roomId, String username);

    boolean existsByRoomIdAndUserId(Long roomId, Long userId);

    void deleteByRoomId(Long roomId);

    @Query("SELECT p FROM RoomPlayer p WHERE p.room.id = ?1 ORDER BY p.currentScore DESC, p.correctAnswers DESC")
    List<RoomPlayer> findByRoomIdOrderByScore(Long roomId);

    Long countByRoomId(Long roomId);
}