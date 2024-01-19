package io.github.mateuussilvapb.msAvaliadorCredito.application;

import feign.FeignException;
import io.github.mateuussilvapb.msAvaliadorCredito.application.ex.DadosClienteNotFoundException;
import io.github.mateuussilvapb.msAvaliadorCredito.application.ex.ErroComunicacaoMicroservicesException;
import io.github.mateuussilvapb.msAvaliadorCredito.application.ex.ErroSolicitacaoCartaoException;
import io.github.mateuussilvapb.msAvaliadorCredito.application.infra.clients.CartoesResourceClient;
import io.github.mateuussilvapb.msAvaliadorCredito.application.infra.clients.ClienteResourceClient;
import io.github.mateuussilvapb.msAvaliadorCredito.domain.model.*;
import io.github.mateuussilvapb.msAvaliadorCredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosDoCliente(cpf);
            ResponseEntity<List<CartaoCliente>> dadosCartoesResponse = cartoesClient.getCartoesByCliente(cpf);
            return SituacaoCliente.builder().cliente(dadosClienteResponse.getBody()).cartoes(dadosCartoesResponse.getBody()).build();
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosDoCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesRendaAte = cartoesClient.getCartoesRendaAte(renda);
            List<Cartao> cartoes = cartoesRendaAte.getBody();
            List<CartaoAprovado> listaCartoesAprovados = cartoes.stream().map(cartao -> {
                DadosCliente dadosCliente = dadosClienteResponse.getBody();
                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                BigDecimal fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);
                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);
                return aprovado;
            }).collect(Collectors.toList());
            return new RetornoAvaliacaoCliente(listaCartoesAprovados);
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCarta(DadosSolicitacaoEmissaoCartao dados) {
        try {
            emissaoCartaoPublisher.solicitarCartao(dados);
            String protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (Exception e) {
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}
