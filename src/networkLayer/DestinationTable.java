package networkLayer;

import linkLayer.Link;
import physicalLayer.PipeBackedPort;

import java.util.ArrayList;

public class DestinationTable {
    NewNetwork owner;
    ArrayList<byte[]> destinations;
    boolean populating;

    public DestinationTable(NewNetwork network){
        owner = network;
        destinations = new ArrayList<byte[]>();
        byte[] me = new byte[3];
        me[0] = owner.getAddress();
        me[1] = -1;
        me[2] = 0;
        destinations.add(me);
        populating = false;
    }

    /**
     * updates the table with information of our neighbor's tables
     */
    public void populateTable(){
        populating = true;
        destinations = new ArrayList<byte[]>();
        byte[] me = new byte[3];
        me[0] = owner.getAddress();
        me[1] = -1;
        me[2] = 0;
        destinations.add(me);

        //for each link this table's node has
        for(int i = 0; i < owner.links.length; i++){
            //get the destination table of the other node
            DestinationTable otherTable = ((NewNetwork) ((PipeBackedPort) owner.links[i].getPhysicalLayer()).getOtherPort().getLinkLayer().getNetworkLayer()).getDestinations();
            if(!otherTable.populating){
                otherTable.populateTable();
            }
            //for each destination in the other node's table
            for(byte[] dest: otherTable.destinations){
                if(dest[0] != me[0]) {
                    //if the route does not exist
                    if(!routeExists(dest)){
                        byte[] newDest = new byte[3];
                        System.arraycopy(dest, 0, newDest, 0, 3);
                        //update hop to be one more because i am the new node that we traverse to get to the destination
                        newDest[2] += 1;
                        // update the link to send to the port on my device, not the link number from the other device
                        newDest[1] = (byte) i;
                        //add it to our table
                        destinations.add(newDest);
                    } else {
                        //route does already exist
                        ArrayList<byte[]> routes = getRoutes(dest[0]);
                        boolean samePort = false;
                        // does it exist on this port?
                        for(byte[] bytes: routes){
                            if(bytes[1] == (byte) i){
                                samePort = true;
                                bytes[2] = (byte) Math.min(bytes[2], dest[2] + 1);
                                replaceWith(bytes);
                            }
                        }
                        if(!samePort){
                            byte[] newDest = new byte[3];
                            System.arraycopy(dest, 0, newDest, 0, 3);
                            //update hop to be one more because i am the new node that we traverse to get to the destination
                            newDest[2] += 1;
                            // update the link to send to the port on my device, not the link number from the other device
                            newDest[1] = (byte) i;
                            //add it to our table
                            destinations.add(newDest);
                        }
                    }
                }
            }
        }
        populating = false;
    }

    /**
     *
     * @param newRoute - the updated route for a given port
     */
    public void replaceWith(byte[] newRoute) {
        byte[] oldRoute = new byte[3];
        for(byte[] d: destinations) {
            if(d[0] == newRoute[0] && d[1] == newRoute[1]) {
                oldRoute = d;
            }
        }
        destinations.remove(oldRoute);
        destinations.add(newRoute);
    }

    /**
     * @param dest - the destination of interest
     * @return whether or not there is a known route
     */
    private boolean routeExists(byte[] dest){
        for(byte[] d: destinations){
            if(d[0] == dest[0]){
                return true;
            }
        }
        return false;
    }

    /**
     * gets all the known routes to a given address
     * through a unique port on this machine
     * @param dest - the destination of interest
     * @return
     */
    public ArrayList<byte[]> getRoutes(byte dest){
        ArrayList<byte[]> output = new ArrayList<byte[]>();
        for(byte[] bytes: destinations){
            if(bytes[0] == dest){
                output.add(bytes);
            }
        }
        return output;
    }

    /**
     * finds the best route of all routes on our table to send through
     * @param dest - destination address of interest
     * @return - the port number with the fastest route
     */
    public byte getBestRoute(byte dest){
        ArrayList<byte[]> routes = getRoutes(dest);
        int outputNum = 0;
        byte currentHops = routes.get(0)[2];
        for(int i = 0; i < routes.size(); i++){
            // if there number of hops is less than our current number of hops then update it all
            if(currentHops > routes.get(i)[2]){
                outputNum = i;
                currentHops = routes.get(i)[2];
            }
        }
        return routes.get(outputNum)[1];
    }
}
