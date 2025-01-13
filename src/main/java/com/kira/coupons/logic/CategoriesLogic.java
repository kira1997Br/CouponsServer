package com.kira.coupons.logic;

import com.kira.coupons.dal.ICategoriesDal;
import com.kira.coupons.dto.Category;
import com.kira.coupons.entities.CategoriesEntity;
import com.kira.coupons.enums.ErrorType;
import com.kira.coupons.exceptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriesLogic {
    private ICategoriesDal categoriesDal;

    @Autowired
    public CategoriesLogic(ICategoriesDal categoriesDal) {
        this.categoriesDal = categoriesDal;
    }

    @Transactional
    public void createCategory(Category category, String userType) throws ServerException, RuntimeException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        validateCategory(category);
        CategoriesEntity categoriesEntity = convertCategoryToCategoryEntity(category);
        this.categoriesDal.save(categoriesEntity);
    }

    public void updateCategory(Category category, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }

        CategoriesEntity categoriesEntity = this.categoriesDal.findById(category.getId()).get();
        if (categoriesEntity == null) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }

        validateCategory(category);
        categoriesEntity.setName(category.getName());
        this.categoriesDal.save(categoriesEntity);
    }

    public Category getCategoryById(int id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        if (!this.categoriesDal.existsById(id)) {
            throw new ServerException(ErrorType.INVALID_CATEGORY_ID);
        }

        CategoriesEntity categoriesEntity = this.categoriesDal.findById(id).get();
        Category category = convertCategoryEntityToCategory(categoriesEntity);
        return category;
    }

    public Category findCategoryByName(String name) throws ServerException {
        CategoriesEntity categoriesEntity = this.categoriesDal.findCategoryByName(name);
        if (categoriesEntity == null) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        Category category = convertCategoryEntityToCategory(categoriesEntity);
        return category;
    }


    @Transactional
    public void deleteCategory(String userType, int id) throws ServerException, RuntimeException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        if (!this.categoriesDal.existsById(id)) {
            throw new ServerException(ErrorType.INVALID_CATEGORY_ID);
        }
        this.categoriesDal.deleteById(id);
    }

    public Page<Category> getListOfCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.categoriesDal.allCategory(pageable);
    }

    private List<Category> convertCategoryEntityToCategoryList(List<CategoriesEntity> categoriesEntities) {
        List<Category> categories = new ArrayList<>();
        for (CategoriesEntity categoriesEntity : categoriesEntities) {
            Category category = convertCategoryEntityToCategory(categoriesEntity);
            categories.add(category);
        }
        return categories;
    }

    private Category convertCategoryEntityToCategory(CategoriesEntity categoriesEntity) {
        Category category = new Category(categoriesEntity.getId(),
                categoriesEntity.getName());
        return category;
    }

    private CategoriesEntity convertCategoryToCategoryEntity(Category category) {
        CategoriesEntity categoriesEntity = new CategoriesEntity(category.getId(),
                category.getName());
        return categoriesEntity;
    }

    private void validateCategory(Category category) throws ServerException {
        if (category.getName() == null || category.getName().length() > 45) {
            throw new ServerException(ErrorType.INVALID_CATEGORY_NAME, category.toString());
        }
        if (this.categoriesDal.isCategoryNameNotUnique(category.getName())) {
            throw new ServerException(ErrorType.INVALID_CATEGORY_NAME);
        }
    }

    boolean isCategoryIdExists(int id) throws ServerException {
        return categoriesDal.existsById(id);
    }
}
