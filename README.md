<p align="center">
    <picture>
        <img src="assets/piku-logomark.svg" alt="Piku Logomark" height="256">
    </picture>
</p>

<p align="center">
    <img src="assets/java25.svg" alt="" width="144">
    <img src="assets/kotlin.svg" alt="" width="144">
    <img src="assets/fabric-language-kotlin.svg" alt="" width="265">
    <a href="https://discord.gg/DeTbNe6UyY">
        <img src="assets/discord.svg" alt="" width="192">
    </a>
</p>

**[📖 Docs](https://docs.obscure.computer/project.piku/)** · **[💻 GitHub](https://github.com/obscurecomputer/piku)**

**Piku** is a Fabric mod that brings **Luau scripting** to servers!
You can build custom UI, cameras systems, and run client-side logic without Java, shader work or extra server load!

## Usage

Piku is a **client-side mod**.
Players are **not required to use it** on Piku servers **by default**, and is typically used to expand server functionality.

## Platforms

| Platform 	 | Client 	               | Server | Server (drop-in jar) |
|------------|------------------------|--------|----------------------|
| Fabric   	 | ✅      	               | ❌      | ❌                    |
| Minestom 	 | ❌      	               | ✅      | ❌  (Not Possible)    |
| Paper    	 | ❌ (Not Possible)     	 | ✅      | ✅                    |

## Current Features
- **Input Handling**
    - listening to player's inputs (keyboard, mouse)
- **UI System**
    - build complex UIs using the 8+ UI components
- **Camera System**
    - manipulate the player's camera using a **full 3-axis movement** (roll included), with easings
- **Cinematic Camera System**
    - manipulate a decoupled camera using 3 axis movement with various movement and animation controls
- **Scheduler**
    - time-based events and delays
- **Shared States**
    - sync data between the server and the client
- **Client Control**
    - tinker with the client using various controls
        - hotbar visibility
        - perspective
        - inventory items
        - nbt
        - play sounds
        - view bobbing
- **Debugging**
    - built-in script independent logging
- **Raycasting**
    - client-side raycasting with powerful entity filters and customisation options
- **NBT & Components**
    - read & write data components of items and entities
- **Controlify Support**
    - Allow movement through UIs using controllers!

# To-do List / Roadmap
Tasks labeled here may not be worked on in chronological order.

- [x] Input Handling
- [x] Finish UI system (now finished for the most part)
- [x] Error handling & debugging
  - [x] `log.info`, `warn`, `error`
- [x] Time system/scheduler
- [x] Camera System
  - [x] Movement/rotation + easing
  - [x] All 3 axes of rotation
- [x] World Interaction
  - [x] Raycasts
  - [x] Querying blocks/entities on the client
- [ ] Client Control
  - [ ] Lock/manipulate/animate FOV
  - [x] Disable/Enable bobbing
  - [x] Changeable bobbing strength
  - [x] Querying keybinds on the client
- [ ] Player control
  - [ ] Disabling view of client or other players' limbs/armor
- [x] Shared states
- [ ] Particle System (GPU based)
- [ ] Rewrite docs
- [ ] Vertical UI Gradients
- [ ] UI transitioning