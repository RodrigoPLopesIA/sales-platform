package br.com.rodrigolopesdev.product_api.services;

import br.com.rodrigolopesdev.product_api.dtos.ListProductDTO;
import br.com.rodrigolopesdev.product_api.dtos.ProductFilterDTO;
import br.com.rodrigolopesdev.product_api.models.Product;
import br.com.rodrigolopesdev.product_api.repository.ProductRepository;
import br.com.rodrigolopesdev.product_api.repository.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductService {


    @Autowired
    private ProductRepository productRepository;


    public Page<ListProductDTO> find(Pageable pageable, ProductFilterDTO filter){
        Specification<Product> spec = ProductSpecification.conjunction();

        if (filter.name() != null)
            spec.and(ProductSpecification.withName(filter.name()));

        if(filter.sku() != null)
            spec.and(ProductSpecification.withSKU(filter.sku()));

        if (filter.categoryName() != null)
            spec.and(ProductSpecification.withCategoryName(filter.categoryName()));

        if(filter.supplierName() != null)
            spec.and(ProductSpecification.withSupplierName(filter.supplierName()));

        if(filter.minPrice() != null)
            spec.and(ProductSpecification.withMinPrice(filter.minPrice()));

        if(filter.maxPrice() != null)
            spec.and(ProductSpecification.withMaxPrice(filter.maxPrice()));

        if(filter.isActive() != null)
            spec.and(ProductSpecification.isActive(filter.isActive()));

        if(filter.inStockOnly() != null)
            spec.and(ProductSpecification.inStock());

        return this.productRepository.findAll(spec, pageable).map(ListProductDTO::new);
    }
}
