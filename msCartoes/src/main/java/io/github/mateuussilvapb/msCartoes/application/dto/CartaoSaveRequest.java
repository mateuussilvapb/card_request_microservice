package io.github.mateuussilvapb.msCartoes.application.dto;

import io.github.mateuussilvapb.msCartoes.domain.BandeiraCartao;
import io.github.mateuussilvapb.msCartoes.domain.Cartao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoSaveRequest {
    private String nome;
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limite;

    public Cartao toModel() {
        return new Cartao(nome, bandeira, renda, limite);
    }
}
