package com.kira.coupons.dal;

import com.kira.coupons.dto.Category;
import com.kira.coupons.entities.CategoriesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriesDal extends JpaRepository<CategoriesEntity, Integer> {
    @Query("select count(c.id)>0 from CategoriesEntity c where name = :name")
    boolean isCategoryNameNotUnique(@Param("name") String categoryName);

    @Query("SELECT c FROM CategoriesEntity c WHERE c.name = :name")
    CategoriesEntity findCategoryByName(@Param("name") String name);

    @Query("SELECT new com.kira.coupons.dto.Category(c.id,c.name) FROM CategoriesEntity c")
    Page<Category> allCategory(Pageable pageable);
}
