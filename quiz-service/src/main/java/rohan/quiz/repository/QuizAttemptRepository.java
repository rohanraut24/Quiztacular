package rohan.quiz.repository;

import rohan.quiz.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    List<QuizAttempt> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<QuizAttempt> findByUsernameOrderByCreatedAtDesc(String username);

    List<QuizAttempt> findByCategoryOrderByScoreDesc(String category);

    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.category = ?1 ORDER BY qa.score DESC, qa.timeTakenSeconds ASC")
    List<QuizAttempt> findTopScoresByCategory(String category);

    @Query("SELECT qa FROM QuizAttempt qa ORDER BY qa.score DESC, qa.timeTakenSeconds ASC")
    List<QuizAttempt> findGlobalLeaderboard();

    Long countByUserId(Long userId);
}
