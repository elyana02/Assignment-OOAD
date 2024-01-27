import java.util.Objects;

public class Player
{
    private int playerNumber;   //player 1 or player 2
    
    public Player(int playerNumber)
    {
        this.playerNumber = playerNumber;
    }
    
    public int getPlayerNumber()
    {
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