package com.muyang.server.address;

import com.muyang.server.address.AddressRequest;
import com.muyang.server.address.Address;
import com.muyang.server.auth.User;
import com.muyang.server.address.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private Long uid(User u){ return u == null ? null : u.getId();}
    @GetMapping
    public List<Address> list(@AuthenticationPrincipal User user){
        return addressService.list(uid(user));
    }
    @PostMapping
    public Address create(@AuthenticationPrincipal User user, @Valid @RequestBody AddressRequest req){
        return addressService.create(uid(user), req);
    }
    @PutMapping("/{id}")
    public Address update(@AuthenticationPrincipal User user, @PathVariable Long id, @Valid @RequestBody AddressRequest req){
        return addressService.update(uid(user), id, req);
    }
    @DeleteMapping("/{id}")
    public void delete(@AuthenticationPrincipal User user, @PathVariable Long id){
        addressService.delete(uid(user), id);
    }
    @PostMapping("/{id}/default")
    public Address setDefault(@AuthenticationPrincipal User user, @PathVariable Long id){
        return addressService.setDefault(uid(user), id);
    }
}
