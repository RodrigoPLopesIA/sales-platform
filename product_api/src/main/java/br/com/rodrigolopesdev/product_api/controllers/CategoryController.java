package br.com.rodrigolopesdev.product_api.controllers;

import br.com.rodrigolopesdev.product_api.dtos.ListCategoryDTO;
import br.com.rodrigolopesdev.product_api.dtos.SaveCategoryDTO;
import br.com.rodrigolopesdev.product_api.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private  CategoryService categoryService;


    @GetMapping
    public ResponseEntity<Page<ListCategoryDTO>> listAll(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {

        Page<ListCategoryDTO> categories = categoryService.find(pageable, name);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListCategoryDTO> getById(@PathVariable String id) {
        ListCategoryDTO category = categoryService.findById(UUID.fromString(id));
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<ListCategoryDTO> create(
            @RequestBody @Valid SaveCategoryDTO dto,
            UriComponentsBuilder uriBuilder) {

        ListCategoryDTO category = categoryService.create(dto);
        URI uri = uriBuilder.path("/api/v1/categories/{id}").buildAndExpand(category.id()).toUri();
        return ResponseEntity.created(uri).body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListCategoryDTO> update(
            @PathVariable String id,
            @RequestBody @Valid SaveCategoryDTO dto) {

        ListCategoryDTO updatedCategory = categoryService.update(UUID.fromString(id), dto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        categoryService.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.existsById(UUID.fromString(id)));
    }
}