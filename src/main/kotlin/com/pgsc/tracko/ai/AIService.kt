package com.pgsc.tracko.ai

/**
 * Boundary for AI-related operations.
 * Isolated from core domain; implement when integrating with an AI provider.
 */
interface AIService {

    /**
     * Placeholder for future AI features (e.g. ticket summarization, categorization).
     */
    fun healthCheck(): Boolean
}
