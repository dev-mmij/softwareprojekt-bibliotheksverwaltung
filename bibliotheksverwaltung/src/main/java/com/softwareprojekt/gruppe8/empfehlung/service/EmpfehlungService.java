package com.softwareprojekt.gruppe8.empfehlung.service;



import com.softwareprojekt.gruppe8.empfehlung.algorithmus.*;
import com.softwareprojekt.gruppe8.empfehlung.domain.Empfehlung;
import com.softwareprojekt.gruppe8.empfehlung.domain.Empfehlungsparameter;
import com.softwareprojekt.gruppe8.empfehlung.domain.EmpfehlungsparameterDefinition;
import com.softwareprojekt.gruppe8.empfehlung.domain.Feedback;
import com.softwareprojekt.gruppe8.empfehlung.repository.EmpfehlungRepository;
import com.softwareprojekt.gruppe8.empfehlung.repository.EmpfehlungsparameterRepository;
import com.softwareprojekt.gruppe8.katalog.entity.Autor;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.katalog.entity.Genre;
import com.softwareprojekt.gruppe8.katalog.repository.BuchRepository;
import com.softwareprojekt.gruppe8.konto.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EmpfehlungService {

    private final BuchRepository buchRepository;
    private final EmpfehlungRepository empfehlungRepository;
    private final EmpfehlungsparameterRepository empfehlungsparameterRepository;

    private final Empfehlungsalgorithmus algorithmus;

    public EmpfehlungService(BuchRepository buchRepository, EmpfehlungRepository empfehlungRepository, EmpfehlungsparameterRepository empfehlungsparameterRepository) {
        this.buchRepository = buchRepository;
        this.empfehlungRepository = empfehlungRepository;
        this.empfehlungsparameterRepository = empfehlungsparameterRepository;
        algorithmus = new Empfehlungsalgorithmus(new Random());
    }

    @Transactional
    public List<Empfehlungsdaten> ladeNeueEmpfehlungen(User benutzer) {
        List<Buch> alleBuecher = buchRepository.findAll();
        List<BuchInfo> buchInfos = new ArrayList<>(); //Fuer den Algorithmus benoetigte Informationen
        Map<Long, Buch> buecherNachId = new HashMap<>(); //Zum Zurueckuebersetzen in Buecher
        for(Buch buch : alleBuecher) {
            buchInfos.add(zuBuchInfo(buch));
            buecherNachId.put(buch.getId(), buch);
        }
        List<VerlaufEintrag> verlauf = empfehlungRepository.findAllByBenutzer(benutzer).stream().map(this::zuVerlaufEintrag).toList();
        int naechstePosition = empfehlungRepository.findTopByBenutzerOrderByPositionDesc(benutzer)
                .map(letzteEmpfehlung -> letzteEmpfehlung.getPosition() + 1).orElse(1); //Liefert die naechste Position, oder 1 bei keiner Empfehlung
        AlgorithmusInput input = new AlgorithmusInput(buchInfos, verlauf, Set.of(), Set.of(), Set.of(), naechstePosition);
        Map<EmpfehlungsparameterDefinition, Double> parameter = new HashMap<>();
        for(Empfehlungsparameter empfehlungsparameter : empfehlungsparameterRepository.findAll()) {
            parameter.put(empfehlungsparameter.getDefinition(), empfehlungsparameter.getWert());
        }
        List<Vorschlag> empfehlungsvorschlaege = algorithmus.generiereNaechsteEmpfehlungsrunde(input, parameter);

        //Speichern der Empfehlungen
        List<Empfehlung> empfehlungen = new ArrayList<>();
        for(Vorschlag vorschlag : empfehlungsvorschlaege) {
            empfehlungen.add(new Empfehlung(benutzer, buecherNachId.get(vorschlag.buchId()), vorschlag.grund(), vorschlag.score(), vorschlag.position(), vorschlag.istExploration()));
        }
        empfehlungRepository.saveAll(empfehlungen);
        return empfehlungen.stream().map(this::zuEmpfehlungsdaten).toList();
    }

    @Transactional
    public void speichereFeedback(Long empfehlungId, Feedback feedback) {
        Empfehlung empfehlung = empfehlungRepository.findById(empfehlungId).orElse(null);
        if(empfehlung != null) {
            empfehlung.setFeedback(feedback);
            empfehlungRepository.save(empfehlung);
        }
    }

    private BuchInfo zuBuchInfo(Buch buch) {
        Map<Long, String> genres = new HashMap<>();
        Map<Long, String> autoren = new HashMap<>();
        for(Genre genre : buch.getGenres()) {
            genres.put(genre.getId(), genre.getBezeichnung());
        }
        for(Autor autor: buch.getAutoren()) {
            autoren.put(autor.getId(), autor.getName());
        }
        return new BuchInfo(buch.getId(), buch.getTitel(), genres, autoren);
    }

    private VerlaufEintrag zuVerlaufEintrag(Empfehlung empfehlung){
        return new VerlaufEintrag(empfehlung.getBuch().getId(), empfehlung.getPosition(), empfehlung.getFeedback());
    }

    private Empfehlungsdaten zuEmpfehlungsdaten(Empfehlung empfehlung) {
        List<String> autorenNamen = new ArrayList<>();
        List<String> genreBezeichnungen = new ArrayList<>();
        for(Autor autor : empfehlung.getBuch().getAutoren()){
            autorenNamen.add(autor.getName());
        }
        for(Genre genre : empfehlung.getBuch().getGenres()){
            genreBezeichnungen.add(genre.getBezeichnung());
        }
        return new Empfehlungsdaten(empfehlung.getEmpfehlungId(), empfehlung.getBuch().getTitel(),
                autorenNamen, genreBezeichnungen, "",
                empfehlung.getGrund(), empfehlung.istExploration());
    }
}
