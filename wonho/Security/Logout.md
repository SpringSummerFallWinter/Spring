# Logout

## 설정

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.logout()
                .logoutUrl("/logout") // 로그아웃 처리 URL
                .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동페이지
                .deleteCookies("JSESSIONID", "remember-me") // 로그아웃 후 쿠키 삭제
                .addLogoutHandler(logoutHandler()) // 로그아웃 핸들러
                .logoutSuccessHandler(logoutSuccessHandler()); // 로그아웃 성공 후 핸들러

        return http.build();
    }
}
```
