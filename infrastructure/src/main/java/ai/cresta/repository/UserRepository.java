package ai.cresta.repository;

import ai.cresta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @Query("select u from User u where u.userId = :userId")
    List<User> findByUserId(@Param("userId") String userId);

    @Query("select u from User u where u.conversationId = :conversationId")
    List<User> findByConversationId(@Param("conversationId") String conversationId);
}
