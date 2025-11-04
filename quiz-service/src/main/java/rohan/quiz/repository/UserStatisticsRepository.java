package rohan.quiz.repository;


import rohan.quiz.entity.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {

    Optional<UserStatistics> findByUserId(Long userId);

    Optional<UserStatistics> findByUsername(String username);

    boolean existsByUserId(Long userId);
}