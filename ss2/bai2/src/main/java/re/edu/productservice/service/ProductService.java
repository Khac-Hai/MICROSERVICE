package re.edu.productservice.service;

import org.springframework.stereotype.Service;
import re.edu.productservice.entity.ProductEntity;
import re.edu.productservice.dto.ProductResponseDTO;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductService {

    private final Map<Long, ProductEntity> inMemoryProducts = new HashMap<>();

    @PostConstruct
    public void init() {
        // sample data
        inMemoryProducts.put(1L, new ProductEntity(1L, "Wireless Mouse", "WM-100", 8.0, 19.99, 150));
        inMemoryProducts.put(2L, new ProductEntity(2L, "Mechanical Keyboard", "MK-200", 25.0, 59.99, 80));
        inMemoryProducts.put(3L, new ProductEntity(3L, "USB-C Cable", "UC-300", 1.0, 5.99, 500));
    }

    public ProductEntity getProductById(Long id) {
        return inMemoryProducts.get(id);
    }

    public ProductResponseDTO toResponseDto(ProductEntity entity) {
        if (entity == null) return null;
        return new ProductResponseDTO(entity.getId(), entity.getName(), entity.getSellPrice());
    }
}

