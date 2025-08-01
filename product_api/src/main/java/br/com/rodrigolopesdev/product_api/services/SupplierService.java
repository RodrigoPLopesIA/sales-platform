package br.com.rodrigolopesdev.product_api.services;

import br.com.rodrigolopesdev.product_api.dtos.ListSupplierDTO;
import br.com.rodrigolopesdev.product_api.dtos.SaveSupplierDTO;
import br.com.rodrigolopesdev.product_api.models.Supplier;
import br.com.rodrigolopesdev.product_api.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;
    
    public Page<ListSupplierDTO> find(Pageable pageable, String name) {

        Supplier exampleSupplier = Supplier.builder().name(name).build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Example<Supplier> example = Example.of(exampleSupplier, matcher);

        return supplierRepository.findAll(example, pageable).map(ListSupplierDTO::new);
    }

    public ListSupplierDTO findById(UUID id) {
        Supplier Supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
        return new ListSupplierDTO(Supplier);
    }


    public ListSupplierDTO create(SaveSupplierDTO dto) {
        if (supplierRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Supplier already exists with name: " + dto.name());
        }

        Supplier supplier = Supplier.builder().name(dto.name()).build();

        Supplier savedSupplier = supplierRepository.save(supplier);
        return new ListSupplierDTO(savedSupplier);
    }

    public ListSupplierDTO update(UUID id, SaveSupplierDTO dto) {
        Supplier Supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));

        if (!Supplier.getName().equals(dto.name()) &&
                supplierRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Supplier already exists with name: " + dto.name());
        }

        Supplier.setName(dto.name());
        Supplier updatedSupplier = supplierRepository.save(Supplier);
        return new ListSupplierDTO(updatedSupplier);
    }

    public void delete(UUID id) {
        if (!supplierRepository.existsById(id)) {
            throw new EntityNotFoundException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return supplierRepository.existsById(id);
    }
}
