package com.cas.challengeservice.service;

import com.cas.challengeservice.dto.*;

import java.util.List;

public interface BadgeService {
    public GenericMessage<List<BadgeDto>> getBadgeList(BadgeGetRequest request);
    public GenericMessage<BadgeDto> addBadge(BadgeAddRequest request);
    public GenericMessage<BadgeDto> deleteBadge(BadgeDeleteRequest request);
}