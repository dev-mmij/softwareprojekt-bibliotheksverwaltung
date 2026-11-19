package com.softwareprojekt.gruppe8.empfehlung.service;

import com.softwareprojekt.gruppe8.empfehlung.domain.Empfehlungsparameter;
import com.softwareprojekt.gruppe8.empfehlung.domain.EmpfehlungsparameterDefinition;
import com.softwareprojekt.gruppe8.empfehlung.repository.EmpfehlungsparameterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class EmpfehlungsparameterService {

    private final EmpfehlungsparameterRepository empfehlungsparameterRepository;

    public EmpfehlungsparameterService(EmpfehlungsparameterRepository empfehlungsparameterRepository) {
        this.empfehlungsparameterRepository = empfehlungsparameterRepository;
    }

    public List<Empfehlungsparameter> getEmpfehlungsparameter(){
        return empfehlungsparameterRepository.findAllByOrderByDefinitionAsc();
    }

    @Transactional
    public void speichern(Map<EmpfehlungsparameterDefinition, Double> parameterWerte){
        parameterWerte.forEach((definition, wert) -> empfehlungsparameterRepository.findByDefinition(definition)
                .orElseThrow().setWert(wert));
    }

    @Transactional
    public void zuruecksetzen(){
        empfehlungsparameterRepository.findAll().forEach(Empfehlungsparameter::aufStandardZuruecksetzen);
    }
}
