/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

import org.centrale.infosi.pappl.logement.util.PasswordUtils;
import org.centrale.infosi.pappl.logement.items.Personne;
import org.centrale.infosi.pappl.logement.items.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

/**
 * Repository custom des personnes
 *
 * @author clesp
 */
@Repository
public class PersonneRepositoryCustomImpl implements PersonneRepositoryCustom {

    @Autowired
    @Lazy
    private PersonneRepository personneRepository;

    @Autowired
    @Lazy
    private RoleRepository roleRepository;

    private String generateNewToken() {
        StringBuilder token = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        Calendar aCalendar = Calendar.getInstance();
        Date now = aCalendar.getTime();
        String nowStr = sdf.format(now);

        boolean insertedDate = false;
        for (int i=0; i < 28; i++) {
            int rnd = (int) (Math.random() * 100);
            char c = (char) ('A' + (int) (Math.random() * 26));
            char o = (char) ('0' + (int) (Math.random() * 10));
            if ((! insertedDate) && ((i > 10) || (rnd < 5))) {
                token.append(nowStr);
                insertedDate = true;
            } else if (rnd < 55) {
                token.append(c);
            } else {
                token.append(o);
            }
        }
        return token.toString();
    }

    @Override
    public Personne getByLoginAndPassword(String login, String password) {
        Collection<Personne> result = personneRepository.findByLoginAndPassword(login, password);
        if (result.size() == 1) {
            Iterator<Personne> iterator = result.iterator();
            return iterator.next();
        } else {
            return null;
        }
    }

    @Override
    public Personne update(int id, String firstName, String lastName, String Login, String Password) {
        Personne item = null;
        if (id > 0) {
            item = personneRepository.getReferenceById(id);
        }
        if ((item != null) && (firstName != null) && (lastName != null)) {
            item.setPrenom(firstName);
            item.setNom(lastName);
            item.setLogin(Login);
            personneRepository.saveAndFlush(item);
        }
        return item;
    }

    @Override
    public Personne create(String firstName, String lastName, String Login, String Password) {
        if ((firstName != null) && (lastName != null)) {
            Personne item = new Personne();
            item.setPrenom(firstName);
            item.setNom(lastName);
            item.setLogin(Login);
            PasswordUtils hachage = new PasswordUtils();
            String Password_haché;
            Password_haché = hachage.hashPassword(Password);
            item.setPassword(Password_haché);
            Role role = new Role(3, "Assistant");
            item.setRoleId(role);
            personneRepository.saveAndFlush(item);

            Optional<Personne> result = personneRepository.findById(item.getPersonneId());
            if (result.isPresent()) {
                return result.get();
            }
        }
        return null;
    }

    @Override
    public Personne createEleve(String nom, String prenom) {
        if ((nom != null) && (prenom != null)) {
            Personne item = new Personne();
            item.setNom(nom);
            item.setPrenom(prenom);

            Collection<Role> myList = roleRepository.findByRoleNom("Eleve");
            if (myList.isEmpty()) {
                System.out.println("Erreur: le rôle Eleve n'existe pas from createEleve");
            } else {
                item.setRoleId(myList.iterator().next());
            }

            //Todo add token 
            personneRepository.saveAndFlush(item);

            Optional<Personne> result = personneRepository.findById(item.getPersonneId());

            if (result.isPresent()) {
                return result.get();
            }
        }

        return null;
    }

    @Override
    public void delete(int id) {
        Personne item = null;
        if (id > 0) {
            item = personneRepository.getReferenceById(id);
            personneRepository.delete(item);
        }
    }

    @Override
    public Personne update(Personne personne, String login, String password) {
        if ((personne != null) && (login != null) && (password != null)) {
            personne.setLogin(login);
            personne.setPassword(password);
            personneRepository.saveAndFlush(personne);
        }
        return personne;
    }

    @Override
    public Personne resetPassword(Personne personne) {
        if (personne != null) {
            personne.setPassword(null);
            personneRepository.saveAndFlush(personne);
        }
        return personne;
    }

    @Override
    public Personne updateNoms(int id, String nom, String prenom) {
        Personne personne = personneRepository.getReferenceById(id);
        if (personne != null) {
            personne.setNom(nom);
            personne.setPrenom(prenom);
            personneRepository.saveAndFlush(personne);
            return personne;
        }
        return null;
    }

    @Override
    public Personne setToken(Personne personne) {
        if (personne != null) {
            personne = personneRepository.getReferenceById(personne.getPersonneId());
        }
        if (personne != null) {
            String token = generateNewToken();
            personne.setFirstConnectionToken(token);
            personneRepository.saveAndFlush(personne);
        }
        return personne;
    }

    @Override
    public Personne resetToken(Personne personne) {
        if (personne != null) {
            personne = personneRepository.getReferenceById(personne.getPersonneId());
        }
        if (personne != null) {
            personne.setFirstConnectionToken(null);
            personneRepository.saveAndFlush(personne);
        }
        return personne;
    }
    
    @Override
    public Personne getByLogin(String login) {
        if ((login != null) && (! login.isEmpty())) {
            Collection<Personne> result = personneRepository.findByLogin(login);
            if ((result != null) && (result.size() == 1)) {
                return result.iterator().next();
            }
        }
        return null;
    }
}
