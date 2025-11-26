package com.luizabau.gestordeestoque.service;

import com.luizabau.gestordeestoque.domain.MovimentacaoEstoque;
import com.luizabau.gestordeestoque.domain. Produto;
import com.luizabau.gestordeestoque.domain.TipoMovimentacao;
import com.luizabau.gestordeestoque.dto.*;
import com.luizabau.gestordeestoque.repository.MovimentacaoEstoqueRepository;
import com.luizabau.gestordeestoque.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j. Slf4j;
import org. springframework.stereotype.Service;
import org.springframework.transaction.annotation. Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time. YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream. Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DashboardService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @Transactional(readOnly = true)
    public DashboardKPIsDTO calcularKPIs(Integer mes, Integer ano) {
        log.info("Calculando KPIs para mês={}, ano={}", mes, ano);

        List<Produto> produtosAtivos = produtoRepository.findAll().stream()
                .filter(Produto::getAtivo)
                .toList();

        BigDecimal valorTotalEstoque = produtosAtivos.stream()
                . filter(p -> p.getEstoque() != null)
                . map(p -> {
                    int quantidade = p.getEstoque(). getQuantidade();
                    BigDecimal preco = p.getPreco() != null ? p.getPreco() : BigDecimal.ZERO;
                    return preco. multiply(BigDecimal.valueOf(quantidade));
                })
                . reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Produto> produtosBaixoEstoque = produtoRepository.findProdutosComEstoqueBaixo();

        LocalDateTime dataInicio = null;
        LocalDateTime dataFim = null;

        if (mes != null && ano != null) {
            YearMonth yearMonth = YearMonth.of(ano, mes);
            dataInicio = yearMonth.atDay(1). atStartOfDay();
            dataFim = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        }

        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findAll();

        if (dataInicio != null && dataFim != null) {
            LocalDateTime finalDataInicio = dataInicio;
            LocalDateTime finalDataFim = dataFim;
            movimentacoes = movimentacoes.stream()
                    .filter(m -> ! m.getDataMovimentacao().isBefore(finalDataInicio)
                            && !m.getDataMovimentacao().isAfter(finalDataFim))
                    .toList();
        }

        int totalEntradas = movimentacoes.stream()
                .filter(m -> m.getTipo() == TipoMovimentacao.ENTRADA)
                . mapToInt(MovimentacaoEstoque::getQuantidade)
                .sum();

        int totalSaidas = movimentacoes.stream()
                .filter(m -> m.getTipo() == TipoMovimentacao.SAIDA)
                .mapToInt(MovimentacaoEstoque::getQuantidade)
                .sum();

        double taxaGiro = calcularTaxaGiroEstoque(totalSaidas, valorTotalEstoque);

        return DashboardKPIsDTO. builder()
                .valorTotalEstoque(valorTotalEstoque)
                .produtosAtivos(produtosAtivos. size())
                .produtosEstoqueBaixo(produtosBaixoEstoque.size())
                .taxaGiroEstoque(taxaGiro)
                .totalMovimentacoesMes(movimentacoes.size())
                .totalEntradas(totalEntradas)
                .totalSaidas(totalSaidas)
                .build();
    }

    private double calcularTaxaGiroEstoque(int totalSaidas, BigDecimal valorTotalEstoque) {
        if (valorTotalEstoque.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }


        BigDecimal saidas = BigDecimal.valueOf(totalSaidas);
        return saidas.divide(valorTotalEstoque, 2, RoundingMode.HALF_UP). doubleValue();
    }

    @Transactional(readOnly = true)
    public List<ProdutoVendasDTO> calcularTopVendidos(Integer mes, Integer ano, Integer limite) {
        log. info("Calculando top {} produtos mais vendidos para mês={}, ano={}", limite, mes, ano);

        LocalDateTime dataInicio = null;
        LocalDateTime dataFim = null;

        if (mes != null && ano != null) {
            YearMonth yearMonth = YearMonth.of(ano, mes);
            dataInicio = yearMonth. atDay(1).atStartOfDay();
            dataFim = yearMonth.atEndOfMonth(). atTime(23, 59, 59);
        }

        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findAll(). stream()
                .filter(m -> m.getTipo() == TipoMovimentacao.SAIDA)
                .toList();

        if (dataInicio != null && dataFim != null) {
            LocalDateTime finalDataInicio = dataInicio;
            LocalDateTime finalDataFim = dataFim;
            movimentacoes = movimentacoes.stream()
                    .filter(m -> !m. getDataMovimentacao().isBefore(finalDataInicio)
                            && !m.getDataMovimentacao().isAfter(finalDataFim))
                    .toList();
        }

        Map<Integer, ProdutoVendasDTO> vendasPorProduto = new HashMap<>();

        for (MovimentacaoEstoque mov : movimentacoes) {
            Produto produto = mov.getProduto();
            Integer produtoId = produto.getId();

            ProdutoVendasDTO dto = vendasPorProduto.getOrDefault(produtoId,
                    ProdutoVendasDTO.builder()
                            .produtoId(produtoId)
                            .codigoProduto(produto.getCodigo())
                            .nomeProduto(produto.getNome())
                            .quantidade(0)
                            .valorTotal(BigDecimal.ZERO)
                            .build()
            );

            dto.setQuantidade(dto.getQuantidade() + mov. getQuantidade());
            BigDecimal valorMovimentacao = produto.getPreco() != null
                    ?  produto.getPreco(). multiply(BigDecimal.valueOf(mov.getQuantidade()))
                    : BigDecimal. ZERO;
            dto.setValorTotal(dto.getValorTotal().add(valorMovimentacao));

            vendasPorProduto.put(produtoId, dto);
        }

        return vendasPorProduto. values(). stream()
                .sorted(Comparator.comparing(ProdutoVendasDTO::getQuantidade). reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoVendasDTO> calcularMenosVendidos(Integer mes, Integer ano, Integer limite) {
        log. info("Calculando top {} produtos menos vendidos para mês={}, ano={}", limite, mes, ano);

        List<ProdutoVendasDTO> topVendidos = calcularTopVendidos(mes, ano, Integer.MAX_VALUE);

        return topVendidos.stream()
                .sorted(Comparator.comparing(ProdutoVendasDTO::getQuantidade))
                .limit(limite)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoriaVendasDTO> calcularCategoriasVendidas(Integer mes, Integer ano) {
        log.info("Calculando categorias mais vendidas para mês={}, ano={}", mes, ano);

        LocalDateTime dataInicio = null;
        LocalDateTime dataFim = null;

        if (mes != null && ano != null) {
            YearMonth yearMonth = YearMonth.of(ano, mes);
            dataInicio = yearMonth.atDay(1).atStartOfDay();
            dataFim = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        }

        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository. findAll().stream()
                . filter(m -> m.getTipo() == TipoMovimentacao.SAIDA)
                . toList();

        if (dataInicio != null && dataFim != null) {
            LocalDateTime finalDataInicio = dataInicio;
            LocalDateTime finalDataFim = dataFim;
            movimentacoes = movimentacoes.stream()
                    . filter(m -> !m.getDataMovimentacao().isBefore(finalDataInicio)
                            && !m.getDataMovimentacao().isAfter(finalDataFim))
                    .toList();
        }

        Map<Integer, CategoriaVendasDTO> vendasPorCategoria = new HashMap<>();
        int totalGeral = 0;

        for (MovimentacaoEstoque mov : movimentacoes) {
            Produto produto = mov.getProduto();
            Integer categoriaId = produto.getCategoria().getId();
            String nomeCategoria = produto.getCategoria().getNome();

            CategoriaVendasDTO dto = vendasPorCategoria.getOrDefault(categoriaId,
                    CategoriaVendasDTO.builder()
                            . categoriaId(categoriaId)
                            .nomeCategoria(nomeCategoria)
                            .quantidade(0)
                            .percentual(0.0)
                            .build()
            );

            dto.setQuantidade(dto.getQuantidade() + mov.getQuantidade());
            vendasPorCategoria.put(categoriaId, dto);
            totalGeral += mov.getQuantidade();
        }

        final int total = totalGeral;
        vendasPorCategoria.values(). forEach(dto -> {
            if (total > 0) {
                double percentual = (dto.getQuantidade() * 100.0) / total;
                dto.setPercentual(Math.round(percentual * 100.0) / 100.0);
            }
        });

        return vendasPorCategoria.values().stream()
                .sorted(Comparator.comparing(CategoriaVendasDTO::getQuantidade). reversed())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovimentacoesPeriodoDTO calcularMovimentacoesPeriodo(Integer mes, Integer ano) {
        log.info("Calculando movimentações por período para mês={}, ano={}", mes, ano);

        LocalDate dataInicio;
        LocalDate dataFim;

        if (mes != null && ano != null) {
            YearMonth yearMonth = YearMonth.of(ano, mes);
            dataInicio = yearMonth. atDay(1);
            dataFim = yearMonth.atEndOfMonth();
        } else {
            YearMonth yearMonth = YearMonth.now();
            dataInicio = yearMonth.atDay(1);
            dataFim = yearMonth.atEndOfMonth();
        }

        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findAll().stream()
                .filter(m -> {
                    LocalDate dataMovimentacao = m.getDataMovimentacao().toLocalDate();
                    return !dataMovimentacao. isBefore(dataInicio) && !dataMovimentacao.isAfter(dataFim);
                })
                .toList();

        Map<LocalDate, Integer> entradasPorDia = new TreeMap<>();
        Map<LocalDate, Integer> saidasPorDia = new TreeMap<>();

        LocalDate data = dataInicio;
        while (!data.isAfter(dataFim)) {
            entradasPorDia.put(data, 0);
            saidasPorDia.put(data, 0);
            data = data.plusDays(1);
        }

        for (MovimentacaoEstoque mov : movimentacoes) {
            LocalDate dataMovimentacao = mov.getDataMovimentacao(). toLocalDate();

            if (mov.getTipo() == TipoMovimentacao.ENTRADA) {
                entradasPorDia.merge(dataMovimentacao, mov.getQuantidade(), Integer::sum);
            } else {
                saidasPorDia. merge(dataMovimentacao, mov.getQuantidade(), Integer::sum);
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter. ofPattern("dd/MM");
        List<String> labels = entradasPorDia.keySet().stream()
                .map(d -> d.format(formatter))
                .collect(Collectors.toList());

        List<Integer> entradas = new ArrayList<>(entradasPorDia.values());
        List<Integer> saidas = new ArrayList<>(saidasPorDia.values());

        return MovimentacoesPeriodoDTO. builder()
                .labels(labels)
                .entradas(entradas)
                .saidas(saidas)
                .build();
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResumoDTO> buscarUltimasMovimentacoes(Integer limite) {
        log.info("Buscando últimas {} movimentações", limite);

        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findAll().stream()
                .sorted(Comparator.comparing(MovimentacaoEstoque::getDataMovimentacao). reversed())
                .limit(limite)
                .toList();

        return movimentacoes.stream()
                .map(mov -> MovimentacaoResumoDTO. builder()
                        .movimentacaoId(Long.valueOf(mov.getId()))
                        .dataHora(mov.getDataMovimentacao())
                        .tipo(mov.getTipo())
                        .tipoDescricao(mov.getTipo().name())
                        .produtoId(mov.getProduto().getId())
                        .codigoProduto(mov.getProduto().getCodigo())
                        .nomeProduto(mov.getProduto().getNome())
                        .quantidade(mov.getQuantidade())
                        .usuario(mov.getUsuario())
                        .observacao(mov.getObservacao())
                        .build())
                .collect(Collectors.toList());
    }
}