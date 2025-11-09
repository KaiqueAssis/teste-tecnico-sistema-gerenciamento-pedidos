package org.teste.casetecnico.service;


import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.teste.casetecnico.exception.UsuarioException;
import org.teste.casetecnico.form.CadastroForm;
import org.teste.casetecnico.model.Users;
import org.teste.casetecnico.model.enums.UserRole;
import org.teste.casetecnico.repository.UserRepository;

@Service
public class CadastroService {

    private final UserRepository userRepository;

    public CadastroService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public void cadastrarNovoUser(CadastroForm form)
            throws Exception {
        if (this.userRepository.findByLogin(form.login()) != null) {
            throw new UsuarioException("Já existe um usuário com esse login");
        }

        String senhaCriptografada = new BCryptPasswordEncoder().encode(form.senha());
        Users user = new Users(form.login(), senhaCriptografada, UserRole.valueOf(form.role()));

        this.userRepository.save(user);

    }
}
