# Tank battle game

The game is based on the 'Battle City' game, which was released in 1985. The goal of the game is to score the most points by destroying enemy tanks.

## Requirements

- The game takes place over multiple rounds, each with its own map.
- A round lasts 2 minutes.
- The map contains destructible and indestructible elements.
- Destroying an enemy tank gives the player 100 points.
- Destructible entities have hit points.
- Players can collect temporary power-ups that grant their tank additional abilities:
  - Increased speed;
  - Faster firing rate;
  - Double shot;
  - Extra health.
- Players learn who the winner is at the end of the round.
- Entity types:
  - Water (indestructible; tanks cannot move through it; shots pass through it);
  - Wall (destructible; tanks cannot move through it; shots deal damage);
  - Power-up (temporary; tanks can move through it; tanks can collect it);
- Power-up abilities:
  - Speed increase;
  - Firing rate;
  - Increased damage;
  - Shot types:
    - Single;
    - Double;
- Each player starts with 20 hit points.
- If a game is already in progress when a player joins, they must wait until the game is over.
- The game supports up to 4 players.
- If a fifth player attempts to join, they will receive a message that the game is full and they cannot join.
- The color of the tank is set automatically.
