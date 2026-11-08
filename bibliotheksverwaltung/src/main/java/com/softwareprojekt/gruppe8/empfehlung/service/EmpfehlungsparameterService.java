package com.softwareprojekt.gruppe8.empfehlung.service;

import com.softwareprojekt.gruppe8.empfehlung.domain.Empfehlungsparameter;
import com.softwareprojekt.gruppe8.empfehlung.repository.EmpfehlungsparameterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpfehlungsparameterService {

    private final EmpfehlungsparameterRepository empfehlungsparameterRepository;

    public EmpfehlungsparameterService(EmpfehlungsparameterRepository empfehlungsparameterRepository) {
        this.empfehlungsparameterRepository = empfehlungsparameterRepository;
    }

    public List<Empfehlungsparameter> getEmpfehlungsparameters(){
        return empfehlungsparameterRepository.findAll();
    }
}
