package br.com.rodrigolopesdev.product_api.services;


import br.com.rodrigolopesdev.product_api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void shouldReturnAllProducts(){


        Pageable pageable = PageRequest.of(1, 20);
        var result = productService.find()
    }
}
