package io.github.mateuussilvapb.msClientes.infra.repository;

import io.github.mateuussilvapb.msClientes.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCpf(String cpf);
}
