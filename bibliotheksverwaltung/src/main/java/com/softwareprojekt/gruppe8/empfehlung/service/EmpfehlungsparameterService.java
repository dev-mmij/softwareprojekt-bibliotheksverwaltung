package com.softwareprojekt.gruppe8.empfehlung.service;

import com.softwareprojekt.gruppe8.empfehlung.domain.Empfehlungsparameter;
import com.softwareprojekt.gruppe8.empfehlung.domain.EmpfehlungsparameterDefinition;
import com.softwareprojekt.gruppe8.empfehlung.repository.EmpfehlungsparameterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmpfehlungsparameterService {

    private final EmpfehlungsparameterRepository empfehlungsparameterRepository;

    public EmpfehlungsparameterService(EmpfehlungsparameterRepository empfehlungsparameterRepository) {
        this.empfehlungsparameterRepository = empfehlungsparameterRepository;
    }

    public List<Empfehlungsparameter> getEmpfehlungsparameters(){
        return empfehlungsparameterRepository.findAll().stream()
                .sorted(Comparator.comparing(Empfehlungsparameter::getKennung))
                .collect(Collectors.toList());
    }

    @Transactional
    public void speichern(Map<String, Double> parameterWerte){
        parameterWerte.forEach((kennung, wert) -> {
            EmpfehlungsparameterDefinition parameterDefinition = EmpfehlungsparameterDefinition.valueOf(kennung);
            if(!parameterDefinition.istGueltig(wert)){
                throw new IllegalArgumentException(kennung + " muss zwischen " + parameterDefinition.getMinimum()
                + " und " + parameterDefinition.getMaximum() + " liegen.");
        }});

        parameterWerte.forEach((kennung, wert) -> empfehlungsparameterRepository.findByKennung(kennung)
                .orElseThrow().setWert(wert));
    }

    @Transactional
    public void zuruecksetzen(){
        empfehlungsparameterRepository.findAll().forEach(Empfehlungsparameter::aufStandardZuruecksetzen);
    }
}
