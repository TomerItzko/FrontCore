# FrontCore Safety Rules

This file defines high-risk areas that must not be changed casually.

## 1) BF Packet Pipeline (Critical)

Do not change these without full in-game validation (move, interact, break, drop):

- `src/main/java/dev/tomerdev/mercfrontcore/server/net/BfPacketRouter.java`
- `src/main/java/dev/tomerdev/mercfrontcore/mixin/PacketListenerPlayerMoveMixin.java`
- `src/main/java/dev/tomerdev/mercfrontcore/mixin/PacketListenerInteractionMixin.java`
- `src/main/java/dev/tomerdev/mercfrontcore/mixin/PacketListenerPlayerActionMixin.java`
- `src/main/resources/mercfrontcore.mixins.json` (server mixin activation order)

Rules:
- Never swallow packets when player lookup fails; forward downstream.
- Avoid broad packet cancellation in BF listeners unless absolutely required.
- Do not re-enable `BFServerManagerMixin` unless needed and tested on the target server build.

## 2) Drop Blocking (High Risk)

Do not reintroduce direct drop packet cancellation without proving no item-desync/ghost-loss:

- `src/main/java/dev/tomerdev/mercfrontcore/mixin/ServerPlayNetworkHandlerMixin.java`
- `src/main/java/dev/tomerdev/mercfrontcore/server/event/MercFrontCoreServerEvents.java` (`ItemTossEvent` handling)

Rules:
- Match-only restrictions must use stable BF session checks.
- Prefer server-authoritative toss cancel + restore over destructive client/server race paths.

## 3) Match Session Detection (High Risk)

Sensitive file:
- `src/main/java/dev/tomerdev/mercfrontcore/util/MatchCompat.java`

Rules:
- Any failure here can disable protections or break normal gameplay.
- Keep checks null-safe and exception-safe.

## 4) Build/Runtime Verification (Required)

Sensitive file:
- `src/main/java/dev/tomerdev/mercfrontcore/MercFrontCore.java`

Rules:
- Keep a visible startup marker log when debugging deployment mismatches.
- Verify server is running expected jar before diagnosing gameplay logic.

## 5) Release Discipline

Before shipping any packet/drop/gameplay change:

1. Validate outside match: move, interact, break, drop.
2. Validate waiting for players: move, interact, break, drop restrictions.
3. Validate active match: move, interact, break, drop restrictions.
4. Validate death/respawn (no kick regression).
5. Update `CHANGELOG.md`.

If any of the above fail, revert the risky change first, then reintroduce incrementally.
