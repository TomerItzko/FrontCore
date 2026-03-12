# FrontCore

`FrontCore` is a companion utility mod for BlockFront focused on offline flow control, loadout editing, persistent gun skins, lobby helpers, and advanced admin/setup tools.

Credit to the OG author: [`forteus19`](https://github.com/forteus19/Front-Utilities).

## In-Game Usage

## 1) Offline Flow Behavior

- Launch game normally: BlockFront menu appears first.
- Choose offline mode in BlockFront.
- `FrontCore` redirects to the vanilla Minecraft title screen.

## 2) Key Commands

- Main command: `/frontcore`
- Short alias: `/fc`

### Player Commands

- Open the permanent gun skin selector:
`/fc gun skins`
- Join a live match by mode:
`/fc lobby random`
`/fc lobby ffa|tdm|dom|conq|inf|gg|ttt|boot`

### Admin Server Commands

- Check current FrontCore state:
`/fc status`
- Manage profile overrides:
`/fc profile show|save|reload`
`/fc profile set <target> <displayName> <level> <prestige>`
`/fc profile clear <target>`
- Manage gun skins and modifiers:
`/fc gun giveWithSkin <id> <skin>`
`/fc gun skinPlayer <player> <gun id> <skin>`
`/fc gun removeSkinPlayer <player> <gun id> [skin]`
`/fc gun modifier list`
- Manage loadouts:
`/fc loadout reload`
- Manage proxy and lobby debugging:
`/fc admin proxy show|reload|save`
`/fc admin proxy set compatibility <true|false>`
`/fc admin proxy set directOnly <true|false>`
`/fc admin proxy set trustForwardedIdentity <true|false>`
`/fc admin lobby debug|random|ffa|tdm|dom|conq|inf|gg|ttt|boot`
`/fc admin infected vendor relocate`
`/fc admin infected vendor here`
`/fc admin randomDrop <players> [count]`

### Admin Client Commands

- Open the give-menu flow:
`/fc gun giveMenu <item>`
- Open or sync the loadout editor:
`/fc loadout editor`
`/fc loadout sync`
- Inspect gun debug options:
`/fc admin gun debugOptions <item>`

### Access Rules

- `/fc status`, all `admin` subcommands, and the client `gun giveMenu` flow are operator-only.

## 3) Typical Workflow

- Start BlockFront and enter offline mode.
- Use `/frontcore gun skins` to choose a permanent skin for supported guns.
- Use `/frontcore lobby ...` to join an active BlockFront match by mode.
- Use the client loadout editor and server profile/admin commands for setup, proxy compatibility, and debugging.

## 4) Gun Skins

- Permanent player gun skins apply to matching guns in BlockFront matches.
- If a gun is given through `gun giveMenu` with its own explicit skin, that explicit skin is kept.
- The gun skin selector always includes `default`, which clears the permanent override for that gun.
- A `Gun Skins` button is also added to the vanilla pause menu.

## 5) Sound Sliders (Client-Side)

- Sound sliders are in Minecraft sound options:
`Gun SFX Volume` and `Grenade SFX Volume`.
- These sliders are **client-only** (local player preference).
- Scope: BlockFront gun/grenade/rocket/explosion style sounds.
- Behavior:
`0%` mutes the target category.
`1-100%` scales target category volume.
- The rest of game audio is not changed.

## 6) Versioning

- Version format is:
`<frontcore>-<BF>`
- Example:
`1.0.6-0.7.1.2b`

## 7) Requirements

- Minecraft `1.21.1`
- NeoForge `21.1.x`
- BlockFront `0.7.1.2b`

## 8) Build (for local development)

- Follow setup notes in `bf/README.md`.
- Build with:
`gradlew.bat build`

## 9) Safety Rules

- Before touching packet routing, drop handling, or match gating logic, read:
`RULES.md`
