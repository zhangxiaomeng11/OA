package com.tz.oa.framework.util;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * 用于测试各种加密算法
 * 
 * @author Administrator 不可逆算法类型 MD5 SHA1 可逆算法 BASE64 HEX
 */
public class EncrptTest {

	// 测试不可逆的md5加密算法 e10adc3949ba59abbe56e057f20f883e
	@Test
	public void testMD5() {
		String plainPsd = "123456";
		String encrptPsd = DigestUtils.md5Hex(plainPsd.getBytes());
		System.out.println(encrptPsd);
	}

	// 测试不可逆的sha1加密算法 7c4a8d09ca3762af61e59520943dc26494f8941b
	// 7c4a8d09ca3762af61e59520943dc26494f8941b
	@Test
	public void testSha1() {
		String plainPsd = "123456";
		String encrptPsd = DigestUtils.sha1Hex(plainPsd.getBytes());
		System.out.println(encrptPsd);
	}

	// 测试可逆的BASE64加密算法--加密
	@Test
	public void testBASE64Encode() {
		String plainPsd = "123456";
		String encrptPsd = Base64.encode(plainPsd.getBytes());
		System.out.println(encrptPsd); // MTIzNDU2
	}

	// 测试可逆的BASE64加密算法--解密
	@Test
	public void testBASE64Decode() throws Base64DecodingException {
		String ecrptPsd = "MTIzNDU2";
		@SuppressWarnings("restriction")
		String plainPsd = new String(Base64.decode(ecrptPsd.getBytes()));
		System.out.println(plainPsd);
	}

	// 测试可逆的Hex加密算法--加密
	@Test
	public void testHexEncode() {
		String plainPsd = "123456";
		System.out.println(Hex.encodeHex(plainPsd.getBytes())); // 313233343536
	}

	// 测试可逆的Hex加密算法--解密
	@Test
	public void testHexDecode() throws DecoderException {
		String ecrptPsd = "313233343536";
		String plainPsd = new String(Hex.decodeHex(ecrptPsd.toCharArray()));
		System.out.println(plainPsd);
	}

	// palinPsd = 123456
	// 1: 生成一个随机数
	// 2: 用可逆的加密算法加密随机数 (hex)
	// 3: 将随机数和我们的密码用 不可逆加密算法(sha1) 进行加密
	// 4: 讲第三步得到的字符串值用可逆的加密算法加密
	// 5: 讲第二步和第四步的值拼凑成我们需要的 加密值encryptPsd
	//
	//
	@Test
	public void testOAencrypt() {
		String plainPsd = "123456";
		// 1
		byte[] random = EncryptUtil.generateSalt(8);
		// 2
		String randomHex = EncryptUtil.encodeHex(random);
		// 3
		byte[] thirdEncrypt = EncryptUtil.sha1(plainPsd.getBytes(), random, 1024);
		// 4
		String sha1Psd = EncryptUtil.encodeHex(thirdEncrypt);
		// 5 randomHex + sha1Psd
		// fd7d8de7f40126a 7a23c7e4863adc608a29b5d703d760a7c5277493a
		// aac209a1267fad52b42696b5e903fef4c65c46a30b0daa957d277497
		String encryptPsd = randomHex + sha1Psd;
		System.out.println(encryptPsd);
	}

	@Test
	public void testPsdValidator() {
		String plainPsd = "123456";
		String encrypPsd = "fd7d8de7f40126a7a23c7e4863adc608a29b5d703d760a7c5277493a";
		// fd7d8de7f40126a7a23c7e4863adc608a29b5d703d760a7c5277493a
		// 将密文急转 ,得到random(salt)的的明文
		byte[] salt = EncryptUtil.decodeHex(encrypPsd.substring(0, 16));

		// 重新根据盐,输入的明文密码,以及迭代的次数来生成密文 ,生成密文
		byte[] hashPassword = EncryptUtil.sha1(plainPsd.getBytes(), salt, 1024);
		String newEncrypPsd = EncryptUtil.encodeHex(salt) + EncryptUtil.encodeHex(hashPassword);

		System.out.println(newEncrypPsd);
	}

}
