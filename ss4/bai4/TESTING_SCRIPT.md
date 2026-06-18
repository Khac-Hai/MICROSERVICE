# Script Test Load Balancing

Để kiểm tra Load Balancing hoạt động, bạn có thể sử dụng các script dưới đây.

---

## 1️⃣ Test với Postman

### Bước 1: Import Collection

Tạo một Postman Collection mới với các request sau:

#### Request 1: Tạo Product
```
Method: POST
URL: http://localhost:8082/api/v1/products
Headers: Content-Type: application/json

Body:
{
  "name": "IPhone 17",
  "price": 999.99,
  "quantity": 100,
  "description": "Latest iPhone model"
}
```

**Lưu ID: 1**

---

#### Request 2: Kiểm tra Load Balancing (Product Service)
```
Method: GET
URL: http://localhost:8082/api/v1/products/1
```

**Chạy lần lượt 5 lần, quan sát Port thay đổi**

---

#### Request 3: Kiểm tra Load Balancing (Order Service)
```
Method: GET
URL: http://localhost:8080/api/v1/orders/check-product/1
```

**Chạy lần lượt 5 lần, quan sát Port thay đổi**

---

#### Request 4: Tạo Order
```
Method: POST
URL: http://localhost:8080/api/v1/orders?productId=1&quantity=3
```

---

#### Request 5: Xem tất cả Orders
```
Method: GET
URL: http://localhost:8080/api/v1/orders
```

---

## 2️⃣ Test với cURL (Command Line)

### Bước 1: Tạo Product

```bash
curl -X POST http://localhost:8082/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "IPhone 17",
    "price": 999.99,
    "quantity": 100,
    "description": "Latest iPhone"
  }'
```

---

### Bước 2: Test Load Balancing (5 lần liên tiếp)

**PowerShell:**

```powershell
for ($i=1; $i -le 5; $i++) {
    Write-Host "Request $i:"
    curl -X GET http://localhost:8080/api/v1/orders/check-product/1 | ConvertFrom-Json | Select-Object name
    Start-Sleep -Seconds 1
}
```

**Bash (Linux/Mac):**

```bash
for i in {1..5}; do
    echo "Request $i:"
    curl -X GET http://localhost:8080/api/v1/orders/check-product/1 | jq '.name'
    sleep 1
done
```

---

**Kết quả dự kiến:**
```
Request 1:
name: "IPhone 17 - Port 8082"

Request 2:
name: "IPhone 17 - Port 8084"

Request 3:
name: "IPhone 17 - Port 8082"

Request 4:
name: "IPhone 17 - Port 8084"

Request 5:
name: "IPhone 17 - Port 8082"
```

---

### Bước 3: Test Load Balancing (Direct to Product Service)

**5 lần gọi trực tiếp:**

```bash
for i in {1..5}; do
    echo "Request $i:"
    curl -X GET http://localhost:8082/api/v1/products/1 | jq '.name'
    sleep 1
done
```

**Hoặc gọi từng port cụ thể:**

```bash
# Port 8082
curl -X GET http://localhost:8082/api/v1/products/1 | jq '.name'

# Port 8084
curl -X GET http://localhost:8084/api/v1/products/1 | jq '.name'

# Port 9090 (nếu running)
curl -X GET http://localhost:9090/api/v1/products/1 | jq '.name'
```

---

## 3️⃣ Test với Python Script

Tạo file `test_loadbalancing.py`:

