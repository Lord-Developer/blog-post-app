package lord.dev.repository;

import lord.dev.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsById(Long id);
    boolean existsByTitle(String title);
}
