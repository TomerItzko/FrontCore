# Changelog

All notable changes to this project are documented in this file.

## [1.0.8-0.7.1.2b] - 2026-03-13

### Added
- Added native BlockFront profile XP rewards at match end through FrontCore's `GAME -> POST_GAME` transition hook, so awarded progress now feeds BF's real profile EXP/rank system instead of a separate leveling path.
- Added direct client profile XP sync after match rewards so the local BlockFront profile sees the updated EXP in time for native post-match progress rendering.
- Added configurable native match XP reward settings in `config/mercfrontcore.json` for rank XP and class XP event rewards, including kills, assists, deaths, headshots, no-scopes, back-stabs, match completion, match wins, and infected win rewards.
- Added persistent player XP storage under `mercfrontcore/players/`, including saved BlockFront global XP, prestige, and per-class XP so match-earned progression survives restarts.
- Added an XP leaderboard GUI with `weekly`, `monthly`, and `all time` tabs, opened by the player-facing `/fc leaderboard` client command.
- Added rolling XP gain history to player XP files so weekly and monthly leaderboard views can be calculated from saved server data instead of only the current total.
- Added pause-menu shortcut buttons for the new leaderboard and the existing gun-skins screen, using BlockFront item icons instead of text rows.

### Changed
- Match-end progression now uses FrontCore-calculated XP rewards written into BF `PlayerCloudData` while still relying on BlockFront's native EXP thresholds, prestige rules, and summary/rank UI.
- Player XP data files are now named with both player name and UUID for easier administration while keeping entries unique across name changes.
- Leaderboard ranking is now server-authoritative and built from the saved `mercfrontcore/players/` data for every player who has earned XP on the server.
- Native XP defaults now follow the configured per-event reward table for rank XP and the separate per-event reward table for class XP.

### Fixed
- Loadout `minimumXp` enforcement now works server-side during live BlockFront matches through a tick-based lock check, preventing players from keeping classes/loadouts whose required class XP is above their current class XP even when BF runtime mixins do not apply on production jars.
- Gun modifier sync now sends only real override entries instead of resending the full default gun modifier set, avoiding invalid fire-mode rewrites.
- Gun fire-mode modifier application now skips empty post-filter fire-mode lists instead of passing a zero-length array into BlockFront and crashing.

## [1.0.7-0.7.1.2b] - 2026-03-12

### Fixed
- Restored BF action packet routing with a guarded bridge so block-break packets still reach vanilla while BF drop/offhand rules stay active.
- Hardened BF Netty player resolution by falling back to the real `packet_handler` player when address-based lookup fails, reducing invalid-entity kicks during interaction handling.
- Strengthened infected vendor re-pair pulses after login and relocate, plus periodic maintenance resyncs to improve vendor visibility after relocate and later re-entry.
- Skipped the dedicated-server BF packet router attach path on integrated singleplayer servers to stop client/world desync issues that broke movement and block breaking offline.
- Reworked winner skin reward triggering to detect real match-end `GAME -> POST_GAME` transitions on the server and recompute winners through BlockFront, restoring reward grants and player chat notifications when the original match-finish hook did not fire.
- Expanded winner reward selection to build from the synced gun option index as well as the live skin index so reward pools do not collapse on servers where the cloud skin registry is late or incomplete at match end.
- Winner reward chat announcements now broadcast reliably to players still in the same match, with the awarded skin name highlighted in rarity color and bold for visibility.

### Added
- Added operator commands to force infected vendor relocation to a configured spawn or directly to the executing player's position for verification.
- Added rarity-aware random skin selection for admin random drops and winner rewards, with rolls done by rarity first and then by eligible gun/skin within that rarity.
- Added an optional admin rarity filter to `randomDrop` so operators can force `coal`, `iron`, `lapis`, `gold`, or `diamond` while still keeping the actual skin random.

### Notes
- Infected vendor relocate has only been verified through local/admin-driven testing so far. Real-match validation is still pending, and `Front-Utilities` uses a deeper `relocateVendor` patch if native BF relocate behavior still proves unreliable under live match conditions.

## [1.0.6-0.7.1.2b] - 2026-03-11

### Added
- Random winner-only gun skin drops at match finish, with an independent roll per victorious player.
- Server-authoritative reward selection that prefers skins the player does not already own and syncs the reward immediately to the client.

