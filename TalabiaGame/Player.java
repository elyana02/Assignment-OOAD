/**
 * The Player class represents a player in the Talabia Chess Game.
 * Each player is identified by a unique player number (1 or 2).
 *
 * Key Features:
 * - Stores the player number to distinguish between Player 1 and Player 2.
 * - Provides methods for retrieving the player number, implementing equality, and generating hash codes.
 * - Enables comparison between player objects based on their player numbers.
 *
 * Relationships:
 * - Used in various parts of the Talabia Chess Game to identify the current player.
 * - Supports equality checks for player instances.
 *
 * Usage Guidelines:
 * - Instantiate this class to create player objects for use in the chess game.
 * - Retrieve the player number using the getPlayerNumber() method.
 * - Use the equals() method for comparing player instances based on their player numbers.
 * - Integrate this class to manage player-related logic in the game.
 */

import java.util.Objects;

public class Player {
    private int playerNumber;   //Player 1 or player 2
    
    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
    }
    
    public int getPlayerNumber() {
        return playerNumber;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Player player = (Player) obj;
        return playerNumber == player.playerNumber;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(playerNumber);
    }
}
