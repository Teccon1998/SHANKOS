public class KernelMessage {
    private int senderPID;
    private int targetPID;
    private byte[] data;
    private int messageType;


    public KernelMessage(int senderPID, int targetPID, byte[] data, int messageType) {
        this.senderPID = senderPID;
        this.targetPID = targetPID;
        this.data = data;
        this.messageType = messageType;
    }

    public KernelMessage(KernelMessage km)
    {
        this.senderPID = km.getSenderPID();
        this.targetPID = km.getTargetPID();
        this.data = km.getData();
        this.messageType = km.getMessageType();
    }


    public int getSenderPID() {
        return senderPID;
    }

    public int getTargetPID() {
        return targetPID;
    }

    public byte[] getData() {
        return data;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setSenderPID(int senderPID) {
        this.senderPID = senderPID;
    }

    public void setTargetPID(int targetPID) {
        this.targetPID = targetPID;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String toString() {
        return "Sender PID: " + senderPID + "\nTarget PID: " + targetPID + "\nData: " + new String(data) + "\nMessage Type: " + messageType;
    }
}
