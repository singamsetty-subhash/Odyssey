package com.ecell.odyssey;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, String> {
    Optional<Registration> findById(String id);
    List<Registration> findAllByOrderByRegisteredAtDesc();
    long countByTrack(String track);
    boolean existsByTeamNameIgnoreCase(String teamName);
}
