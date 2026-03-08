# Changelog

All notable changes to this project are documented in this file.

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
