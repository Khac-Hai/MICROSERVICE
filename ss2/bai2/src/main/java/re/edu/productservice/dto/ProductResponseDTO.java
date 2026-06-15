package re.edu.productservice.dto;

public class ProductResponseDTO {
    private Long id;
    private String name;
    private Double sellPrice;

    public ProductResponseDTO() {
    }

    public ProductResponseDTO(Long id, String name, Double sellPrice) {
        this.id = id;
        this.name = name;
        this.sellPrice = sellPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }
}

