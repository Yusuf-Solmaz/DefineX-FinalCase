package com.yms.projectservice.repository;

import com.yms.projectservice.dto.ProjectDto;
import com.yms.projectservice.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Integer> {

    @Query("SELECT p.teamMemberIds FROM Project p WHERE p.id = :projectId")
    List<Integer> findMemberIdsByProjectId(@Param("projectId") Integer projectId);

    List<Project> findAllByDepartmentName(String name);

    @Query("SELECT p FROM Project p WHERE p.isDeleted = false")
    Page<Project> findAllActives(Pageable pageable);
}
