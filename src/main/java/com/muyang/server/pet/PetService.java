package com.muyang.server.pet;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PetService {
    private final PetRepository petRepository;
    @Transactional(readOnly = true)
    public List<Pet>list(Long userId){
        requireUser(userId);
        return petRepository.findByUserIdOrderByIdAsc(userId);
    }
    public Pet create(Long userId, PetRequest req){
        requireUser(userId);
        return petRepository.save(Pet.builder()
                .userId(userId)
                .name(req.name())
                .species(req.species())
                .age(req.age())
                .build());
    }
    public Pet update(Long userId, Long petId, PetRequest req){
        Pet pet = getOwned(userId, petId);
        pet.setName(req.name());
        pet.setSpecies(req.species());
        pet.setAge(req.age());
        return  petRepository.save(pet);
    }
    public void delete(Long userId, Long petId){
        petRepository.delete(getOwned(userId, petId));
    }
    private Pet getOwned(Long userId, Long petId){
        requireUser(userId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "반려동물을 찾을 수 없습니다."));
        if(!pet.getUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 반려동물만 관리할 수 있습니다.");
        }
        return pet;
    }
    private void requireUser(Long userId){
        if(userId == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }
}
