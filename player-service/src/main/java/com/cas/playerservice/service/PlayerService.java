/* (C)2023 */
package com.cas.playerservice.service;

import com.cas.playerservice.dto.GenericMessage;
import com.cas.playerservice.dto.PlayerDto;
import com.cas.playerservice.dto.PlayerLoginRequest;
import com.cas.playerservice.dto.PlayerRegisterRequest;

public interface PlayerService {
    public GenericMessage<PlayerDto> register(PlayerRegisterRequest request);

    GenericMessage<PlayerDto> login(PlayerLoginRequest request);

    void startGame();
}
