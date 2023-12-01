package be.ucll.project.ownerservice.web;

import be.ucll.project.ownerservice.api.OwnerApiDelegate;
import be.ucll.project.ownerservice.api.model.ApiOwner;
import be.ucll.project.ownerservice.api.model.ApiOwners;
import be.ucll.project.ownerservice.domain.Owner;
import be.ucll.project.ownerservice.domain.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class OwnerController implements OwnerApiDelegate {

    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }
    @Override
    public ResponseEntity<ApiOwners> getOwners() {
        ApiOwners owners = new ApiOwners();
        owners.setOwners(ownerService.getOwners().stream()
                        .map(this::toDto)
                        .toList()
        );

        return ResponseEntity.ok(owners);
    }

    @Override
    public ResponseEntity<ApiOwner>createOwner(ApiOwner owner) {

        return ResponseEntity.ok(toDto(ownerService.createOwner(owner)));
    }

    private ApiOwner toDto(Owner owner) {
        return new ApiOwner()
                .id(owner.getId())
                .name(owner.getName())
                .email(owner.getEmail());
    }


}
