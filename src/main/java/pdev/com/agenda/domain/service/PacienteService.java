package pdev.com.agenda.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdev.com.agenda.domain.entity.Paciente;
import pdev.com.agenda.domain.entity.PacienteAgenda;
import pdev.com.agenda.domain.repository.PacienteRepository;
import pdev.com.agenda.exception.BusinessException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;

    public Paciente salvar(Paciente paciente) {
        boolean existeCpf = false;
        boolean existeEmail = false;

       // List<PacienteAgenda> allAgendamentos = repository.findAllAgendamentos();
       // System.out.println("Quantidade de linhas: " + allAgendamentos.size()); metodo de simples busca para chamar a query no repository

        Optional<Paciente> optPaciente = repository.findByCpf(paciente.getCpf());

        if (optPaciente.isPresent()) {
            if (!optPaciente.get().getId().equals(paciente.getId())) {
                existeCpf = true;
            }
        }
        if (existeCpf) {
            throw new BusinessException("Cpf já cadastrado!");
        }
        Optional<Paciente> optionalPaciente = repository.findByEmail(paciente.getEmail());

        if (optionalPaciente.isPresent()) {
            if (!optionalPaciente.get().getId().equals(paciente.getId())) {
                existeEmail = true;
            }
        }
        if (existeEmail) {
            throw new BusinessException("Email já cadastrado!");
        }

        return repository.save(paciente);
    }

    public Paciente alterar(Long id, Paciente paciente) {
        Optional<Paciente> optPaciente = this.buscarPorId(id);

        if (optPaciente.isEmpty()) {
            throw new BusinessException("Paciente não cadastrado!");
        }

        paciente.setId(id);

        return salvar(paciente);
    }

    public List<Paciente> listarTodos() {
        return repository.findAll();
    }

    public Optional<Paciente> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}