package io.github.mateuussilvapb.msCartoes.application.dto;

import io.github.mateuussilvapb.msCartoes.domain.ClienteCartao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartoesPorClienteResponse {
    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

    public static CartoesPorClienteResponse fromModel(ClienteCartao model) {
        return new CartoesPorClienteResponse(model.getCartao().getNome(), model.getCartao().getBandeira().toString(), model.getLimite());
    }
}
