package com.yms.projectservice.repository;

import com.yms.projectservice.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("SELECT p FROM Project p WHERE :memberId MEMBER OF p.teamMemberIds")
    List<Project> findByTeamMemberId(@Param("memberId") Long memberId);
}
