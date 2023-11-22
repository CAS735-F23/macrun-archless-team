/* (C)2023 */
package com.cas.playerservice.service;

import com.cas.playerservice.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface PlayerService {
    public GenericMessage<PlayerDto> register(PlayerRegisterRequest request);

    GenericMessage<PlayerDto> login(PlayerRequest request);

    GenericMessage<Object> logout(PlayerRequest request);

    GenericMessage<Object> setLocation(PlayerSetLocationRequest request);

    Boolean isPlayerLoggedIn(String username);
}
