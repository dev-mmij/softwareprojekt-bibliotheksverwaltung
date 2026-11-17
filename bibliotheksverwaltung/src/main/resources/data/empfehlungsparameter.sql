INSERT INTO empfehlungsparameter (kennung, wert, standardwert, beschreibung) VALUES
    ('STARTWERT_LIEBLINGSGENRE', 0.5, 0.5, 'Startwert für jedes Genre, das als Lieblingsgenre ausgewählt wurde.'),
    ('DAUMEN_HOCH_GENRE', 0.1, 0.1, 'Erhöhung pro Genre bei Daumen hoch'),
    ('DAUMEN_HOCH_AUTOR', 0.15, 0.15, 'Erhöhung pro Autor bei Daumen hoch'),
    ('DAUMEN_RUNTER_GENRE', 0.05, 0.05, 'Senkung pro Genre bei Daumen runter'),
    ('DAUMEN_RUNTER_AUTOR', 0.05, 0.05, 'Senkung pro Autor bei Daumen runter'),
    ('BONUS_GEFOLGTER_AUTOR', 0.25, 0.25, 'Bonus für Autoren, denen der Nutzer folgt'),
    ('GEWICHT_MIN', -1.0, -1.0, 'Untere Grenze eines Interessenwerts'),
    ('GEWICHT_MAX', 1.0, 1.0, 'Obere Grenze eines Interessenwerts'),
    ('ANTEIL_AUTOR', 0.3, 0.3, 'Wie stark die Autoren in die Empfehlungen zählen'),
    ('ZUFALL_MAX', 0.2, 0.2, 'Größter Zufallsanteil im Punktewert'),
    ('ENTDECKUNGSPLAETZE', 2, 2, 'Entdeckungskarten pro 10 Karten'),
    ('MINDESTABSTAND_WIEDERHOLUNG', 20, 20, 'Anzahl Karten, nach denen ein Buch frühstens wiederkommt');