# Changelog

All notable changes to this project are documented in this file.

## [0.7.1.2b-hotfix4] - 2026-03-09

### Fixed
- Removed FrontCore infected-vendor relocate mixin overrides so vendor behavior remains BlockFront-native.
- Added server-side vendor tracking sync pulses for infected match players after login, forcing vendor pairing packets to players who missed initial tracking.
- Tuned vendor tracking sync window to 100 ticks to reduce packet noise while preserving reliability.
- Restored custom gun/grenade sound sliders by registering missing client mixins (`SoundOptionsScreenMixin`, `AbstractSoundInstanceMixin`).
- Removed temporary startup build-marker error log (`MFC-BUILD-2026-03-09-DROPRESET-01`).

## [0.7.1.2b-hotfix3] - 2026-03-09

### Changed
- Added explicit BF packet router attach on player login/tick to stabilize server-side packet listener presence.
- Router now keeps BF move/interaction handlers attached and removes BF action handler to allow vanilla action packet flow.
- Reworked server-side drop prevention scope for BF sessions (waiting/active) to reduce packet-level desync risk.

### Fixed
- Addressed missing BF packet routing attachment visibility by adding explicit server log lines for router attach.
- Reduced ghost/desync side effects from direct action-packet interception in current hotfix path.

## [0.7.1.2b-hotfix2] - 2026-03-08

### Fixed
- Loadout editor sync now sends a stable snapshot to server before local reload, preventing sync-time loss.
- Loadout persistence now survives restarts more reliably:
  - load/apply moved to server started phase,
  - sync path saves explicit snapshot payload,
  - shutdown save path hardened with cached snapshot fallback.
- Loadout editor JSON now stores gun component data (`scope`, `magType`, `barrelType`, `skin`) instead of only item ids.
- Fixed editor extra-slot save behavior (`ItemStack` emptiness checks + copy-on-save).
- Infected vendor persistence hardened:
  - vendor is marked persistent,
  - vendor chunk is force-loaded,
  - forced chunk now follows relocation (unforce old chunk, force new chunk).
- Gun sound slider now also scales hit-confirm sounds (hitmarker/headshot/kill patterns).
- AFK false triggers during matches addressed with server-tick keepalive that marks active in-game players as moved each tick.

## [0.7.1.2b-hotfix1] - 2026-03-08

### Fixed
- Prevented duplicate BlockFront Netty handler injection on Velocity server switches and reconnect paths.
- Added idempotent server-side pipeline insertion handling for BlockFront player join packet handlers.
- Added client login-time pipeline cleanup for stale BlockFront handlers:
  - `mod_packet_handler_time`
  - `mod_packet_handler_custom_payload`
  - `mod_packet_handler_system_chat`
  - `mod_packet_handler_set_chunk_cache_center`
- Added runtime mixin target guard for `BFServerManagerMixin` to avoid missing-target warnings on obfuscated production jars.

## [0.7.1.2b] - 2026-03-08

### Added
- Dedicated client sound scaling path via `AbstractSoundInstance` mixin.
- `SoundVolumeResolver` to classify and scale BlockFront gun/grenade sound groups.
- README documentation for client-only sound slider behavior.

### Changed
- Sound controls are now strictly client-side in behavior and documentation.
- README command reference updated to match actual `/frontcore` and `/fc` command tree.
- Server status output no longer includes client audio values.

### Fixed
- Removed unstable event-time sound mutations that caused random global mute and loop issues.
- Removed `SoundInstance` proxy wrapping path that caused repeated scaling/loop side effects.
- Restored predictable slider behavior:
  - `0%` mutes category.
  - `1-100%` scales category volume.
  - Non-target audio remains unchanged.
- Project name updated from `MERCFront-core` to `FrontCore` in user-facing metadata and docs.
- Build artifact base name updated to `FrontCore`.
- `neoforge.mods.toml` display name updated to `FrontCore`.
- Credit text added and standardized to the OG author: `forteus19` (`https://github.com/forteus19/Front-Utilities`).
- License metadata in `neoforge.mods.toml` changed from `MIT` to `GPL-3.0`.
- Technical mod id remains `mercfrontcore` for compatibility.
