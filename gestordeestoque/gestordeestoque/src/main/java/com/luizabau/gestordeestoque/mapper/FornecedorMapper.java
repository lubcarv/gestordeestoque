package com.luizabau.gestordeestoque.mapper;

import com.luizabau.gestordeestoque.domain.Fornecedor;
import com.luizabau.gestordeestoque.dto.FornecedorCreateDTO;
import com.luizabau.gestordeestoque.dto.FornecedorResponseDTO;
import com.luizabau.gestordeestoque.dto.FornecedorSimpleDTO;
import com.luizabau.gestordeestoque.repository.FornecedorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FornecedorMapper {

    private final FornecedorRepository fornecedorRepository;

    public Fornecedor toEntity(FornecedorCreateDTO dto) {
        return Fornecedor.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .fone(dto.getFone())
                .cnpj(dto.getCnpj())
                .endereco(dto.getEndereco())
                .ativo(dto.getAtivo())
                .build();
    }

    public FornecedorResponseDTO toResponseDTO(Fornecedor entity) {
        Long totalProdutos = 0L;
        try {
            totalProdutos = fornecedorRepository.countProdutosByFornecedor(entity.getId());
        } catch (Exception e) {
            totalProdutos = 0L;
        }

        return FornecedorResponseDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .fone(entity.getFone())
                .cnpj(entity.getCnpj())
                .endereco(entity.getEndereco())
                .ativo(entity.getAtivo())
                .dataCriacao(entity.getDataCriacao())
                .dataAtualizacao(entity.getDataAtualizacao())
                .totalProdutos(totalProdutos.intValue())
                .build();
    }

    public FornecedorSimpleDTO toSimpleDTO(Fornecedor entity) {
        return new FornecedorSimpleDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCnpj(),
                entity.getEmail(),
                entity.getAtivo()
        );
    }
}