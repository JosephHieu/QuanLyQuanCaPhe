# Sử dụng một image Java 17 (hoặc phiên bản bạn dùng) gọn nhẹ
FROM eclipse-temurin:21-jdk-alpine

# Đặt thư mục làm việc bên trong container
WORKDIR /app

# QUAN TRỌNG: Sửa tên file .jar này cho khớp với file trong thư mục target/ của bạn
COPY target/QuanLyQuanCaPhe-0.0.1-SNAPSHOT.jar app.jar

# Mở cổng 8080 (cổng mặc định của Spring Boot)
EXPOSE 8080

# Lệnh để chạy ứng dụng khi container khởi động
ENTRYPOINT ["java", "-jar", "app.jar"]