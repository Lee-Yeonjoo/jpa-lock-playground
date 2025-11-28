package study.crud.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import study.crud.domain.Item;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Boolean existsByCode(String code);

    @Query("select i from Item i where i.id = :id")
    Optional<Item> findByIdWithOptimisticLock(Long id);

    //네임드 락 설정
    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    void getLock(String key);  //key = 락의 이름

    //네임드 락 해제
    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(String key);
}
