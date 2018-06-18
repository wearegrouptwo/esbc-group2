import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Handa on 2018/6/17.
 */
public class DataBean extends LongWritable implements Writable {
    private  String userID;
    private String date;
    private boolean status;

    public  DataBean(){
        super();
    }
    public DataBean(String userID,String date,boolean status){
        super();
        this.userID=userID;
        this.date=date;
        this.status=status;
    }

    public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
            "d/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
    //public static final SimpleDateFormat dateformat1 = new SimpleDateFormat(
//            "yyyyMMddHHmmss");
    /**
     * 解析英文时间字符串
     *
     * @param string
     * @return
     * @throws ParseException
     */
    public Date parseDateFormat(String string) {
        Date parse = null;
        try {
            parse = FORMAT.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public String getUserID(){
        return userID;
    }
    public void setUserID(String userID){
        this.userID=userID;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date=date;
    }
    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status){
        this.status=status;
    }
    // public long length(){ return 1;}
    //重写序列化方法
    public void write(DataOutput out)throws IOException{
        out.writeUTF(userID);
        out.writeUTF(date);
        out.writeBoolean(status);
    }
    //重写反序列化方法
    //@Override
    public void readFields(DataInput in)throws IOException{
        userID=in.readUTF();
        date=in.readUTF();
        status=in.readBoolean();
    }
    //重写tostring()方法
    @Override
    public String toString() {
        return ""+ userID+"\t"+date+"\t"+status;
    }
}
