package io.github.mateuussilvapb.msAvaliadorCredito.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RetornoAvaliacaoCliente {
    private List<CartaoAprovado> cartoes;
}
