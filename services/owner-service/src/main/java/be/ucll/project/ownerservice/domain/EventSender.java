package be.ucll.project.ownerservice.domain;

public interface EventSender {
    void  sendOwnerCreatedEvent(Owner owner);
}
