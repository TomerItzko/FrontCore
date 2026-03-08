# Changelog

All notable changes to this project are documented in this file.

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
