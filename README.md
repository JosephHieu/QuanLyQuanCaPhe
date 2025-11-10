# Phần mềm Quản lý Quán Cà Phê (Cafe Management System)

## `Giới thiệu`
#### `Link Demo:` https://drive.google.com/file/d/1hqICk4utgtWYR5lZ6V7nbMjN7afOTYZn/view?usp=sharing
### Đây là một dự án web full-stack mô phỏng Hệ thống Quản lý Quán Cà Phê (POS) hoàn chỉnh, được xây dựng từ đầu bằng Spring Boot và Thymeleaf.
### Ứng dụng bao gồm các chức năng nghiệp vụ phức tạp từ quản lý nhân viên, kiểm soát kho hàng, quản lý thực đơn (bao gồm định lượng nguyên liệu), đến một giao diện bán hàng trực quan (xem, chuyển, gộp, tách bàn) và hệ thống báo cáo tài chính.

## `🚀 Tính năng nổi bật`
### Dự án này thể hiện khả năng xử lý các nghiệp vụ phức tạp và bảo mật ở cả backend và frontend:
## `1. Hệ thống Xác thực & Phân quyền (Spring Security)`
### Đăng nhập: Sử dụng Spring Security, mật khẩu được mã hóa bằng BCrypt.
### Phân quyền (Role-Based): Giao diện và chức năng được hiển thị động tùy theo vai trò (ROLE_ADMIN hoặc ROLE_STAFF).
### Admin: Thấy tất cả các mô-đun quản lý (Nhân viên, Kho, Báo cáo...).
### Staff (Nhân viên): Chỉ thấy các mô-đun cơ bản (Trang cá nhân, Quản lý Bán hàng).
### Bảo vệ Endpoint: Các API backend được bảo vệ (ví dụ: /admin/** chỉ Admin mới được truy cập).
### Bảo vệ CSRF: Kích hoạt cho tất cả các yêu cầu POST, đảm bảo an toàn.

## `2. Giao diện Bán hàng (POS)`
### Đây là mô-đun phức tạp nhất, xử lý logic nghiệp vụ theo thời gian thực:
### Hiển thị bàn: Hiển thị lưới các bàn với màu sắc động theo trạng thái (Trống, Có khách, Đặt trước).
### Xem thông tin bàn: Popup hiển thị các món đã gọi và thông tin đặt trước.
### Chọn & Cập nhật món: Thêm/sửa/xóa món ăn cho một hóa đơn đang mở.
### Nghiệp vụ Bàn:
### Chuyển bàn: Chuyển toàn bộ hóa đơn từ bàn A (có khách) sang bàn B (trống).
### Gộp bàn: Gộp các món và tổng tiền từ nhiều bàn nguồn vào một bàn đích.
### Tách bàn: Chuyển một số món (với số lượng tùy chọn) từ bàn A sang bàn B.
### Thanh toán: Xử lý thanh toán, tính tiền thối, và tự động cập nhật trạng thái hóa đơn (HoaDon.TrangThai) và trạng thái bàn (Ban.TinhTrang).

## `3. Các Mô-đun Quản lý (CRUD)`
### Quản lý Nhân viên: CRUD, tích hợp tạo TaiKhoan và phân quyền.
### Quản lý Thực đơn: CRUD, bao gồm logic phức tạp để quản lý thành phần nguyên liệu (ChiTietThucDon).
### Quản lý Kho hàng (HangHoa): Tích hợp logic Nhập/Xuất kho, tự động cập nhật số lượng tồn (soLuong).
### Quản lý Thiết bị & Khuyến mãi: CRUD tiêu chuẩn.

## `4. Báo các & Thống kê`
### Lọc động: Lọc báo cáo theo khoảng ngày và loại báo cáo (Thu/Chi, Lương).
### Tổng hợp dữ liệu: Tự động tổng hợp dữ liệu từ nhiều bảng (HoaDon, ChiTieu, NhanVien, ChucVu) bằng Java Streams ở tầng Service.
### Xuất file (Demo): Xây dựng API backend (/admin/reports/export) có khả năng tạo và trả về file Excel (.xls) (sử dụng thư viện Apache POI).

