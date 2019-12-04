package com.example.demo.services;

import com.example.demo.models.Curso;
import com.example.demo.models.Disponibilidade;
import com.example.demo.models.Explicador;
import com.example.demo.repositories.ExplicadorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ExplicadorService {

    @Autowired
    private ExplicadorRepo explicadorRepo;

    @Autowired
    private CursoService cursoService;

    public Set<Explicador> findAll() {
        Set<Explicador> explicadores = new HashSet<>();
        for(Explicador explicador: this.explicadorRepo.findAll()){
            explicadores.add(explicador);
        }
        return explicadores;
    }

    public Optional<Explicador> findById(Long id) { return this.explicadorRepo.findById(id); }

    public Optional<Explicador> createExplicador(Explicador explicador) {
        Optional<Explicador> optionalExplicador = this.explicadorRepo.findByNome(explicador.getNome());
        if(!optionalExplicador.isPresent()){
            return Optional.of(this.explicadorRepo.save(explicador));
        } else
            return Optional.empty();
    }

    //Should the Explicador be saved again in the repo?
    public Optional<Explicador> putExplicadorCurso(Explicador explicador, String cursoName){
        Optional<Explicador> optionalExplicador = this.explicadorRepo.findByNome(explicador.getNome());
        Optional<Curso> cursoOptional = this.cursoService.findByNome(cursoName);
        if(optionalExplicador.isPresent() && cursoOptional.isPresent()){
            explicador.setCurso(cursoOptional.get());
            return Optional.of(this.explicadorRepo.save(explicador));
        } else
            return Optional.empty();
    }
    //???????????????

    public Optional <Explicador> searchExplicador(String cursoName, String dia, int horaInicio, int horaFim){
        int diaValue;
        switch(dia){
            case "domingo" : diaValue = 0; break;
            case "segunda": diaValue = 1; break;
            case "terca": diaValue = 2; break;
            case "quarta": diaValue = 3; break;
            case "quinta": diaValue = 4; break;
            case "sexta": diaValue = 5; break;
            case "sabado": diaValue = 6; break;
            default: diaValue = -1; break;
        }

        for(Explicador explicador: this.explicadorRepo.findAll()){
            for(Disponibilidade disponibilidade: explicador.getDisponibilidades()){

                if(disponibilidade.getDia().getValue() == diaValue
                && disponibilidade.getHoraIn().getHour() == horaInicio
                && disponibilidade.getHoraFim().getHour() == horaFim
                && explicador.getCurso().getNome() == cursoName){
                    return Optional.of(disponibilidade.getExplicador());
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Explicador> findByNome(String nome){
        return this.explicadorRepo.findByNome(nome);
    }
}
