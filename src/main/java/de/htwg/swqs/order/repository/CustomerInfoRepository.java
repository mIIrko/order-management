package de.htwg.swqs.order.repository;

import de.htwg.swqs.order.model.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Long> {

    Optional<CustomerInfo> findByEmail(String email);
}
