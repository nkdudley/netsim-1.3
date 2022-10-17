package networkLayer;

import configurator.Configurable;
import linkLayer.Link;
import physicalLayer.PipeBackedPort;

import java.io.PipedInputStream;
import java.util.ArrayList;

public class NewNetwork extends Network implements Configurable {


    private byte address;
    private DestinationTable table;

    public byte getAddress(){return address;}

    @Override
    public void bringDown() {

    }

    @Override
    public void interrupt() {

    }

    @Override
    public void receiveFromTransport(byte[] p) {
        //get the port we would like to send to with the
        byte to = p[0];
        table.populateTable();
        links[table.getBestRoute(to)].receiveFromNetwork(p);
    }

    @Override
    public void receiveFromLink(byte[] f) {
        //if this is the final destination then call receive from network
        System.out.println(address + " was passed the packet");
        if(f[0] == address) {
            getTransportLayer().receiveFromNetwork(f);
        } else{
            links[table.getBestRoute(f[0])].receiveFromNetwork(f);
        }
        //otherwise find the address in my table and send it along
    }

    @Override
    public byte[] toRawBytes(NetworkPacket p) {
        byte[] output = new byte[p.data().length + ((Meta) p.meta()).getData().length];
        Meta meta = (Meta) p.meta();
        System.arraycopy(meta.getData(), 0, output, 0, meta.getData().length);
        System.arraycopy(p.data(), 0, output, meta.getData().length, p.data().length);
        return output;
    }

    @Override
    public NetworkPacket fromRawBytes(byte[] bits) {
        byte[] metadata = new byte[4];
        System.arraycopy(bits,0,metadata,0,4);
        Meta meta = new Meta(metadata);
        byte[] data = new byte[4];
        System.arraycopy(bits,4,data,0,4);
        NetworkPacket packet = new NetworkPacket(meta,data);
        return packet;
    }

    @Override
    public void configureWith(String s) {
        //take s and cast it to an integer and the int to a byte so that
        //the byte value is the value of the int not its asci values
        int num = Integer.parseInt(s);
        byte addrs = (byte) num;

        //assign the address to the byte
        address = addrs;
        table = new DestinationTable(this);
    }

    public DestinationTable getDestinations(){return table;}
}
