package com.bank.api.service;

import com.bank.api.dto.UsuarioCriacaoDTO;
import com.bank.api.dto.UsuarioDTO;
import com.bank.api.model.Usuario;
import com.bank.api.model.ContaBancaria;
import com.bank.api.repository.UsuarioRepository;
import com.bank.api.repository.ContaBancariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    public UsuarioDTO criar(UsuarioCriacaoDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setCpf(dto.getCpf());
        usuario.setTelefone(dto.getTelefone());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setEndereco(dto.getEndereco());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Criar conta bancária automaticamente
        criarContaBancaria(usuarioSalvo);

        return converterParaDTO(usuarioSalvo);
    }

    public UsuarioDTO obterPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return converterParaDTO(usuario);
    }

    public UsuarioDTO obterPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return converterParaDTO(usuario);
    }

    public List<UsuarioDTO> obterTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO atualizar(Long id, UsuarioCriacaoDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(dto.getNome());
        usuario.setTelefone(dto.getTelefone());
        usuario.setEndereco(dto.getEndereco());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return converterParaDTO(usuarioAtualizado);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    private void criarContaBancaria(Usuario usuario) {
        ContaBancaria contaBancaria = new ContaBancaria();
        contaBancaria.setUsuario(usuario);
        contaBancaria.setNumeroConta(gerarNumeroConta());
        contaBancaria.setAgencia(gerarAgencia());
        contaBancaria.setSaldo(BigDecimal.ZERO);
        contaBancaria.setLimiteCheque(BigDecimal.valueOf(1000));
        contaBancaria.setAtiva(true);

        contaBancariaRepository.save(contaBancaria);
    }

    private String gerarNumeroConta() {
        return String.valueOf(System.currentTimeMillis()).substring(3);
    }

    private String gerarAgencia() {
        return String.format("%04d", (int) (Math.random() * 10000));
    }

    private UsuarioDTO converterParaDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .cpf(usuario.getCpf())
                .telefone(usuario.getTelefone())
                .dataNascimento(usuario.getDataNascimento())
                .endereco(usuario.getEndereco())
                .dataCriacao(usuario.getDataCriacao())
                .dataAtualizacao(usuario.getDataAtualizacao())
                .build();
    }
}

