package com.tankbattle.server.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.utils.Vector2;

public class Player implements GameEntity {
    @JsonIgnore
    private GameController gameController;

    private String sessionId;
    private String username;

    // Player location is a coordinate system where every 1000 units is a new tile,
    // ensuring smooth transition between tiles
    private Vector2 location;
    private Vector2 size;
    private byte movementDirection;
    private float speed = 40;
    private double rotationAngle = 0;
    private int health = 100; // Example health attribute

    private static final byte DIRECTION_UP = 0b1000;
    private static final byte DIRECTION_LEFT = 0b0100;
    private static final byte DIRECTION_DOWN = 0b0010;
    private static final byte DIRECTION_RIGHT = 0b0001;

    @JsonIgnore
    private Vector2 previousLocation; // To store previous position for collision response

    public Player() {
        location = new Vector2(0, 0);
        previousLocation = new Vector2(0, 0);
        movementDirection = 0;
        size = new Vector2(800, 800);

        // Assuming SpringContext is a utility to get Spring beans
        this.gameController = SpringContext.getBean(GameController.class);
    }

    public Player(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        location = new Vector2(0, 0);
        previousLocation = new Vector2(0, 0);
        size = new Vector2(800, 800);
        movementDirection = 0;
        this.gameController = SpringContext.getBean(GameController.class);
    }

    public Player(String sessionId, String username, int x, int y) {
        this.sessionId = sessionId;
        this.username = username;
        location = new Vector2(x, y);
        previousLocation = new Vector2(x, y);
        size = new Vector2(800, 800);
        movementDirection = 0;
        this.gameController = SpringContext.getBean(GameController.class);
    }

    @JsonIgnore
    public byte getMovementDirection() {
        return movementDirection;
    }

    @JsonIgnore
    public void setMovementDirection(byte movementDirection) {
        this.movementDirection = movementDirection;
    }

    @JsonIgnore
    public String getSessionId() {
        return this.sessionId;
    }

    @JsonIgnore
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Vector2 getLocation() {
        return this.location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public Vector2 getSize() {
        return this.size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public int getHealth() {
        return health;
    }

    private void updateRotationAngle() {
        switch (movementDirection) {
            case DIRECTION_UP:
                rotationAngle = 0;
                break;
            case DIRECTION_LEFT:
                rotationAngle = 270;
                break;
            case DIRECTION_DOWN:
                rotationAngle = 180;
                break;
            case DIRECTION_RIGHT:
                rotationAngle = 90;
                break;
            case DIRECTION_UP | DIRECTION_RIGHT:
                rotationAngle = 45;
                break;
            case DIRECTION_UP | DIRECTION_LEFT:
                rotationAngle = 315;
                break;
            case DIRECTION_DOWN | DIRECTION_LEFT:
                rotationAngle = 225;
                break;
            case DIRECTION_DOWN | DIRECTION_RIGHT:
                rotationAngle = 135;
                break;
            default:
                break;
        }
    }

    public void updateLocation() {
       // Calculate intended movement
       float diagonalSpeed = speed / (float) Math.sqrt(2);
       float deltaY = 0;
       float deltaX = 0;

       boolean movingVertically = false;
       boolean movingHorizontally = false;

       if ((movementDirection & DIRECTION_UP) != 0) {
           movingVertically = true;
           deltaY -= (movementDirection & (DIRECTION_LEFT | DIRECTION_RIGHT)) != 0 ? diagonalSpeed : speed;
       }

       if ((movementDirection & DIRECTION_DOWN) != 0) {
           movingVertically = true;
           deltaY += (movementDirection & (DIRECTION_LEFT | DIRECTION_RIGHT)) != 0 ? diagonalSpeed : speed;
       }

       if ((movementDirection & DIRECTION_LEFT) != 0) {
           movingHorizontally = true;
           deltaX -= (movementDirection & (DIRECTION_UP | DIRECTION_DOWN)) != 0 ? diagonalSpeed : speed;
       }

       if ((movementDirection & DIRECTION_RIGHT) != 0) {
           movingHorizontally = true;
           deltaX += (movementDirection & (DIRECTION_UP | DIRECTION_DOWN)) != 0 ? diagonalSpeed : speed;
       }

       // Calculate intended new position
       float newX = location.getX() + deltaX;
       float newY = location.getY() + deltaY;
       Vector2 newPosition = new Vector2(newX, newY);

       // Check for world border constraints
       if (checkWorldBorderConstraints(newX, newY)) {
           // Prevent movement beyond world boundaries
           return;
       }

       // Check for map collisions before moving
       //boolean canMove = gameController.getCollisionManager().canMoveTo(this, newPosition, gameController.getLevel().getGrid());

       previousLocation = new Vector2(location.getX(), location.getY());
       location.setX(newX);
       location.setY(newY);
    //    if (canMove) {
    //        // Update previous location before moving
    //        previousLocation = new Vector2(location.getX(), location.getY());
    //        location.setX(newX);
    //        location.setY(newY);
    //    } else {
    //        // Movement is blocked; revert to previous position
    //        revertToPreviousPosition();
    //    }

       updateRotationAngle();
   }

    /**
     * Checks whether the intended new position is within the world boundaries.
     *
     * @param newX The intended new x-coordinate.
     * @param newY The intended new y-coordinate.
     * @return True if the new position is outside the world boundaries, else false.
     */
    private boolean checkWorldBorderConstraints(float newX, float newY) {
        float leftCornerX = newX - size.getX() / 2;
        float rightCornerX = newX + size.getX() / 2;
        float topCornerY = newY - size.getY() / 2;
        float bottomCornerY = newY + size.getY() / 2;

        return leftCornerX < 0 ||
               rightCornerX > gameController.getLevelCoordinateWidth() ||
               topCornerY < 0 ||
               bottomCornerY > gameController.getLevelCoordinateHeight();
    }

    public void revertToPreviousPosition() {
        this.location = new Vector2(previousLocation.getX(), previousLocation.getY());
    }

    public void applyDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            // Handle player death (e.g., remove player, respawn, etc.)
            System.out.println("Player " + username + " has been eliminated!");
            // Implement additional logic as needed
        }
    }

    public void applyPowerUp(PowerUpType type) {
        switch (type) {
            case HEALTH_BOOST:
                this.health += 20; // Example boost
                if (this.health > 100) this.health = 100; // Max health cap
                break;
            case SPEED_BOOST:
                this.speed += 10; // Example boost
                // Implement duration logic if needed
                break;
            case DAMAGE_BOOST:
                // Implement damage boost logic
                break;
            case SHIELD:
                // Implement shield logic
                break;
            // Add other power-up types as needed
        }
    }

    public String toString() {
        return String.format("{ sessionId: '%s', username: '%s', location: { x: %d, y: %d }, health: %d }", this.sessionId,
                this.username, this.location.getX(), this.location.getY(), this.health);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;
        return username.equals(player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * Moves the player by the specified delta vector.
     *
     * @param delta The vector by which to move the player.
     */
    public void moveBy(Vector2 delta) {
        // Store the current location as previous before moving
        previousLocation = new Vector2(location.getX(), location.getY());
        this.location.setX(this.location.getX() + delta.getX());
        this.location.setY(this.location.getY() + delta.getY());
    }

    public GameController getGameController() {
        return this.gameController;
    }

    //region Collision_stuff
    private int queryId;

    @Override
    public int getQueryId() {
        return queryId;
    }

    @Override
    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }
    //endregion
}
