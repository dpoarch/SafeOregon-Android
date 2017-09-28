package com.etech.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Date;

public class EtechLog {

	/* Logging and Console */
	public static boolean DO_LOGGING = true;
	public static boolean DO_SOP = true;
	
	public static boolean EVENT_LOG = true;
	
	public static String LOG_EVENT_SYNC_CONTACT = "CONTACT_SYNC";
	
	
	private static File logEventFile = null;

	public static void debug(String title, String mesg) {
		File logFile = null;
		RandomAccessFile raf = null;

		try {

			if (EtechLog.DO_LOGGING) {
				logFile = Storage.verifyLogFile();

				raf = new RandomAccessFile(logFile, "rw");

				// seek to end of file
				raf.seek(logFile.length());

				raf.writeUTF("<TR>" + "<TD>" + new Date() + "</TD>" + "<TD>"
						+ title + "</TD>" + "<TD>" + mesg + "</TD></TR>");
			}

		} catch (Exception exception) {
			System.out.println("Log :: Debug :: " + exception.getMessage());
			exception.printStackTrace();

		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (Exception e) {
				}
			}
		}

		raf = null;
		logFile = null;
	}

	public static void startEventLogger(String event) {

		try {
			if (EtechLog.EVENT_LOG) {

				if(logEventFile == null) {
					logEventFile = Storage.createEventLogFile(event);
				}
			}
		} 
		catch (Exception exception) {
		
		} 
	}
	
	public static void logEvent(String title, String mesg) {
		RandomAccessFile raf = null;

		try {
			if (EtechLog.EVENT_LOG) {

				raf = new RandomAccessFile(logEventFile, "rw");

				raf.seek(logEventFile.length());

				raf.writeUTF("<TR>" + "<TD style=\"color:#FF0000\">"
						+ new Date() + "</TD>" + "<TD style=\"color:#FF0000\">"
						+ title + "</TD>" + "<TD style=\"color:#FF0000\">"
						+ mesg + "</TD></TR>");
			}
		} 
		catch (Exception exception) { } 
		finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (Exception e) {
				}
			}
		}

		raf = null;
	}
	
	public static File endEventLogger(String event) {
		File file = logEventFile;
		logEventFile = null;
		return file;
	}
	
	public static void error(String title, String mesg) {
		File logFile = null;
		RandomAccessFile raf = null;

		try {
			if (EtechLog.DO_LOGGING) {

				logFile = Storage.verifyLogFile();

				raf = new RandomAccessFile(logFile, "rw");

				// seek to end of file
				raf.seek(logFile.length());

				raf.writeUTF("<TR>" + "<TD style=\"color:#FF0000\">"
						+ new Date() + "</TD>" + "<TD style=\"color:#FF0000\">"
						+ title + "</TD>" + "<TD style=\"color:#FF0000\">"
						+ mesg + "</TD></TR>");
			}
		} catch (Exception exception) {
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (Exception e) {
				}
			}
		}

		raf = null;
		logFile = null;
	}

	public static void error(String title, Exception mException) {/*
		mException.printStackTrace();
		File logFile = null;
		RandomAccessFile raf = null;
		StackTraceElement[] mStackTraceElement;
		String str = new String();
		try {
			if (EtechLog.DO_LOGGING) {
				logFile = Storage.verifyLogFile();

				raf = new RandomAccessFile(logFile, "rw");

				// seek to end of file
				raf.seek(logFile.length());

				mStackTraceElement = mException.getStackTrace();

				str = "<TR>" + "<TD style=\"color:#FF0000\">" + new Date()
						+ "</TD>" + "<TD style=\"color:#FF0000\">" + title
						+ "</TD><TD style=\"color:#FF0000\"><BR/>"
						+ mException.toString() + "<BR/><BR/>"
						+ mException.getMessage() + "<BR/><BR/>";

				for (int ele = 0; ele < mStackTraceElement.length; ele++) {
					str += mStackTraceElement[ele].getClassName() + "."
							+ mStackTraceElement[ele].getMethodName() + " ("
							+ mStackTraceElement[ele].getFileName() + " : "
							+ mStackTraceElement[ele].getLineNumber()
							+ ") <BR/>";
				}

				str += "</TD></TR>";
				raf.writeUTF(str);
			}
		} catch (Exception exception) {
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (Exception e) {
				}
			}
		}

		raf = null;
		logFile = null;
		str = null;

	*/}

	public static void print(String mesg) {
		if (EtechLog.DO_SOP) {/*
			System.out.println(mesg);
		*/}
	}

	public static void print(String title, String mesg) {
		if (EtechLog.DO_SOP) {/*
			error(title, mesg);
			System.out.println(title + " :: " + mesg);
		*/}
	}

	public static void print(String title, int i) {
		if (EtechLog.DO_SOP) {/*
			error(title, i + "");
			System.out.println(title + " :: " + i);
		*/}
	}

	public static void print(String title, Exception e) {
		if (EtechLog.DO_SOP) {/*
			error(title, e);
			System.out.println("=========================" + title
					+ "=========================");
			e.printStackTrace();
		*/}
	}

	public static void print(Exception e) {
		if (EtechLog.DO_SOP) {/*
			error("", e);
			e.printStackTrace();
		*/}
	}

	public static String htmlEncode(String str) {
		return str.replaceAll(">", "&lt;").replaceAll("<", "&gt;")
				.replaceAll("&", "&amp;").replaceAll("\"", "&quot;")
				.replaceAll("'", "&#039;");
	}

}
