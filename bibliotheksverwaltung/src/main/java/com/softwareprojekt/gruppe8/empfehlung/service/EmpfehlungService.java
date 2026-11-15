package com.softwareprojekt.gruppe8.empfehlung.service;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpfehlungService {
    public List<Empfehlungsdaten> ladeNeueEmpfehlungen(){
        //TODO: Durch echte Empfehlungen aus dem spaeteren Algorithmus ersetzen

        return List.of(new Empfehlungsdaten(1L, "Harry Potter und der Stein der Weisen", "J.K. Rowling", List.of("Fantasy", "Jugend", "Roman"), "", "Du magst Fantasy.", false),
                new Empfehlungsdaten(2L, "Harry Potter und die Kammer des Schreckens", "J.K. Rowling", List.of("Fantasy", "Jugend", "Roman"), "", "Du magst Fantasy.", false),
                new Empfehlungsdaten(3L, "Harry Potter und der Gefangene von Azkaban", "J.K. Rowling", List.of("Fantasy", "Jugend", "Roman"), "", "Du magst Fantasy.", false),
                new Empfehlungsdaten(4L, "ES", "Stephen King", List.of("Horror"), "", "Mal was Neues.", true),
                new Empfehlungsdaten(5L, "Harry Potter und der Orden des Phoenix", "J.K. Rowling", List.of("Fantasy", "Jugend", "Roman"), "", "Du magst Fantasy.", false),
                new Empfehlungsdaten(6L, "Harry Potter und der Halbblutprinz", "J.K. Rowling", List.of("Fantasy", "Jugend", "Roman"), "", "Du magst Fantasy.", false),
                new Empfehlungsdaten(7L, "Harry Potter und die Heiligtümer des Todes", "J.K. Rowling", List.of("Fantasy", "Jugend", "Roman"), "", "Du magst Fantasy.", false),
                new Empfehlungsdaten(8L, "Sherlock Holmes: Eine Studie in Scharlachrot", "Arthur Conan Doyle", List.of("Mystery", "Detektiv"), "", "Du magst Arthur Conan Doyle.", false),
                new Empfehlungsdaten(9L, "Friedhof der Kuscheltiere", "Stephen King", List.of("Horror"), "", "Mal was Neues.", true),
                new Empfehlungsdaten(10L, "To Kill a Mockingbird", "Harper Lee", List.of("Klassische Belletristik", "Historischer Roman"), "", "Du magst Romane.", false)
        );
    }
}
