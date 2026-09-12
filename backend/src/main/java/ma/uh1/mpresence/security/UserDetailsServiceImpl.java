package ma.uh1.mpresence.security;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Résout le principal aussi bien pour un compte Admin/Formateur (identifié
 * par son CIN, voir {@link ma.uh1.mpresence.entity.User}) que pour un
 * Doctorant (identifié par son Code Apogée, voir
 * {@link ma.uh1.mpresence.entity.Doctorant}) : les deux implémentent
 * UserDetails et partagent le même mécanisme JWT.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final DoctorantRepository doctorantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByCinIgnoreCase(username)
                .<UserDetails>map(u -> u)
                .or(() -> doctorantRepository.findByCodeApogeeIgnoreCase(username).map(d -> d))
                .orElseThrow(() -> new UsernameNotFoundException("Compte introuvable: " + username));
    }
}
