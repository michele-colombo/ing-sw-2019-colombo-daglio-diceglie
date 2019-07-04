package it.polimi.ingsw.communication;

/**
 * Contains values for ping pong between client and server
 */
public class CommonProperties {
    /**
     * delay between a pong and the other and a ping and the other
     */
    public static final long PING_PONG_DELAY=1000;

    /**
     * text sent via socket when sending a ping
     */
    public static final String PING_NAME= "ping";
    /**
     * text sent via socket when sending a pong
     */
    public static final String PONG_NAME= "pong";


}
