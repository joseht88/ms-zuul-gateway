package upeu.ms.app.zuul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/security/oauth/token").permitAll()
		.antMatchers(HttpMethod.GET, "/api/producto/listar", "/api/item/listar", "/api/usuarios/usuario").permitAll()
		.antMatchers(HttpMethod.GET, "/api/producto/ver/{id}",
				"/api/item/ver/{id}/cantidad/{cantidad}",
				"/api/usuarios/usuario/{id}").hasAnyRole("ADMIN","USER")
		.antMatchers("/api/producto/**", "/api/item/**", "/api/usuarios/**").hasRole("ADMIN")
		.anyRequest().authenticated();
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		var tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey("12345");
		return tokenConverter;
	}
	
}
