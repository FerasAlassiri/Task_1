package com.example.task_1.repository;

import com.example.task_1.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a where a.disabled = false") // fetch only active articles
    Page<Article> findEnabled(Pageable pageable);
}
