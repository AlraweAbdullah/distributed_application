package be.ucll.project.userservice.domain;

import be.ucll.project.userservice.api.model.ApiUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(ApiUser data) {
        if(data.getEmail() == null || data.getEmail().trim().isEmpty()){
            throw new UserException("add","email can't  be empty.");
        }

        if(data.getName() == null || data.getName().trim().isEmpty()){
            throw new UserException("add","name can't be empty.");
        }

        if(repository.findByEmail(data.getEmail()) != null){
            throw new UserException("add","you already have an account with email " + data.getEmail() + ".");
        }

        return repository.save(new User(
                data.getName(),
                data.getEmail()));

    }

    public List<User> getUsers(){
        List<User> usersList = new ArrayList<>();

        repository.findAll().forEach(usersList::add);
        return  usersList;
    }


    public User validateUser(String userEmail) {
       return repository.findByEmail(userEmail);
    }
}
