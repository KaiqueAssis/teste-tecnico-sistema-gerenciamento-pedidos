package org.teste.casetecnico.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.teste.casetecnico.exception.UsuarioException;
import org.teste.casetecnico.form.CadastroForm;
import org.teste.casetecnico.model.Users;
import org.teste.casetecnico.model.enums.UserRole;
import org.teste.casetecnico.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CadastroServiceTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CadastroService cadastroService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarNovoUsuarioQuandoLoginNaoExiste() throws Exception {

        CadastroForm form = new CadastroForm("kaique", "123456", "USER");

        when(userRepository.findByLogin("kaique")).thenReturn(null);

        cadastroService.cadastrarNovoUser(form);

        ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);
        verify(userRepository, times(1)).save(captor.capture());

        Users userSalvo = captor.getValue();
        assertEquals("kaique", userSalvo.getLogin());
        assertTrue(new BCryptPasswordEncoder().matches("123456", userSalvo.getPassword()));
        assertEquals(UserRole.USER, userSalvo.getRole());
    }

    @Test
    void deveLancarExcecaoQuandoLoginJaExiste() {
        CadastroForm form = new CadastroForm("kaique", "123456", "USER");

        when(userRepository.findByLogin("kaique")).thenReturn(new Users());

        UsuarioException thrown = assertThrows(
                UsuarioException.class,
                () -> cadastroService.cadastrarNovoUser(form)
        );

        assertEquals("Já existe um usuário com esse login", thrown.getMessage());
        verify(userRepository, never()).save(any());
    }

}