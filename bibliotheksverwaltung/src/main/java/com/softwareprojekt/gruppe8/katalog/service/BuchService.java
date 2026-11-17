package com.softwareprojekt.gruppe8.katalog.service;

import com.softwareprojekt.gruppe8.katalog.entity.Autor;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.katalog.entity.Genre;
import com.softwareprojekt.gruppe8.katalog.entity.Verlag;
import com.softwareprojekt.gruppe8.katalog.repository.AutorRepository;
import com.softwareprojekt.gruppe8.katalog.repository.BuchRepository;
import com.softwareprojekt.gruppe8.katalog.repository.GenreRepository;
import com.softwareprojekt.gruppe8.katalog.repository.VerlagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) //Verknüpfte Daten wie Autor, Verlag und Genre werden mitgeladen, um LazyInitializationException in Vaadin zu vermeiden
public class BuchService {

    private final BuchRepository buchRepository;
    private final AutorRepository autorRepository;
    private final GenreRepository genreRepository;
    private final VerlagRepository verlagRepository;

    public BuchService(BuchRepository buchRepository,
                    AutorRepository autorRepository,
                    GenreRepository genreRepository,
                    VerlagRepository verlagRepository) {
        this.buchRepository = buchRepository;
        this.autorRepository = autorRepository;
        this.genreRepository = genreRepository;
        this.verlagRepository = verlagRepository;
    }

    public List<Buch> findAllBuecher() {
        return buchRepository.findAll();
    }

    public List<Buch> searchByTitel(String suchbegriff) {
        if (suchbegriff == null || suchbegriff.isBlank()) {
            return buchRepository.findAll();
        }
        return buchRepository.findByTitelContainingIgnoreCase(suchbegriff.trim());
    }

    public Optional<Buch> findById(Long id) {
        return buchRepository.findWithDetailsById(id);
    }

    @Transactional
    public Buch saveBuch(Buch buch) {
        if (buch == null) {
            throw new IllegalArgumentException("Buch darf nicht null sein");
        }
        return buchRepository.save(buch);
    }

    @Transactional
    public void deleteBuch(Buch buch) {
        if (buch != null) {
            buchRepository.delete(buch);
        }
    }

    public List<Verlag> findAllVerlage() {
        return verlagRepository.findAll();
    }

    public List<Autor> findAllAutoren() {
        return autorRepository.findAll();
    }

    public List<Genre> findAllGenres() {
        return genreRepository.findAll();
    }
}
