package re.edu.productservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.productservice.dto.ProductRequestDTO;
import re.edu.productservice.dto.ProductResponseDTO;
import re.edu.productservice.entity.Product;
import re.edu.productservice.exception.ResourceNotFoundException;
import re.edu.productservice.repository.ProductRepository;
import re.edu.productservice.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .build();

        Product saved = productRepository.save(product);

        return mapToResponse(saved);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProductResponseDTO mapToResponse(Product p) {
        return new ProductResponseDTO(p.getId(), p.getName(), p.getPrice(), p.getStockQuantity());
    }
}

