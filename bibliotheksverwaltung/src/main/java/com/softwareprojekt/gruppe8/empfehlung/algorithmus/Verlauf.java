package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

import com.softwareprojekt.gruppe8.empfehlung.domain.Feedback;

import java.util.*;

public class Verlauf {
    private final Map<Long, VerlaufEintrag> verlaufEintraege;
    public Verlauf(List<VerlaufEintrag> verlauf) {
        verlaufEintraege = new HashMap<>();
        for(VerlaufEintrag eintrag : verlauf) {
            verlaufEintraege.merge(eintrag.buchId(), eintrag, (aktuellerEintrag, neuerEintrag)
                    -> neuerEintrag.istNeuerAls(aktuellerEintrag) ? neuerEintrag : aktuellerEintrag);
        }
    }
    public boolean hatFeedback(Long buchId, Feedback feedback){
        VerlaufEintrag eintrag = verlaufEintraege.get(buchId);
        return eintrag != null && eintrag.feedback() == feedback;
    }
    public boolean wurdeKuerzlichEmpfohlen(Long buchId, int naechstePosition, int mindestAbstand){
        VerlaufEintrag eintrag =  verlaufEintraege.get(buchId);
        return eintrag != null && naechstePosition - eintrag.position() < mindestAbstand;
    }
    public boolean wurdeEmpfohlen(Long buchId){
        return verlaufEintraege.containsKey(buchId);
    }
}
