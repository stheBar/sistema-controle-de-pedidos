package com.csi.sistema_controle_pedidos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produto")
@Schema(description = "Entidade que representa um produto (item de cardápio)")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    @Schema(description = "ID único do produto", example = "1")
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    @Schema(description = "Nome do produto", example = "X-Salada")
    private String nome;

    @Column(name = "descricao")
    @Schema(description = "Descrição detalhada do produto", example = "Pão, bife, queijo, alface, tomate e maionese")
    private String descricao;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Preço unitário do produto", example = "22.00")
    private BigDecimal preco;

    @Column(name = "disponivel", nullable = false)
    @Schema(description = "Indica se o produto está disponível para venda", example = "true")
    private Boolean disponivel;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    @Schema(description = "Categoria à qual o produto pertence", example = "COMIDA")
    private ProdutoCategoria produtoCategoria;

    @OneToMany(mappedBy = "produto")
    @Schema(description = "Lista de itens de pedido associados a este produto")
    private List<ItemPedido> itensPedido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public ProdutoCategoria getProdutoCategoria() {
        return produtoCategoria;
    }

    public void setProdutoCategoria(ProdutoCategoria produtoCategoria) {
        this.produtoCategoria = produtoCategoria;
    }

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }
}