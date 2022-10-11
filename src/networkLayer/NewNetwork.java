package networkLayer;

import linkLayer.Link;

public class NewNetwork extends Network{

    @Override
    public void bringDown() {

    }

    @Override
    public void interrupt() {

    }

    @Override
    public void receiveFromTransport(byte[] p) {
        //lets assume p is passed in as a packet already
        //the first byte will be the addressTo
        byte to = p[0];
        for(Link l: links){
            //if this link is addressed to the same one as the metadata expects it to be
            //then send the message on this link
        }
    }

    @Override
    public void recieveFromLink(byte[] f) {

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
        return null;
    }
}