```python
import requests
import time

def test_load_balancing():
    """Test load balancing bằng cách gọi API nhiều lần"""
    
    url = "http://localhost:8080/api/v1/orders/check-product/1"
    
    print("=" * 60)
    print("Testing Load Balancing - 10 requests")
    print("=" * 60)
    
    for i in range(1, 11):
        try:
            response = requests.get(url)
            data = response.json()
            port = data.get('name', 'N/A').split('- Port ')[-1] if '- Port' in data.get('name', '') else 'N/A'
            
            print(f"Request {i:2d}: Port {port:<6} - Status {response.status_code}")
            time.sleep(0.5)
            
        except Exception as e:
            print(f"Request {i:2d}: Error - {e}")
            time.sleep(1)

def create_test_product():
    """Tạo product test"""
    url = "http://localhost:8082/api/v1/products"
    payload = {
        "name": "Test Product",
        "price": 99.99,
        "quantity": 100,
        "description": "Test"
    }
    
    try:
        response = requests.post(url, json=payload)
        if response.status_code == 201:
            print("✅ Product created successfully")
            return response.json().get('id')
        else:
            print(f"❌ Failed to create product: {response.status_code}")
    except Exception as e:
        print(f"❌ Error creating product: {e}")

def create_order(product_id, quantity):
    """Tạo order"""
    url = f"http://localhost:8080/api/v1/orders?productId={product_id}&quantity={quantity}"
    
    try:
        response = requests.post(url)
        if response.status_code == 201:
            print(f"✅ Order created: {response.json()}")
        else:
            print(f"❌ Failed to create order: {response.status_code}")
    except Exception as e:
        print(f"❌ Error creating order: {e}")

if __name__ == "__main__":
    # Create product
    # product_id = create_test_product()
    
    # Test load balancing
    test_load_balancing()
    
    # Or create order
    # if product_id:
    #     create_order(product_id, 5)
```

**Chạy script:**

```bash
python test_loadbalancing.py
```

---

## 4️⃣ Test với Node.js Script

Tạo file `test_loadbalancing.js`:

```javascript
const http = require('http');

function makeRequest(url) {
    return new Promise((resolve, reject) => {
        http.get(url, (res) => {
            let data = '';
            res.on('data', chunk => data += chunk);
            res.on('end', () => {
                try {
                    resolve(JSON.parse(data));
                } catch {
                    reject('Invalid JSON');
                }
            });
        }).on('error', reject);
    });
}

async function testLoadBalancing() {
    console.log('='.repeat(60));
    console.log('Testing Load Balancing - 10 requests');
    console.log('='.repeat(60));
    
    const url = 'http://localhost:8080/api/v1/orders/check-product/1';
    
    for (let i = 1; i <= 10; i++) {
        try {
            const data = await makeRequest(url);
            const name = data.name || 'N/A';
            const port = name.split('- Port ')[1] || 'N/A';
            
            console.log(`Request ${String(i).padStart(2)}: Port ${String(port).padEnd(6)} - ${name}`);
            
            await new Promise(resolve => setTimeout(resolve, 500));
        } catch (error) {
            console.log(`Request ${String(i).padStart(2)}: Error - ${error}`);
            await new Promise(resolve => setTimeout(resolve, 1000));
        }
    }
}

testLoadBalancing().catch(console.error);
```

**Chạy script:**

```bash
node test_loadbalancing.js
```

---

## 5️⃣ Kiểm tra Eureka Dashboard

- Truy cập: **http://localhost:8761**
- Kiểm tra các service đã đăng ký
- Quan sát status của từng instance

---

## 6️⃣ Kiểm tra Log

Mở terminal của từng service và quan sát log:

**Product Service (8082):**
```
GET /api/v1/products/1
```

**Product Service (8084):**
```
GET /api/v1/products/1
```

Nếu nhìn thấy requests được phân phối, Load Balancing đang hoạt động! ✅

---

## 🔍 Điểm quan trọng cần quan sát

### ✅ Load Balancing hoạt động khi:

1. **Port thay đổi tuần tự**: 8082 → 8084 → 8082 → 8084 → ...
2. **Requests phân phối đều**: Mỗi port nhận ~50% requests
3. **Không cần chỉ định port**: URL là `http://PRODUCT-SERVICE`, không phải `http://localhost:8082`
4. **Eureka liệt kê cả 2 instances**

### ❌ Load Balancing KHÔNG hoạt động nếu:

1. **Port không thay đổi**: Cùng port mỗi request
2. **Lỗi kết nối**: `UnknownHostException: PRODUCT-SERVICE`
3. **Chỉ 1 instance trong Eureka**: Không có load balancing
4. **Phải chỉ định port**: Phải dùng `localhost:8082` cố định

---

## 📊 Metrics cần kiểm tra

| Metric | Expected | Actual |
|--------|----------|--------|
| Eureka Services | 2 (Product, Order) | __ |
| Product Instances | 2-3 | __ |
| Order Instances | 1 | __ |
| Port Distribution | 50/50 (8082/8084) | __ |
| Response Time | < 100ms | __ |

---


