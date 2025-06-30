package services.pet;

import models.Pet;
import java.util.List;

public interface PetService {
    void createPet();
    List<Pet> readAllPets();
    Pet findPetById(int id);
    void updatePet(int id);
    void deletePet(int id);
    void listPetsByClient();
}