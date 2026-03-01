package com.pgsc.tracko.ai

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.stereotype.Service

@Service
@ConditionalOnMissingBean(AIService::class)
class PlaceholderAIService : AIService {

    override fun healthCheck(): Boolean = true
}
