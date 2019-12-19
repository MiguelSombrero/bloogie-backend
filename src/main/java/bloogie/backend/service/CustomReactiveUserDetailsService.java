
//package bloogie.backend.service;
//
//import java.util.stream.Collectors;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//import bloogie.backend.repository.ReactiveAccountRepository;
//import org.springframework.security.core.userdetails.User;
//
///**
// *
// * @author miika
// */
//
//@Service()
//public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {
//
//    @Autowired
//    ReactiveAccountRepository accountRepository;
//
//    @Override
//    public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
//        return accountRepository.findByUsername(username).switchIfEmpty(Mono.defer(() -> {
//            return Mono.error(new UsernameNotFoundException("Username " + username + " not found!"));
//            
//        })).map(account -> new User(
//                account.getUsername(),
//                account.getPassword(),
//                true,
//                true,
//                true,
//                true,
//                account.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
//
//    }
//}
//
