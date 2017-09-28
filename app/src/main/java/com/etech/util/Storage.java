package com.etech.util;

import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//@SuppressLint({ "SimpleDateFormat", "SdCardPath" })
public class Storage {

	public static void verifyDirPath(String dir) {

		try {
			File f_dir = new File(dir);
			if (!f_dir.exists()) {
				f_dir.mkdirs();
			}
			f_dir = null;
		} catch (Exception e) {
			Log.v("verifyDirPath", "error : ");
		e.printStackTrace();
			EtechLog.print(Storage.class + " : verifyDirPath() : ", e);
			EtechLog.error(Storage.class + " : verifyDirPath() : ", e);
		}

	}

	public static void createNoMediaInDir(String dir) {

		try {
			File f_dir = new File(dir);

			if (!f_dir.exists()) {
				f_dir.mkdirs();
			}

			File file = new File(dir, Constant.NOMEDIA_FILE);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					EtechLog.print(Storage.class + " : createNoMediaDir() : ",
							e);
					EtechLog.error(Storage.class + " : createNoMediaDir() : ",
							e);
				}
			}

			f_dir = null;
		} catch (Exception e) {
			EtechLog.print(Storage.class + " : createNoMediaDir() : ", e);
			EtechLog.error(Storage.class + " : createNoMediaDir() : ", e);
		}

	}

	public static File verifyLogFile() throws IOException {
		File logFile = new File(Constant.DIR_LOG + "/Rizzl_Log_"
				+ new SimpleDateFormat("yyyy_MM_dd").format(new Date())
				+ ".html");
		FileOutputStream fos = null;

		Storage.verifyDirPath(Constant.DIR_LOG);

		if (!logFile.exists()) {
			logFile.createNewFile();

			fos = new FileOutputStream(logFile);

			String str = "<TABLE style=\"width:100%;border=1px\" cellpadding=2 cellspacing=2 border=1><TR>"
					+ "<TD style=\"width:30%\"><B>Date n Time</B></TD>"
					+ "<TD style=\"width:20%\"><B>Title</B></TD>"
					+ "<TD style=\"width:50%\"><B>Description</B></TD></TR>";

			fos.write(str.getBytes());
		}

		if (fos != null) {
			fos.close();
		}

		fos = null;

		return logFile;
	}
	
	public static File createEventLogFile(String eventname) throws IOException {

		Date date=new Date();
	    //System.out.println(date.getTime());
		
		File eventlogFile = new File(Constant.DIR_LOG + "/Rizzl_Log_"+ String.valueOf(date.getTime())+"_"+eventname);
		FileOutputStream fos = null;
		
		Log.d("StorageFile", "File Name : "+eventlogFile.getName());

		Storage.verifyDirPath(Constant.DIR_LOG);

		if (!eventlogFile.exists()) {
			eventlogFile.createNewFile();

			fos = new FileOutputStream(eventlogFile);

			String str = "<TABLE style=\"width:100%;border=1px\" cellpadding=2 cellspacing=2 border=1><TR>"
					+ "<TD style=\"width:30%\"><B>Date n Time</B></TD>"
					+ "<TD style=\"width:20%\"><B>Title</B></TD>"
					+ "<TD style=\"width:50%\"><B>Description</B></TD></TR>";

			fos.write(str.getBytes());
		}

		if (fos != null) {
			fos.close();
		}

		fos = null;

		return eventlogFile;
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1) {
					break;
				}
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static void createLogZip() {
		ZipOutputStream zout = null;
		FileInputStream fis = null;
		String files[] = null;
		int ch;
		try {
			files = new File(Constant.DIR_LOG).list();

			zout = new ZipOutputStream(new FileOutputStream(Constant.LOG_ZIP));

			zout.setLevel(Deflater.DEFAULT_COMPRESSION);

			for (int ele = 0; ele < files.length; ele++) {
				fis = new FileInputStream(Constant.DIR_LOG + "/" + files[ele]);

				/*
				 * To begin writing ZipEntry in the zip file, use
				 * 
				 * void putNextEntry(ZipEntry entry) method of ZipOutputStream
				 * class.
				 * 
				 * This method begins writing a new Zip entry to the zip file
				 * and positions the stream to the start of the entry data.
				 */

				zout.putNextEntry(new ZipEntry(Constant.DIR_LOG + "/"
						+ files[ele]));

				/*
				 * After creating entry in the zip file, actually write the
				 * file.
				 */

				while ((ch = fis.read()) > 0) {
					zout.write(ch);
				}

				/*
				 * After writing the file to ZipOutputStream, use
				 * 
				 * void closeEntry() method of ZipOutputStream class to close
				 * the current entry and position the stream to write the next
				 * entry.
				 */

				zout.closeEntry();

				// close the InputStream
				fis.close();
			}

			// close the ZipOutputStream
			zout.close();

		} catch (Exception e) {
			EtechLog.error(Storage.class + " :: create log zip :: ", e);
		}

		zout = null;
		fis = null;
		files = null;
	}

	public static void clearLog() {
		String files[] = null;
		File file = null;
		try {
			files = new File(Constant.DIR_LOG).list();

			for (int ele = 0; ele < files.length; ele++) {
				file = new File(Constant.DIR_LOG, files[ele]);

				file.delete();
			}

		} catch (Exception e) {
			EtechLog.error(Storage.class + " :: clearLog :: ", e);
		}

		files = null;
		files = null;
	}

	public static void delete(String f) throws IOException {
		File file = new File(f);

		if (file.exists()) {
			file.delete();
		}

		file = null;
		f = null;
	}

	public static boolean copyFile(String src, String dest) {
		EtechLog.print("=========FROM :: " + src);
		EtechLog.print("=========TO :: " + dest);
		boolean success = true;
		FileInputStream in = null;
		FileOutputStream out = null;

		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(dest);

			byte[] buf = new byte[1024];
			int len;

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			return success;
		} catch (IOException e) {
			success = false;
			e.printStackTrace();
			EtechLog.error("Storage ::copy:: ", e);
		} finally {
			success = false;
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}
		in = null;
		out = null;
		return success;

	}

	public static void writeFile(InputStream in, String dest) {

		FileOutputStream out = null;

		try {
			out = new FileOutputStream(dest);

			byte[] buf = new byte[1024];
			int len;

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}
		in = null;
		out = null;
	}

}