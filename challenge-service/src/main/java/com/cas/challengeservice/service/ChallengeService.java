/* (C)2023 */
package com.cas.challengeservice.service;

import com.cas.challengeservice.dto.*;

public interface ChallengeService {
    GenericMessage<ChallengeTypeDto> getChallenge(ChallengeGetRequest request);

    GenericMessage<ChallengeTypeDto> addChallenge(ChallengeAddRequest request);

    GenericMessage<ChallengeTypeDto> deleteChallenge(ChallengeDeleteRequest request);
}
