package services.pet;

import services.pet.PetService;
import models.Pet;
import java.util.ArrayList;
import java.util.List;


public class PetServiceImpl implements PetService {
    private static List<Pet> pets = new ArrayList<>();

    @Override
    public void savePet(Pet pet) {
        pets.add(pet);
    }

    @Override
    public List<Pet> listPets() {
        return pets;
    }

    @Override
    public List<Pet> listPetsByClient(int clientId) {
        List<Pet> result = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getIdClient() == clientId) {
                result.add(pet);
            }
        }
        return result;
    }
}
