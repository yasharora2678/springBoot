package authentication.jwt.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import authentication.jwt.Entity.UserRegisterEntity;
import authentication.jwt.Repository.UserRegisterEntityRepository;

@Service
public class UserRegisterEntityService implements UserDetailsService {
    @Autowired
    UserRegisterEntityRepository userRegisterEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return userRegisterEntityRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    public UserDetails save(UserRegisterEntity userRegisterEntity) {
        return userRegisterEntityRepository.save(userRegisterEntity);
    }
}
