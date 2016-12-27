package engine;

//Ideal - not connected
//		- connected but not start
//WaitForOpponent - connected, start but waiting for opponent to send shipPosition 
//     - waiting for opponent to send move
//Turn - Send move
//End - win or lose


/**
 * The Enum of PlayerStatus.
 */
public enum PlayerStatus {
	
	NOT_CONNECTED,
	WAITINGOP,
	CONNECTED,
	READY,
	TURN,
	WAIT,
	GAMEOVER,	
}
