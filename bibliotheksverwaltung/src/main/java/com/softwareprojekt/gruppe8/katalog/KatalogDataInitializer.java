package com.softwareprojekt.gruppe8.katalog;

import com.softwareprojekt.gruppe8.katalog.entity.Autor;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.katalog.entity.Genre;
import com.softwareprojekt.gruppe8.katalog.entity.Verlag;
import com.softwareprojekt.gruppe8.katalog.repository.AutorRepository;
import com.softwareprojekt.gruppe8.katalog.repository.BuchRepository;
import com.softwareprojekt.gruppe8.katalog.repository.GenreRepository;
import com.softwareprojekt.gruppe8.katalog.repository.VerlagRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Configuration
public class KatalogDataInitializer {

    @Bean
    CommandLineRunner initKatalogDaten(BuchRepository buchRepo,
                                    AutorRepository autorRepo,
                                    GenreRepository genreRepo,
                                    VerlagRepository verlagRepo) {
        return args -> {
            // Nur importieren, wenn die Datenbank noch leer ist
            if (buchRepo.count() > 0) {
                return;
            }

            Map<String, Verlag> verlagCache = new HashMap<>();
            Map<String, Autor> autorCache = new HashMap<>();
            Map<String, Genre> genreCache = new HashMap<>();
            List<Buch> buecher = new ArrayList<>();

            ClassPathResource resource = new ClassPathResource("buecher_seed.csv");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                // Kopfzeile überspringen
                String line = reader.readLine();

                while ((line = reader.readLine()) != null) {
                    if (line.isBlank()) continue;

                    String[] parts = line.split(";");
                    String titel = parts[0].trim();
                    String isbn = parts[1].trim();
                    int seitenzahl = Integer.parseInt(parts[2].trim());
                    String verlagName = parts[3].trim();
                    String[] autorNamen = parts[4].split("\\|");
                    String[] genreNamen = parts[5].split("\\|");

                    // Verlag holen oder neu anlegen
                    Verlag verlag = verlagCache.computeIfAbsent(verlagName, name -> {
                        Verlag v = new Verlag();
                        v.setName(name);
                        return verlagRepo.save(v);
                    });

                    // Autoren holen oder neu anlegen
                    Set<Autor> autoren = new HashSet<>();
                    for (String aName : autorNamen) {
                        String cleanName = aName.trim();
                        Autor autor = autorCache.computeIfAbsent(cleanName, name -> {
                            Autor a = new Autor();
                            a.setName(name);
                            return autorRepo.save(a);
                        });
                        autoren.add(autor);
                    }

                    // Genres holen oder neu anlegen
                    Set<Genre> genres = new HashSet<>();
                    for (String gName : genreNamen) {
                        String cleanGenre = gName.trim();
                        Genre genre = genreCache.computeIfAbsent(cleanGenre, name -> {
                            Genre g = new Genre();
                            g.setBezeichnung(name);
                            return genreRepo.save(g);
                        });
                        genres.add(genre);
                    }

                    Buch buch = new Buch();
                    buch.setTitel(titel);
                    buch.setIsbn(isbn);
                    buch.setSeitenzahl(seitenzahl);
                    buch.setVerlag(verlag);
                    buch.setAutoren(autoren);
                    buch.setGenres(genres);

                    buecher.add(buch);
                }
            }

            buchRepo.saveAll(buecher);
            System.out.println("✅ " + buecher.size() + " echte Bücher erfolgreich aus CSV importiert!");
        };
    }
}