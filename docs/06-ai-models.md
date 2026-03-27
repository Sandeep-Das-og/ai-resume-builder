# AI Model Options (Free + Safer Defaults)

## Recommendation (MVP)
- Use open-weight models with Apache 2.0 licenses to reduce legal friction.
- Start with smaller models for cost: Qwen2.5-1.5B or Qwen2.5-7B (Apache 2.0).

## Deployment Modes
1. Local inference (best privacy)
- Run the model on your own server (no third-party data sharing).
- Requires more RAM/CPU, and ideally a GPU for low latency.

2. External free-tier inference
- Use a free-tier hosted inference API.
- Requires explicit user consent since resume content is sent to a third party.

## Notes
- Qwen2.5 model card shows Apache 2.0 licensing for several sizes (0.5B, 1.5B, 7B). See references.
- Ollama has a large model library that includes Qwen variants, useful for local inference.

## References
- Qwen2.5 model card (license table)
- Ollama model library list
