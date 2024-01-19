package io.github.mateuussilvapb.msCartoes.infra.repository;

import io.github.mateuussilvapb.msCartoes.domain.ClienteCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteCartaoRepository extends JpaRepository<ClienteCartao, Long> {

    List<ClienteCartao> findByCpf(String cpf);

}
