package be.ucll.project.ownerservice.domain;

import be.ucll.project.ownerservice.api.model.ApiOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OwnerService {

    private final OwnerRepository repository;
    private final EventSender eventSender;

    @Autowired
    public OwnerService(OwnerRepository repository, EventSender eventSender) {
        this.repository = repository;
        this.eventSender = eventSender;
    }

    public Owner createOwner(ApiOwner data) {
        if(data.getEmail() == null || data.getEmail().trim() == ""){
            throw new ServiceException("add","email can't  be empty.");
        }

        if(data.getName() == null || data.getName().trim() == ""){
            throw new ServiceException("add","name can't be empty.");
        }

        if(repository.findByEmail(data.getEmail()) != null){
            throw new ServiceException("add","you already have an account with email " + data.getEmail() + ".");
        }

        Owner owner = repository.save(new Owner(
                data.getName(),
                data.getEmail()));
        eventSender.sendOwnerCreatedEvent(owner);

        return owner;
    }

    public List<Owner> getOwners(){
        List<Owner> ownersList = new ArrayList<>();

        repository.findAll().forEach(ownersList::add);
        return  ownersList;
    }


}
