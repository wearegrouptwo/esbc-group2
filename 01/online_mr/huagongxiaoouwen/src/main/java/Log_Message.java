import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Calendar;

public class Log_Message implements Writable {


    String time;
    int log_status;
    String uid;

    //在反序列化时，反射机制需要调用无参构造方法，所以显式定义了一个无参构造方法


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLog_status() {
        return log_status;
    }

    public void setLog_status(int log_status) {
        this.log_status = log_status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public Log_Message() {

    }

    //为了对象数据的初始化方便，加入一个带参的构造函数
    public Log_Message(String time, int log_status, String uid) {
        super();

        this.time = time;
        this.log_status = log_status;
        this.uid = uid;
    }



    //重写序列化方法
    @Override
    public void write(DataOutput out) throws IOException, IOException {

        out.writeUTF(uid);
        out.writeUTF(time);
        out.writeInt(log_status);



    }

    //重写反序列化方法
    @Override
    public void readFields(DataInput in) throws IOException {

        uid = in.readUTF();
        time = in.readUTF();
        log_status = in.readInt();

    }

    //重写toString方法
    @Override
    public String toString() {
        return this.time+" "+this.log_status+" "+this.uid;
    }


}
