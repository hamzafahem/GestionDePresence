package ma.uh1.mpresence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Doctorant (candidat au doctorat) géré par l'administration CEDoc.
 * Fiche gérée en CRUD par l'admin, mais AUSSI un compte de connexion pour
 * l'app mobile : le doctorant se connecte avec son "Code Apogée" (username)
 * + mot de passe, pour consulter ses formations et son historique de
 * présence (voir hp/carte-d et hp/qrcode côté front).
 */
@Entity
@Table(name = "doctorants")
@Getter
@Setter
@NoArgsConstructor
public class Doctorant implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true)
    private String email;

    private String mobile;

    @Column(unique = true)
    private String cin;

    @Column(name = "cne", unique = true)
    private String cne;

    /**
     * Identifiant scanné/tapé côté front (hp/qrcode, hp/carte-d) pour marquer
     * une présence à un module, ET identifiant de connexion (username) pour
     * l'app mobile.
     */
    @Column(name = "code_apogee", nullable = false, unique = true)
    private String codeApogee;

    /**
     * Mot de passe de connexion (nullable : un doctorant créé par l'admin
     * sans mot de passe ne peut simplement pas se connecter à l'app).
     */
    private String password;

    private String filiere;

    private String nationalite = "Marocaine";

    private String photo;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(length = 2000)
    private String details;

    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDoctorant statut = StatutDoctorant.ACTIF;

    /**
     * Formateur encadrant (directeur de thèse) affecté à ce doctorant.
     */
    @ManyToOne
    @JoinColumn(name = "encadrant_user_id")
    private User encadrant;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Embedded
    private UserSettings settings = new UserSettings();

    // ---- UserDetails ----

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_DOCTORANT"));
    }

    @Override
    public String getUsername() {
        return codeApogee;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return statut != StatutDoctorant.SUSPENDU && !settings.isAccountDeactivated();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
