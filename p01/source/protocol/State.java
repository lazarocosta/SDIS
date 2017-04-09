package protocol;

import systems.Peer;

/**
 * Created by jazz on 09-04-2017.
 */
public class State {

    public static void show(){

        // TODO: BACKED UP FILES

        System.out.println("Disk capacity: " + Peer.getDb().getDisk().getStorageSpace() + " kB; occupied: " + Peer.getDb().getDisk().getUsedBytes() + " kB.");
    }
}
