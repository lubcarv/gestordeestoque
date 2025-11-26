package com.luizabau.gestordeestoque.controller;

import com. luizabau.gestordeestoque.dto.*;
import com.luizabau.gestordeestoque.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org. springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@AllArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints para dados do dashboard e gráficos")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/kpis")
    @Operation(
            summary = "Obter KPIs do dashboard",
            description = "Retorna os principais indicadores: valor total do estoque, produtos ativos, " +
                    "produtos com estoque baixo, taxa de giro e movimentações do período"
    )
    public ResponseEntity<DashboardKPIsDTO> getKPIs(
            @Parameter(description = "Mês (1-12) para filtrar.  Se não informado, considera todos os períodos")
            @RequestParam(required = false) Integer mes,

            @Parameter(description = "Ano (ex: 2025) para filtrar.  Se não informado, considera todos os períodos")
            @RequestParam(required = false) Integer ano
    ) {
        DashboardKPIsDTO kpis = dashboardService.calcularKPIs(mes, ano);
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/top-vendidos")
    @Operation(
            summary = "Top produtos mais vendidos",
            description = "Retorna os produtos com maior quantidade de saídas no período selecionado"
    )
    public ResponseEntity<List<ProdutoVendasDTO>> getTopVendidos(
            @Parameter(description = "Mês (1-12) para filtrar")
            @RequestParam(required = false) Integer mes,

            @Parameter(description = "Ano (ex: 2025) para filtrar")
            @RequestParam(required = false) Integer ano,

            @Parameter(description = "Quantidade de produtos a retornar (padrão: 10)")
            @RequestParam(defaultValue = "10") Integer limite
    ) {
        List<ProdutoVendasDTO> topVendidos = dashboardService.calcularTopVendidos(mes, ano, limite);
        return ResponseEntity. ok(topVendidos);
    }

    @GetMapping("/menos-vendidos")
    @Operation(
            summary = "Top produtos menos vendidos",
            description = "Retorna os produtos com menor quantidade de saídas no período selecionado"
    )
    public ResponseEntity<List<ProdutoVendasDTO>> getMenosVendidos(
            @Parameter(description = "Mês (1-12) para filtrar")
            @RequestParam(required = false) Integer mes,

            @Parameter(description = "Ano (ex: 2025) para filtrar")
            @RequestParam(required = false) Integer ano,

            @Parameter(description = "Quantidade de produtos a retornar (padrão: 10)")
            @RequestParam(defaultValue = "10") Integer limite
    ) {
        List<ProdutoVendasDTO> menosVendidos = dashboardService.calcularMenosVendidos(mes, ano, limite);
        return ResponseEntity.ok(menosVendidos);
    }

    @GetMapping("/categorias-vendidas")
    @Operation(
            summary = "Categorias mais vendidas",
            description = "Retorna a distribuição de vendas por categoria com percentuais"
    )
    public ResponseEntity<List<CategoriaVendasDTO>> getCategoriasVendidas(
            @Parameter(description = "Mês (1-12) para filtrar")
            @RequestParam(required = false) Integer mes,

            @Parameter(description = "Ano (ex: 2025) para filtrar")
            @RequestParam(required = false) Integer ano
    ) {
        List<CategoriaVendasDTO> categorias = dashboardService. calcularCategoriasVendidas(mes, ano);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/movimentacoes-periodo")
    @Operation(
            summary = "Movimentações por período",
            description = "Retorna entradas e saídas diárias para construção de gráfico temporal"
    )
    public ResponseEntity<MovimentacoesPeriodoDTO> getMovimentacoesPeriodo(
            @Parameter(description = "Mês (1-12) para filtrar.  Se não informado, usa mês atual")
            @RequestParam(required = false) Integer mes,

            @Parameter(description = "Ano (ex: 2025) para filtrar. Se não informado, usa ano atual")
            @RequestParam(required = false) Integer ano
    ) {
        MovimentacoesPeriodoDTO movimentacoes = dashboardService. calcularMovimentacoesPeriodo(mes, ano);
        return ResponseEntity.ok(movimentacoes);
    }

    @GetMapping("/ultimas-movimentacoes")
    @Operation(
            summary = "Últimas movimentações",
            description = "Retorna as movimentações mais recentes do sistema"
    )
    public ResponseEntity<List<MovimentacaoResumoDTO>> getUltimasMovimentacoes(
            @Parameter(description = "Quantidade de movimentações a retornar (padrão: 10)")
            @RequestParam(defaultValue = "10") Integer limite
    ) {
        List<MovimentacaoResumoDTO> movimentacoes = dashboardService. buscarUltimasMovimentacoes(limite);
        return ResponseEntity.ok(movimentacoes);
    }
}