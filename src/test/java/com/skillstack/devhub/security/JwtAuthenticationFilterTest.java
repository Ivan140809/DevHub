package com.skillstack.devhub.security;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @CasoPrueba(
            id = "CP90",
            descripcion = "JwtAuthenticationFilter - sin header Authorization, continua la cadena sin autenticar",
            entrada = "Solicitud HTTP sin header Authorization",
            tipo = "Normal",
            esperado = "Se llama a filterChain.doFilter y el SecurityContext queda vacio"
    )
    void doFilterInternal_noAuthorizationHeader_continuesChain() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        System.out.println("CP90 Sin header Authorization: cadena continuada sin autenticar");
    }

    @Test
    @CasoPrueba(
            id = "CP90B",
            descripcion = "JwtAuthenticationFilter - header sin prefijo Bearer, continua la cadena sin autenticar",
            entrada = "Authorization: Basic someToken",
            tipo = "Normal",
            esperado = "Se llama a filterChain.doFilter sin autenticar"
    )
    void doFilterInternal_nonBearerHeader_continuesChain() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic someToken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @CasoPrueba(
            id = "CP91",
            descripcion = "JwtAuthenticationFilter - token Bearer valido autentica al usuario correctamente",
            entrada = "Authorization: Bearer validToken, email=test@test.com",
            tipo = "Normal",
            esperado = "El SecurityContextHolder contiene la autenticacion del usuario"
    )
    void doFilterInternal_validBearerToken_authenticatesUser() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer validToken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        UserDetails userDetails = User.withUsername("test@test.com")
                .password("encodedPass")
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build();

        when(jwtUtil.extractEmail("validToken")).thenReturn("test@test.com");
        when(userDetailsService.loadUserByUsername("test@test.com")).thenReturn(userDetails);

        filter.doFilterInternal(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("test@test.com", SecurityContextHolder.getContext().getAuthentication().getName());
        verify(chain).doFilter(request, response);

        System.out.println("CP91 Token valido: usuario autenticado correctamente");
        System.out.println("Auth name: " + SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    @CasoPrueba(
            id = "CP92",
            descripcion = "JwtAuthenticationFilter - token invalido limpia el contexto y continua la cadena",
            entrada = "Authorization: Bearer tokenInvalido",
            tipo = "Negativa",
            esperado = "El SecurityContext queda vacio y se llama a filterChain.doFilter"
    )
    void doFilterInternal_invalidToken_clearsContextAndContinues() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer tokenInvalido");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtUtil.extractEmail("tokenInvalido")).thenThrow(new RuntimeException("Token invalido"));

        filter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);

        System.out.println("CP92 Token invalido: contexto limpiado y cadena continuada");
    }
}
