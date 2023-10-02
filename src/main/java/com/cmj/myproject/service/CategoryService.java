package com.cmj.myproject.service;

import com.cmj.myproject.domain.Category;
import com.cmj.myproject.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public Category findCategoryByName(String name) {

        try {
            return categoryRepository.findByName(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("카테고리가 존재하지 않습니다.");
        }

    }


}
