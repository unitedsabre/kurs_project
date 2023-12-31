package urfu.test_kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import urfu.test_kurs.entity.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
