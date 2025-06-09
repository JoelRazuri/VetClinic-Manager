package services.pet;

import models.Pet;
import java.util.List;


public interface PetService {
    void savePet(Pet pet);
    List<Pet> listPets();
    List<Pet> listPetsByClient(int clientId);
}
