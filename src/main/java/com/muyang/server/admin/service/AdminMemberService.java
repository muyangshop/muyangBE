package com.muyang.server.admin.service;

import com.muyang.server.auth.User;
import com.muyang.server.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMemberService {
    private final UserRepository userRepository;
    public List<User> list(){
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}