### Changed
- Added `rewards.enableWinnerSkinDrops` and `rewards.winnerSkinDropChance` to the server `config/mercfrontcore.json` file.
- Server status output now includes winner skin drop settings for quick verification.
- Random admin drops and winner reward drops now choose a gun first, then only choose from skins validated for that exact gun using the same selection source as `gun skinPlayer`.
- Removed the old `admin spawnView` commands and trimmed server-side `loadout` commands down to `reload`, leaving `loadout editor` and `loadout sync` on the client side.

## [1.0.5-0.7.1.2b] - 2026-03-10

### Fixed
- Profile overrides now apply directly to BlockFront live profile data, reapply on login/reload and after BF profile refreshes, and restore the previous live state when cleared.
- Permanent player gun skins now stay active in matches, while guns given with an explicit skin from the gun menu keep that explicit skin.
- Gun skin commands now fully support namespaced gun ids and per-gun skin tab completion.
- The player gun skin selector now always includes `default` as a selectable option and applies it immediately by clearing the permanent skin override for that gun.
- Lobby commands now discover and join live BlockFront matches more reliably on runtime jars instead of failing with false "no active joinable game" results.
- `/fc lobby ...` is now available to non-op players, while admin-only server commands, `status`, and the client gun give menu stay restricted to operators.
- FrontCore versioning is now presented consistently as `<frontcore>-<BF>`.

### Added
- `/frontcore gun skinPlayer <player> <gun id> <skin>` and `/fc gun skinPlayer ...` for persistent per-player gun skins stored in `player_gun_skins.json`.
- `/frontcore gun removeSkinPlayer <player> <gun id> [skin]` and `/fc gun removeSkinPlayer ...` for removing a player's permanent gun skins, with tab completion limited to the guns and skins that player actually owns.
- `/fc gun skins` and `/frontcore gun skins` so players can browse owned guns, preview skins, and choose their active skin from an in-game GUI.
- `/fc lobby random` plus `/fc lobby dom|ffa|tdm|conq|inf|gg|ttt|boot` and matching `/frontcore lobby ...` aliases to join an active in-progress BlockFront match by mode.
- `/fc admin lobby debug` and per-mode variants to print exactly which BlockFront games the lobby command can currently see and how they are classified on the live server.
- Player-facing polish for the skin GUI, including live preview, aligned navigation controls, and a clearer selected-skin marker.
- A `Gun Skins` button in the vanilla pause menu under `Back to Game`, anchored to the live vanilla layout so it stays on its own row.

## [1.0.4-0.7.1.2b] - 2026-03-09

### Fixed
- Removed FrontCore infected-vendor relocate mixin overrides so vendor behavior remains BlockFront-native.
- Added server-side vendor tracking sync pulses for infected match players after login, forcing vendor pairing packets to players who missed initial tracking.
- Vendor relocation now also re-queues the same server-side tracking sync window for players in the infected match.
- Tuned vendor tracking sync window to 100 ticks to reduce packet noise while preserving reliability.
- Restored custom gun/grenade sound sliders by registering missing client mixins (`SoundOptionsScreenMixin`, `AbstractSoundInstanceMixin`).
- Removed temporary startup build-marker error log (`MFC-BUILD-2026-03-09-DROPRESET-01`).

## [1.0.3-0.7.1.2b] - 2026-03-09

### Changed
- Added explicit BF packet router attach on player login/tick to stabilize server-side packet listener presence.
- Router now keeps BF move/interaction handlers attached and removes BF action handler to allow vanilla action packet flow.
- Reworked server-side drop prevention scope for BF sessions (waiting/active) to reduce packet-level desync risk.

### Fixed
- Addressed missing BF packet routing attachment visibility by adding explicit server log lines for router attach.
- Reduced ghost/desync side effects from direct action-packet interception in current hotfix path.

## [1.0.2-0.7.1.2b] - 2026-03-08

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

## [1.0.1-0.7.1.2b] - 2026-03-08

### Fixed
- Prevented duplicate BlockFront Netty handler injection on Velocity server switches and reconnect paths.
- Added idempotent server-side pipeline insertion handling for BlockFront player join packet handlers.
- Added client login-time pipeline cleanup for stale BlockFront handlers:
  - `mod_packet_handler_time`
  - `mod_packet_handler_custom_payload`
  - `mod_packet_handler_system_chat`
  - `mod_packet_handler_set_chunk_cache_center`
- Added runtime mixin target guard for `BFServerManagerMixin` to avoid missing-target warnings on obfuscated production jars.

## [1.0.0-0.7.1.2b] - 2026-03-08

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
