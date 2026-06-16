package re.edu.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.productservice.entity.Product;
import re.edu.productservice.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            product.setStockQuantity(productDetails.getStockQuantity());
            product.setDescription(productDetails.getDescription());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void updateStock(Long id, Integer newStockQuantity) {
        productRepository.findById(id).ifPresent(product -> {
            product.setStockQuantity(newStockQuantity);
            productRepository.save(product);
        });
    }
}

