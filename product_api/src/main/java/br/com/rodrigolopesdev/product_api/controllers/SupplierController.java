package br.com.rodrigolopesdev.product_api.controllers;

import br.com.rodrigolopesdev.product_api.dtos.ListSupplierDTO;
import br.com.rodrigolopesdev.product_api.dtos.SaveSupplierDTO;
import br.com.rodrigolopesdev.product_api.services.SupplierService;
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
@RequestMapping("/api/v1/suppliers")
public class SupplierController {

    @Autowired
    private  SupplierService supplierService;


    @GetMapping
    public ResponseEntity<Page<ListSupplierDTO>> listAll(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {

        Page<ListSupplierDTO> suppliers = supplierService.find(pageable, name);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListSupplierDTO> getById(@PathVariable String id) {
        ListSupplierDTO supplier = supplierService.findById(UUID.fromString(id));
        return ResponseEntity.ok(supplier);
    }

    @PostMapping
    public ResponseEntity<ListSupplierDTO> create(
            @RequestBody @Valid SaveSupplierDTO dto,
            UriComponentsBuilder uriBuilder) {

        ListSupplierDTO supplier = supplierService.create(dto);
        URI uri = uriBuilder.path("/api/v1/suppliers/{id}").buildAndExpand(supplier.id()).toUri();
        return ResponseEntity.created(uri).body(supplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListSupplierDTO> update(
            @PathVariable String id,
            @RequestBody @Valid SaveSupplierDTO dto) {

        ListSupplierDTO updatedSupplier = supplierService.update(UUID.fromString(id), dto);
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        supplierService.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable String id) {
        return ResponseEntity.ok(supplierService.existsById(UUID.fromString(id)));
    }
}