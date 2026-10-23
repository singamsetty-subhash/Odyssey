package com.ecell.odyssey;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentResponseRepository extends JpaRepository<StudentResponse, Long> {
    List<StudentResponse> findAllByOrderBySubmittedAtDesc();
    List<StudentResponse> findByDeptIgnoreCaseOrderBySubmittedAtDesc(String dept);
    long count();
}
