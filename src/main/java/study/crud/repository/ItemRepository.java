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

    //락을 걸기 위해 조회 쿼리를 별도로 생성
    @Lock(LockModeType.OPTIMISTIC)  //낙관적 락
    @Query("select i from Item i where i.id = :id")
    Optional<Item> findByIdWithOptimisticLock(Long id);
}
