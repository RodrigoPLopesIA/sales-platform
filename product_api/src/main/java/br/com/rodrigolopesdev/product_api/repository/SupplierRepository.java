package br.com.rodrigolopesdev.product_api.repository;

import br.com.rodrigolopesdev.product_api.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SupplierRepository  extends JpaRepository<Supplier, UUID> {

    boolean existsByName(String name);
}