## `💻 Công nghệ sử dụng`
### Backend,"Spring Boot, Spring Security (Auth, BCrypt, CSRF), Spring Data JPA (Hibernate)"
### Frontend,"Thymeleaf (Server-Side Rendering), JavaScript (Fetch API, DOM)"
### Database,MySQL
### Build/Dependency,Maven
### Libraries,"Lombok, Apache POI (Xuất Excel)"

## `Hướng dẫn chạy với Docker (Docker Setup)`
### 1. Tải file
### Bạn có thể chạy toàn bộ dự án (Backend + CSDL) chỉ bằng một lệnh duy nhất với Docker.
### Yêu cầu: Đã cài đặt `Docker Desktop` (phiên bản cho Windows, Mac, hoặc Linux).
### Hướng dẫn chạy: Tải file `docker-compose.yml` và file `init.sql` từ repo này về. Đặt cả hai file vào chung một thư mục trống (ví dụ: D:\CafeApp)
### 2. Mở Terminal (Command Prompt hoặc PowerShell)
### Di chuyền đến thư mục bạn vừa tạo `cd D:\CafeApp`
### 3. Khởi động
### Chạy lệnh sau để tải images (ảnh) và khởi động các container (ứng dụng + CSDL). Docker sẽ tự động nạp file `init.sql` để tạo dữ liệu mẫu.
### `docker-compose up -d`
### 4. Truy cập ứng dụng
### Đợi khoảng 1-2 phút để ứng dụng khởi động hoàn toàn.
### Mở trình duyệt và truy cập: `http://localhost:8080`
### 5. Đăng nhập
### Admin: `admin / 123456`
### Staff: `phucvu01 / 123456`


## `🔧 Cài đặt & Chạy dự án (Local Setup)`
### Yêu cầu: Java (JDK 17+), Maven 3.x+, MySQL 8.x+

## `Clone Repository`
git clone https://github.com/JosephHieu/QuanLyQuanCaPhe.git

## `Cài đặt Cơ sở dữ liệu`
### 1. Mở MySQL và tạo một database mới `CREATE DATABASE CafeManagement;`
### 2. Script SQL để tạo bảng và chèn dữ liệu mẫu (đã bao gồm UUID) nằm ở thư mục `database-script`. Hoặc bạn có thể dựa vào cấu hình spring.jpa.hibernate.ddl-auto (nếu đặt là create hoặc update) để Hibernate tự tạo bảng.
### 3. Cấu hình kết nối src/main/resources/application.properties và cập nhật thông tin CSDL của bạn:
### `properties` 
### spring.datasource.url=jdbc:mysql://localhost:3306/CafeManagement
### spring.datasource.username=your_username
### spring.datasource.password=your_password
### spring.jpa.hibernate.ddl-auto=validate # (hoặc update/create)

## `Chạy Backend`
### Bạn có thể chạy bằng Maven hoặc trực tiếp từ IDE:
### Sử dụng `mvn spring-boot:run`
### Ứng dụng sẽ chạy tại http://localhost:8080

## `Tài khoản Demo`
### `Admin:` 
#### - Username: admin
#### - Password 123456 (Đã mã hóa mật khẩu trong csdl)

### `Staff`
#### - Username: phucvu01
#### - Password: 123456 (Đã mã hóa mật khẩu trong csdl)

## `Sơ đồ ERD`
<img width="2466" height="1435" alt="ERD_Cafe" src="https://github.com/user-attachments/assets/707a0a09-236b-47dc-ad6c-a4e4ea05fdd2" />

## `Lược đồ database`
<img width="1037" height="813" alt="DB_Cafe" src="https://github.com/user-attachments/assets/e199de9f-abe5-4ffa-9dd0-26eafbc2c834" />

## `Sơ đồ trạng thái`
<img width="1416" height="1066" alt="image" src="https://github.com/user-attachments/assets/3c7e1183-a7a0-43bd-8028-66c3a2e1d33d" />

