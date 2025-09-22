package com.luizabau.gestordeestoque.service;

import com.luizabau.gestordeestoque.domain.Fornecedor;
import com.luizabau.gestordeestoque.dto.FornecedorCreateDTO;
import com.luizabau.gestordeestoque.dto.FornecedorResponseDTO;
import com.luizabau.gestordeestoque.dto.FornecedorSimpleDTO;
import com.luizabau.gestordeestoque.dto.FornecedorUpdateDTO;
import com.luizabau.gestordeestoque.mapper.FornecedorMapper;
import com.luizabau.gestordeestoque.repository.FornecedorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final FornecedorMapper fornecedorMapper;

    @Transactional(readOnly = true)
    public List<FornecedorResponseDTO> listarTodos() {
        return fornecedorRepository.findAll().stream()
                .map(fornecedorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FornecedorSimpleDTO> listarAtivos() {
        return fornecedorRepository.findByAtivoTrue().stream()
                .map(fornecedorMapper::toSimpleDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FornecedorResponseDTO buscarPorId(Integer id) throws Exception {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new Exception("Fornecedor não encontrado"));
        return fornecedorMapper.toResponseDTO(fornecedor);
    }

    @Transactional(readOnly = true)
    public List<FornecedorResponseDTO> buscarPorNome(String nome) throws Exception {
        List<Fornecedor> fornecedores = fornecedorRepository.findByNomeContainingIgnoreCase(nome);
        if (fornecedores.isEmpty()) {
            throw new Exception("Nenhum fornecedor encontrado com o nome: " + nome);
        }
        return fornecedores.stream()
                .map(fornecedorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FornecedorResponseDTO criar(FornecedorCreateDTO createDTO) throws Exception {
        if (createDTO.getEmail() != null && fornecedorRepository.existsByEmail(createDTO.getEmail())) {
            throw new Exception("Já existe um fornecedor com este email");
        }

        if (createDTO.getCnpj() != null && fornecedorRepository.existsByCnpj(createDTO.getCnpj())) {
            throw new Exception("Já existe um fornecedor com este CNPJ");
        }

        Fornecedor fornecedor = fornecedorMapper.toEntity(createDTO);
        fornecedor = fornecedorRepository.save(fornecedor);

        return fornecedorMapper.toResponseDTO(fornecedor);
    }

    @Transactional
    public FornecedorResponseDTO atualizar(Integer id, FornecedorUpdateDTO updateDTO) throws Exception {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new Exception("Fornecedor não encontrado"));

        if (updateDTO.getEmail() != null &&
                !fornecedor.getEmail().equals(updateDTO.getEmail()) &&
                fornecedorRepository.existsByEmail(updateDTO.getEmail())) {
            throw new Exception("Já existe um fornecedor com este email");
        }

        if (updateDTO.getCnpj() != null &&
                !fornecedor.getCnpj().equals(updateDTO.getCnpj()) &&
                fornecedorRepository.existsByCnpj(updateDTO.getCnpj())) {
            throw new Exception("Já existe um fornecedor com este CNPJ");
        }

        if (updateDTO.getNome() != null) fornecedor.setNome(updateDTO.getNome());
        if (updateDTO.getEmail() != null) fornecedor.setEmail(updateDTO.getEmail());
        if (updateDTO.getFone() != null) fornecedor.setFone(updateDTO.getFone());
        if (updateDTO.getCnpj() != null) fornecedor.setCnpj(updateDTO.getCnpj());
        if (updateDTO.getEndereco() != null) fornecedor.setEndereco(updateDTO.getEndereco());
        if (updateDTO.getAtivo() != null) fornecedor.setAtivo(updateDTO.getAtivo());

        fornecedor = fornecedorRepository.save(fornecedor);
        return fornecedorMapper.toResponseDTO(fornecedor);
    }

    @Transactional
    public void excluir(Integer id) throws Exception {
        if (!fornecedorRepository.existsById(id)) {
            throw new Exception("Fornecedor não encontrado");
        }

        Long countProdutos = fornecedorRepository.countProdutosByFornecedor(id);
        if (countProdutos > 0) {
            throw new Exception("Não é possível excluir fornecedor com " + countProdutos + " produto(s) vinculado(s)");
        }

        fornecedorRepository.deleteById(id);
    }
}