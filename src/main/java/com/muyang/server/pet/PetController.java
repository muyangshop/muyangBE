package com.muyang.server.pet;

import com.muyang.server.auth.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;
    private Long uid(User u){ return u == null ? null : u.getId();}
    @GetMapping
    public List<Pet> list(@AuthenticationPrincipal User user){
        return petService.list(uid(user));
    }
    @PostMapping
    public Pet create(@AuthenticationPrincipal User user, @Valid @RequestBody PetRequest req){
        return petService.create(uid(user), req);
    }
    @PutMapping("/{id}")
    public Pet update(@AuthenticationPrincipal User user, @PathVariable Long id, @Valid @RequestBody PetRequest req){
        return petService.update(uid(user), id, req);
    }
    @DeleteMapping("/{id}")
    public void delete(@AuthenticationPrincipal User user, @PathVariable Long id){
        petService.delete(uid(user), id);
    }
}
