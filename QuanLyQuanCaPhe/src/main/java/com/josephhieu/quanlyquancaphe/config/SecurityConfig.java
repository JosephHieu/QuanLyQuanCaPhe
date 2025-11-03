package com.josephhieu.quanlyquancaphe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Lớp cấu hình chính cho Spring Security.
 * Kích hoạt bảo mật web (@EnableWebSecurity) và định nghĩa các quy tắc
 * truy cập, trang đăng nhập, và xử lý đăng xuất.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Configuration
@EnableWebSecurity // Kích hoạt tính năng bảo mật web của Spring Security
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    /**
     * Định nghĩa một Bean {@link PasswordEncoder} để mã hóa mật khẩu.
     * Chúng ta sử dụng BCrypt, một thuật toán mã hóa một chiều mạnh và an toàn.
     * Bean này sẽ được Spring Security tự động sử dụng để
     * so sánh mật khẩu thô (raw password) từ form với mật khẩu đã mã hóa (hashed) trong CSDL.
     *
     * @return một implement của PasswordEncoder (cụ thể là BCryptPasswordEncoder).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cấu hình "Chuỗi Lọc Bảo mật" (Security Filter Chain).
     * Đây là nơi trung tâm để định nghĩa các quy tắc "ai được phép truy cập cái gì".
     *
     * @param http Đối tượng HttpSecurity để xây dựng các quy tắc bảo mật.
     * @return một {@link SecurityFilterChain} đã được cấu hình.
     * @throws Exception nếu có lỗi xảy ra trong quá trình cấu hình.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/css/**").permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/staff/**").hasRole("STAFF")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(successHandler)
                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL để thực hiện đăng xuất
                        .logoutSuccessUrl("/login?logout=true") // Trang chuyển đến khi đăng xuất thành công
                );

        return http.build();
    }
}
