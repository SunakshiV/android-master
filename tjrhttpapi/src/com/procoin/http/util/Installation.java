package com.procoin.http.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

/**
 * 这个是保存CALL_API文件信息
 * 
 * @author zhengmj
 * 
 */

public class Installation {
	private static String sID = null;
	private static final String TJRUUID = ".tjruuid";

	/**
	 * 这个方法是本机唯一设备id
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String aid(Context context) {
		String m_szImei = "0";
		try {
			TelephonyManager TelephonyMgr = (TelephonyManager) context
					.getSystemService(context.TELEPHONY_SERVICE);
			m_szImei = TelephonyMgr.getDeviceId();
		} catch (Exception e) {

		}
		String m_szDevIDShort = "1";
		try {
			m_szDevIDShort = "35"
					+ // we make this look like a valid IMEI
					Build.BOARD.length() % 10 + Build.BRAND.length() % 10
					+ Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
					+ Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
					+ Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
					+ Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
					+ Build.TAGS.length() % 10 + Build.TYPE.length() % 10
					+ Build.USER.length() % 10; // 13
												// digits
		} catch (Exception e) {
		}
		String m_szAndroidID = "2";
		try {
			m_szAndroidID = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
		} catch (Exception e) {
			// TODO: handle exception
		}
		String m_szWLANMAC = "3";
		try {
			WifiManager wm = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
		} catch (Exception e) {
			// TODO: handle exception
		}
		String m_szBTMAC = "4";
		try {
			BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth
														// adapter
			m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			m_szBTMAC = m_BluetoothAdapter.getAddress();
		} catch (Exception e) {
			// TODO: handle exception
		}
		String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID
				+ m_szWLANMAC + m_szBTMAC;
		// compute md5
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
			// get md5 bytes
			byte p_md5Data[] = m.digest();
			// create a hex string
			String m_szUniqueID = new String();
			for (int i = 0; i < p_md5Data.length; i++) {
				int b = (0xFF & p_md5Data[i]);
				// if it is a single digit, make sure it have 0 in front (proper
				// padding)
				if (b <= 0xF)
					m_szUniqueID += "0";
				// add number to string
				m_szUniqueID += Integer.toHexString(b);
			}
			// hex string to uppercase
			return m_szUniqueID.toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			return m_szLongID;
		}
	}

	/**
	 * 这个是随机的数字用来保存在机子本地 files目录下，保存随机数字
	 * 
	 * @param context
	 * @return
	 */
	public synchronized static String sid(Context context) {
		if (sID == null) {
			try {
				File installation = new File(Environment.getExternalStorageDirectory(), TJRUUID);
				if (!installation.exists())
					writeInstallationFile(installation);
				sID = readInstallationFile(installation);
			} catch (Exception e) {
				
			}
		}
		return sID;
	}

	private static String readInstallationFile(File installation)
			throws IOException {
		RandomAccessFile f = new RandomAccessFile(installation, "r");
		byte[] bytes = new byte[(int) f.length()];
		f.readFully(bytes);
		f.close();
		return new String(bytes);
	}

	private static void writeInstallationFile(File installation)
			throws IOException {
		FileOutputStream out = new FileOutputStream(installation);
		String id = UUID.randomUUID().toString();
		out.write(id.getBytes());
		out.close();
	}
}
