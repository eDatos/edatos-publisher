package es.gobcan.istac.edatos.publisher.rest.internal.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.gobcan.istac.edatos.publisher.core.domain.CategoryEntity;
import es.gobcan.istac.edatos.publisher.core.domain.ExternalCategoryEntity;
import es.gobcan.istac.edatos.publisher.core.domain.ExternalOperationEntity;
import es.gobcan.istac.edatos.publisher.core.service.CategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.FavoriteService;
import es.gobcan.istac.edatos.publisher.rest.common.dto.CategoryDto;
import es.gobcan.istac.edatos.publisher.rest.common.dto.ExternalCategoryDto;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.CategoryMapper;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.ExternalCategoryMapper;
import es.gobcan.istac.edatos.publisher.rest.common.util.HeaderUtil;
import es.gobcan.istac.edatos.publisher.rest.common.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping(CategoryResource.BASE_URL)
public class CategoryResource extends AbstractResource {

    public static final String ENTITY_NAME = "category";
    public static final String BASE_URL = "/api/categories";

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final ExternalCategoryService externalCategoryService;
    private final ExternalCategoryMapper externalCategoryMapper;
    private final ExternalOperationService externalOperationService;
    private final FavoriteService favoriteService;

    // TODO(EDATOS-3357): Add audits

    public CategoryResource(CategoryService categoryService, CategoryMapper categoryMapper, ExternalCategoryService externalCategoryService, ExternalCategoryMapper externalCategoryMapper,
            ExternalOperationService externalOperationService, FavoriteService favoriteService) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.externalCategoryService = externalCategoryService;
        this.externalCategoryMapper = externalCategoryMapper;
        this.externalOperationService = externalOperationService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/{id}")
    @Timed
    @PreAuthorize("@secChecker.canAccessCategory(authentication)")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryEntity category = categoryService.findCategoryById(id);
        CategoryDto externalCategoryDto = categoryMapper.toDto(category);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(externalCategoryDto));
    }

    @GetMapping
    @Timed
    @PreAuthorize("@secChecker.canAccessCategory(authentication)")
    public ResponseEntity<List<CategoryDto>> getCategory(Pageable pageable, @RequestParam(required = false) String query) {
        Page<CategoryDto> result = categoryService.find(query, pageable).map(categoryMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(result, BASE_URL);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/external-categories")
    @Timed
    @PreAuthorize("@secChecker.canAccessCategory(authentication)")
    public ResponseEntity<List<ExternalCategoryDto>> getExternalCategories(Pageable pageable, @RequestParam(required = false) String search) {
        Page<ExternalCategoryDto> result = externalCategoryService.requestExternalCategories(pageable, search).map(externalCategoryMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(result, BASE_URL);
         return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @Timed
    @GetMapping("/tree")
    @PreAuthorize("@secChecker.canAccessCategory(authentication)")
    public ResponseEntity<List<CategoryDto>> getCategoryTree() {
        List<CategoryEntity> tree = categoryService.getTree();
        List<CategoryEntity> all = categoryService.findAll();
        Map<CategoryEntity, List<ExternalCategoryEntity>> externalCategories = categoryService.getCategoryExternalCategories();
        List<ExternalOperationEntity> externalOperations = externalOperationService.findAll();
        Map<String, Long> suscribers = new HashMap<>();
        suscribers.putAll(favoriteService.getCategorySubscribers());
        suscribers.putAll(favoriteService.getOperationSubscribers());
        return ResponseEntity.ok(categoryMapper.toDtos(all, tree, externalCategories, externalOperations, suscribers));
    }

    @Timed
    @PutMapping("/tree")
    @PreAuthorize("@secChecker.canUpdateCategory(authentication)")
    public ResponseEntity<List<CategoryDto>> updateCategory(@RequestBody List<CategoryDto> category) {
        List<CategoryEntity> categoryEntity = categoryMapper.toEntities(category);
        List<CategoryDto> response = categoryMapper.toDtos(categoryService.updateTree(categoryEntity));
        return ResponseEntity.ok(response);
    }

    @Timed
    @DeleteMapping("{id}")
    @PreAuthorize("@secChecker.canDeleteCategory(authentication)")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
