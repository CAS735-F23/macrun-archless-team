/* (C)2023 */
package com.cas.challengeservice.service;

import com.cas.challengeservice.dto.*;

public interface ChallengeService {
    GenericMessage<ChallengeDto> getChallenge(ChallengeGetRequest request);
    GenericMessage<ChallengeDto> addChallenge(ChallengeAddRequest request);
    GenericMessage<Void> deleteChallenge(ChallengeDeleteRequest request);
}
