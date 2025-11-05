package rohan.quizroom.repository;

import rohan.quizroom.entity.RoomAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomAnswerRepository extends JpaRepository<RoomAnswer, Long> {

    List<RoomAnswer> findByRoomIdOrderByAnsweredAtAsc(Long roomId);

    List<RoomAnswer> findByRoomIdAndQuestionNumber(Long roomId, Integer questionNumber);

    List<RoomAnswer> findByRoomIdAndUserId(Long roomId, Long userId);

    boolean existsByRoomIdAndUserIdAndQuestionNumber(Long roomId, Long userId, Integer questionNumber);
}