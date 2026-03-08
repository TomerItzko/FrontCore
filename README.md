# FrontCore

`FrontCore` is a companion utility mod for BlockFront focused on offline flow control, loadout editing, and advanced admin/setup helpers.

Credit to the OG author: [`forteus19`](https://github.com/forteus19/Front-Utilities).

## In-Game Usage

## 1) Offline Flow Behavior

- Launch game normally: BlockFront menu appears first.
- Choose offline mode in BlockFront.
- `FrontCore` redirects to the vanilla Minecraft title screen.

## 2) Key Commands

- Base command: `/frontcore`
- Alias: `/fc`

- Status:
`/frontcore status` or `/fc status`

- Profile overrides:
`/frontcore profile show|save|reload`
`/frontcore profile set <target> <displayName> <level> <prestige>`
`/frontcore profile clear <target>`

- Gun tools:
`/frontcore gun giveWithSkin <id> <skin>`
`/frontcore gun modifier list`

- Loadouts:
`/frontcore loadout list|save|reload`
`/frontcore loadout set <name> <primary> <secondary>`
`/frontcore loadout remove <name>`
`/frontcore loadout give <target> <name>`

- Admin:
`/frontcore admin proxy show|reload|save`
`/frontcore admin proxy set compatibility <true|false>`
`/frontcore admin proxy set directOnly <true|false>`
`/frontcore admin proxy set trustForwardedIdentity <true|false>`
`/frontcore admin randomDrop <players> [count]`
`/frontcore admin spawnView enable <game>`

## 3) Typical Workflow

- Start BlockFront and enter offline mode.
- Use loadout/profile commands to apply your setup.
- Use admin subcommands for proxy compatibility and spawn/debug utilities.

## 4) Sound Sliders (Client-Side)

- Sound sliders are in Minecraft sound options:
`Gun SFX Volume` and `Grenade SFX Volume`.
- These sliders are **client-only** (local player preference).
- Scope: BlockFront gun/grenade/rocket/explosion style sounds.
- Behavior:
`0%` mutes the target category.
`1-100%` scales target category volume.
- The rest of game audio is not changed.

## 5) Requirements

- Minecraft `1.21.1`
- NeoForge `21.1.x`
- BlockFront `0.7.1.2b`

## 6) Build (for local development)

- Follow setup notes in `bf/README.md`.
- Build with:
`gradlew.bat build`
