package re.edu.productservice.entity;

public class ProductEntity {
    private Long id;
    private String name;
    private String sku;
    private Double importPrice;
    private Double sellPrice;
    private Integer stockQuantity;

    public ProductEntity() {
    }

    public ProductEntity(Long id, String name, String sku, Double importPrice, Double sellPrice, Integer stockQuantity) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.importPrice = importPrice;
        this.sellPrice = sellPrice;
        this.stockQuantity = stockQuantity;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(Double importPrice) {
        this.importPrice = importPrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", importPrice=" + importPrice +
                ", sellPrice=" + sellPrice +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}

