package networkLayer;

public class Meta {
    private byte addressTo;
    private byte addressFrom;
    private byte packetNum;
    private byte totalNum;

    public Meta(byte[] bits) {
        addressTo = bits[0];
        addressFrom = bits[1];
        packetNum = bits[2];
        totalNum = bits[3];
    }

    public int getAddressTo(){return addressTo;}
    public int getAddressFrom(){return addressFrom;}
    public int getTotalNum(){return totalNum;}
    public int getPacketNum(){return packetNum;}

    public void setAddressTo(byte to){addressTo = to;}
    public void setAddressFrom(byte from){addressFrom = from;}
    public void setPacketNum(byte num){packetNum = num;}
    public void setTotalNum(byte num){totalNum = num;}

    public byte[] getData() {
        byte[] data = new byte[4];
        data[0] = addressTo;
        data[1] = addressFrom;
        data[2] = packetNum;
        data[3] = totalNum;
        return data;
    }

}
