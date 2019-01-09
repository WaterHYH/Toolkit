package com.dodoo_tech.gfal.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MOU on 2016/9/18.
 */
public class LogcatUtil {

    private final static long LOG_FILE_MAX_SIZE=20*1024*1024;
    private final static int LOG_FILE_MAX_COUNT=2;

    public static synchronized void dumpSysLog(Context context, String packName, String logText, String tag) {

        try
        {
            String path = getSysLogPath(context,packName);

            byte[] data=null;
            try {

                RandomAccessFile randomFile = new RandomAccessFile(path, "rw");
                // 文件长度，字节数
                long fileLength =randomFile.length();
                //将写文件指针移到文件尾。
                randomFile.seek(fileLength);

                if (logText.length() == 0) {
                    return;
                }
                if (randomFile != null ) {
                    logText=getStringFormatDate(new Date(),"yyyy-MM-dd HH:mm:ss.SSS")+" "+tag+": "+logText + "\r\n";
                    data=logText.getBytes();
                    fileLength = randomFile.length();
                    if(fileLength+data.length>=LOG_FILE_MAX_SIZE)
                    {
                        path=getNextSysLogPath(path);
                        randomFile = new RandomAccessFile(path, "rw");
                        // 文件长度，字节数
                        fileLength = randomFile.length();
                        //将写文件指针移到文件尾。
                        randomFile.seek(fileLength);
                    }

                    randomFile.write(data);

                    data=null;
                }

                randomFile.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getStringFormatDate(Date date, String format) {

        try {
            SimpleDateFormat sFormat = new SimpleDateFormat(format);
            return sFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date getDateFormatString(String time, String format) {

        try {
            SimpleDateFormat sFormat = new SimpleDateFormat(format);
            return sFormat.parse(time);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readLastLine(File file, String charset)  {
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            long len = raf.length();
            if (len == 0L) {
                return "";
            } else {
                long pos = len - 1;
                while (pos > 0) {
                    pos--;
                    raf.seek(pos);
                    if (raf.readByte() == '\n') {
                        break;
                    }
                }
                if (pos == 0) {
                    raf.seek(0);
                }
                byte[] bytes = new byte[(int) (len - pos)];
                raf.read(bytes);
                if (charset == null) {
                    return new String(bytes);
                } else {
                    return new String(bytes, charset);
                }
            }
        } catch (Exception e) {

        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception e2) {
                }
            }
        }
        return null;
    }

    /**
     * 每次记录日志之前先清除日志的缓存, 不然会在两个日志文件中记录重复的日志
     */
    private static void clearLogCache() {
        Process proc = null;
        List<String> commandList = new ArrayList<String>();
        commandList.add("logcat");
        commandList.add("-c");
        try {
            proc = Runtime.getRuntime().exec(commandList.toArray(new String[commandList.size()]));
            if (proc.waitFor() != 0) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                proc.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getSysLogZipPath(Context context, String appName)
    {
        String path=null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()+ File.separator + "syslogs"+ File.separator+ "syslogs_"+appName+".zip";
        } else {// 如果SD卡不存在，就保存到本应用的目录下
            path = context.getFilesDir().getAbsolutePath()
                    + File.separator + "syslogs"+ File.separator + "syslogs_"+appName+".zip";
        }


        return path;
    }

    private static String getSysLogDir(Context context, String appName)
    {
        String path=null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "syslogs"+ File.separator+appName+File.separator;
        } else {// 如果SD卡不存在，就保存到本应用的目录下
            path = context.getFilesDir().getAbsolutePath()
                    + File.separator + "syslogs"+ File.separator+appName+File.separator;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    static String getSysLogPath(Context context, String packName)
    {
        String path = getSysLogDir(context,packName);

        File dir=new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        int i=1;
        File[] list=dir.listFiles();
        for(File f:list)
        {
            if(f.isFile()&&f.getName().startsWith("debug"))
            {
                int val=Integer.valueOf(f.getName().replaceAll("debug","").replaceAll(".log",""));
                if(val>=i)
                {
                    i=val;
                }
            }
            else
            {
                FileUtil.deleteFolder(f.getAbsolutePath());//非日志删除
            }
        }

        list=dir.listFiles();
        if(list.length!=i)
        {
            //日志文件和数目不对应
            i=1;
            for(File f:list)
            {
                FileUtil.DeleteFolder(f.getAbsolutePath());//删除
            }
        }
        path =path+ ("debug"+i+ ".log");
        return path;
    }

    static String getNextSysLogPath(String filePath)
    {

        String path = new File(filePath).getParent();
        int val=Integer.valueOf(filePath.replaceAll(path,"").replaceAll( File.separator,"").replaceAll("debug","").replaceAll(".log",""));
        if(val==LOG_FILE_MAX_COUNT)
        {
            val=LOG_FILE_MAX_COUNT;
            File fristf=new File(path+File.separator+"debug1.log");
            FileUtil.deleteFile(fristf.getAbsolutePath());

            File[] list=new File(path).listFiles();
            for(File f:list)
            {
                if(f.isFile()&&f.getName().startsWith("debug"))
                {
                    int i=Integer.valueOf(f.getName().replaceAll("debug","").replaceAll(".log",""));
                    File newFile=new File(path+File.separator+("debug"+(i-1)+ ".log"));
                    f.renameTo(newFile);
                }
            }
        }
        else
        {
            val++;
        }

        return path+File.separator+("debug"+val+ ".log");
    }


}

