# FrontCore

`FrontCore` is a companion utility mod for BlockFront focused on offline flow control, loadout editing, and advanced admin/setup helpers.

Credit to the OG author: [`forteus19`](https://github.com/forteus19/Front-Utilities).

## In-Game Usage

## 1) Offline Flow Behavior

- Launch game normally: BlockFront menu appears first.
- Choose offline mode in BlockFront.
- `FrontCore` redirects to the vanilla Minecraft title screen.

## 2) Key Commands

- Open loadout editor:
`/loadout editor`

- Sync edited loadouts:
`/loadout sync`

- Open weapon extra editor for a gun:
`/gun extra <gun_id>`

- Open gun modifier editor:
`/gun modifier <gun_id>`

- Sync gun modifier changes:
`/gun modifier sync`

## 3) Typical Workflow

- Start BlockFront and enter offline mode.
- Use the editor commands above to adjust loadouts and weapon setup.
- Run sync commands after edits so changes are pushed.

## 4) Requirements

- Minecraft `1.21.1`
- NeoForge `21.1.x`
- BlockFront `0.7.1.2b`

## 5) Build (for local development)

- Follow setup notes in `bf/README.md`.
- Build with:
`gradlew.bat build`
