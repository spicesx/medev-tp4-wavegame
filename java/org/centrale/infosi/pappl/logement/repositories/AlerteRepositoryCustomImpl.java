/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import org.centrale.infosi.pappl.logement.items.Alerte;
import org.centrale.infosi.pappl.logement.items.Formulaire;
import org.centrale.infosi.pappl.logement.items.Statut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

/**
 * Repository custom d'une alerte
 *
 * @author clesp
 */
@Repository
public class AlerteRepositoryCustomImpl implements AlerteRepositoryCustom {

    @Lazy
    @Autowired
    private StatutRepository statutRepository;

    @Lazy
    @Autowired
    private AlerteRepository alerteRepository;

    @Override
    public Alerte create(Formulaire formulaire) {
        if (formulaire != null) {
            // Vérifie que le formulaire existe dans les alertes
            Optional<Alerte> verif = alerteRepository.findByFormulaireId(formulaire);
            if (!verif.isPresent()) {
                // On peut créer l'alerte puisqqu'elle n'existe pas encore
                Collection<Statut> statuts = statutRepository.findByStatutId(Statut.NONTRAITE);
                Statut statutId = (Statut) statuts.iterator().next();

                Alerte alerte = new Alerte();
                alerte.setFormulaireId(formulaire);
                alerte.setStatutId(statutId);
                alerteRepository.saveAndFlush(alerte);
                Optional<Alerte> result = alerteRepository.findByFormulaireId(formulaire);
                if (result.isPresent()) {
                    return result.get();
                }
            }
        }
        return null;

    }

    @Override
    public Alerte update(Formulaire formulaire, String etat) {
        if (formulaire != null) {
            Optional<Alerte> alertes = alerteRepository.findByFormulaireId(formulaire);
            if (!alertes.isEmpty()) {
                Collection<Statut> statuts = statutRepository.findByStatutNom(etat);
                Statut statutId = (Statut) statuts.iterator().next();

                Alerte alerte = alertes.get();
                alerte.setStatutId(statutId);
                alerteRepository.saveAndFlush(alerte);

                return alerte;
            }
        }
        return null;
    }

    @Override
    public Alerte update(Formulaire formulaire, boolean etat) {
        if (formulaire != null) {
            Optional<Alerte> alertes = alerteRepository.findByFormulaireId(formulaire);
            if (!alertes.isEmpty()) {
                Collection<Statut> statuts;
                if (etat) {
                    statuts = statutRepository.findByStatutId(Statut.TRAITE);
                } else {
                    statuts = statutRepository.findByStatutId(Statut.NONTRAITE);
                }
                Statut statutId = (Statut) statuts.iterator().next();

                Alerte alerte = alertes.get();
                alerte.setStatutId(statutId);
                alerteRepository.saveAndFlush(alerte);

                return alerte;
            }
        }
        return null;
    }
}
