package networkLayer;

import linkLayer.Link;
import physicalLayer.PipeBackedPort;

import java.util.ArrayList;

public class DestinationTable {
    NewNetwork owner;
    ArrayList<byte[]> destinations;

    public DestinationTable(NewNetwork network){
        owner = network;
        destinations = new ArrayList<byte[]>();

    }

    public void populateTable(){
        byte[] me = new byte[3];
        me[0] = owner.getAddress();
        me[1] = -1;
        me[2] = 0;
        destinations.add(me);

        //for each link this table's node has
        for(int i = 0; i < owner.links.length; i++){
            //get the destination table of the other node
            DestinationTable otherTable = ((NewNetwork) ((PipeBackedPort) owner.links[i].getPhysicalLayer()).getOtherPort().getLinkLayer().getNetworkLayer()).getDestinations();
            //for each destination in the other node's table
            for(byte[] dest: otherTable.destinations){
                //if the route does not exist
                if(!routeExists(dest)){
                    //update hop to be one more because i am the new node that we traverse to get to the destination
                    dest[2] += 1;
                    // update the link to send to the port on my device, not the link number from the other device
                    dest[1] = (byte) i;
                    //add it to our table
                    destinations.add(dest);
                }
            }
        }
    }

    private boolean routeExists(byte[] dest){
        for(byte[] d: destinations){
            if(d[0] == dest[0]){
                return true;
            }
        }
        return false;
    }

    public byte getRoute(byte dest){
        //look through our table to find the destination
        for(byte[] bytes: destinations){
            //if the byte array in our table is regards the same address as dest
            if(bytes[0] == dest){
                //give the port number to this address
                return bytes[1];
            }
        }

        return -1;
    }
}
