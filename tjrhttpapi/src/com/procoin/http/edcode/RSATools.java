package com.procoin.http.edcode;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSATools {
	/** BASE64编码表 */
	public static final byte[] encodingTable = { (byte) 'A', (byte) 'B',
			(byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9',
			(byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
			(byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',
			(byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V',
			(byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a',
			(byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u',
			(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G',
			(byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k',
			(byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q',
			(byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
			(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
			(byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p',
			(byte) '+', (byte) '/' };// <br/>
	/** BASE64解码表 */
	public static final byte[] decodingTable;
	//对解码表decodingTable进行初始化
	static {
		decodingTable = new byte[128];
		for (int i = 0; i < encodingTable.length; i++) {
			decodingTable[encodingTable[i]] = (byte) i;
		}
	}
	
//	/** RSA算法中公钥和密钥共用的系数N。代表着一个BigInteger。 */
//	public static final String RSA_MODULUS = "137249648689108029143304918815644151184421068415369811200950688402071786395079336886859304232055866735657512381546033184204341954462962803953029094908431239222890029395235329837489066986110054086716904699510147654596398188997246660371499471456893967432770709475010651408576130786041215540779401547053778057327";
//	/** RSA算法中公钥的系数e。代表着一个BigInteger。 */
//	public static final String RSA_PUBLIC_EXPONENT = "65537";
//	/** RSA算法中公钥的系数d。代表着一个BigInteger。 */
//	public static final String RSA_PRIVATE_EXPONENT = "89113738056653872714739931724724810735912313703874394711733123165310567384582998050103561555799643580507171015755173475353817825751987323707360636686811497153394714240596460797516291938180666891432229363007789736972065918712954058518412334758686865376487734220460439334642441650399637017859558156920078503169";
	/** RSA算法中公钥和密钥共用的系数N。代表着一个BigInteger。 */
	public static final String RSA_MODULUS = "8712451969643702606112258857623700562067911588704604801849180165742891862608178673287886953882099222833377107817025625775117825867297399827215691217683093";
	// 
	/** RSA算法中公钥的系数e。代表着一个BigInteger。 */
	public static final String RSA_PUBLIC_EXPONENT = "65537";
	/** RSA算法中公钥的系数d。代表着一个BigInteger。 */
	public static final String RSA_PRIVATE_EXPONENT = "6865391413526858624707499339064357052151809789515417333443661005225447677810463123912490139988980351130229349793307348404618448881888177632767594299136545";


	public static final String RSA = "RSA";
	
	public static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		// 其中Modulus和E(PublicExponent)就是公钥
		RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
//		System.out.println("RSAPublicKey:");
//		System.out.println("Modulus.length=" + rsaPublicKey.getModulus().bitLength());
//		System.out.println("Modulus=" + rsaPublicKey.getModulus().toString());
//		System.out.println("PublicExponent.length=" + rsaPublicKey.getPublicExponent().bitLength());
//		System.out.println("PublicExponent=" + rsaPublicKey.getPublicExponent().toString());
		return publicKey;
	}

	public static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		// Modulus和D(privateExponent)就是私钥
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
		// 查看私钥信息
//		System.out.println("RSAPrivateKey:");
//		System.out.println("Modulus.length=" + rsaPrivateKey.getModulus().bitLength());
//		System.out.println("Modulus=" + rsaPrivateKey.getModulus().toString());
//		System.out.println("PrivateExponent.length=" + rsaPrivateKey.getPrivateExponent().bitLength());
//		System.out.println("PrivateExponent=" + rsaPrivateKey.getPrivateExponent().toString());
		return privateKey;
	}

	public static PublicKey getPublicKey(String modulus, String publicExponent) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		BigInteger bigIntModulus = new BigInteger(modulus);
		BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	public static PrivateKey getPrivateKey(String modulus, String privateExponent) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		BigInteger bigIntModulus = new BigInteger(modulus);
		BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(bigIntModulus, bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}
	
	/** 进行RSA编解码，动态生成公钥与私钥。 */
	public static void codeRSADymanic() {
		String content = "aaaaaaaa";
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
			// 密钥位数
			keyPairGen.initialize(512);
			// 动态生成密钥对，这是当前最耗时的操作，一般要2s以上。
			KeyPair keyPair = keyPairGen.generateKeyPair();
			// 公钥
			PublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			// 私钥
			PrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			byte[] publicKeyData = publicKey.getEncoded();
			byte[] privateKeyData = privateKey.getEncoded();
			// 加解密类
			Cipher cipher = Cipher.getInstance(RSA);// Cipher.getInstance("RSA/ECB/PKCS1Padding");
			// 使用公钥加密
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] dataEncode = cipher.doFinal(content.getBytes());
			// 为了可读性，临时转为Base64，显示在TextView上。
			byte[] tmpStr = Base64.encode(encodingTable, dataEncode);
			String result = new String(tmpStr);
//			System.out.println("Codegram:\n" + result + "\n");
			// 通过密钥字符串得到密钥
			publicKey = RSATools.getPublicKey(publicKeyData);
			privateKey = RSATools.getPrivateKey(privateKeyData);
			// 解密
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] dataDecode = cipher.doFinal(dataEncode);
			String originalContent = new String(dataDecode);
//			System.out.println("Original:\n" + originalContent);
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static String aesEncrypt(String original){
		try {
			PublicKey publicKey = RSATools.getPublicKey(RSA_MODULUS, RSA_PUBLIC_EXPONENT);
			Cipher cipher = Cipher.getInstance(RSA); // "RSA/ECB/PKCS1Padding"
			// 加密
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] dataEncode = cipher.doFinal(original.getBytes());
			// 为了可读性，临时转为Base64，显示在TextView上。
			byte[] tmp = Base64.encode(encodingTable, dataEncode);
			return new String(tmp);
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String aesDecrypt(String encrypted){
		try {
			byte[] data = Base64.decode(decodingTable, encrypted.getBytes());
			PrivateKey privateKey = RSATools.getPrivateKey(RSA_MODULUS, RSA_PRIVATE_EXPONENT);
			Cipher cipher = Cipher.getInstance(RSA); // "RSA/ECB/PKCS1Padding"
			// 加密
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] dataDecode = cipher.doFinal(data);
			return new String(dataDecode);
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
}
