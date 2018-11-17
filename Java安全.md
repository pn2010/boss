#Java安全
##为什么要掌握Java安全？
* 提升逼格，现在市场上的大部分程序员都对Java安全、数据加密等等一无所知，而现在的面试中很多面试官为了凸显自己的高逼格，总爱问你一些安全相关的问题。只有掌握了Java安全相关知识，才能跟面试官站在同一逼格线，进行愉快滴交流。
* 项目中到处需要。如：账号密码的加密，订单支付的加密，个人资料的加密等等。
* 初级工程师向中高级工程师进阶的必经之路。
##什么是加密？
* 古老的加密方法：《潜伏》中的余则成，收到密码：12 25 13 15 29 19；《模仿游戏》，二战时期，德国的密码机及图灵为了破解德军密码而研制出第一代计算机（图灵机）。
* 对称加密和非对称加密等等。
* MD5，属于消息摘要（Message Digest）。

## 学习目标
1. 复习字节与字符、字符编码、进制转换、java里的io
2. 一些基本的安全知识
3. 三大风险：窃听风险、篡改风险、冒充风险
4. 重放攻击（wpe）
5. 常用加密算法（对称/非对称）
6. 消息摘要
7. 数字签名
8. 数字证书
9. Keytool工具的使用
10. SSL/TLS的工作原理
11. Https双向认证原理

#凯撒密码
##第一个案例，对字符进行简单的加密。
* 对字符数组中所有的字符做+1处理（后面可以转换成+num这个数由通讯双方来协定）
###代码

		public class SimpleEncryptionDemo {
	
		public static void main(String[] args) {
			String content = "tonight 404 see you";
			//加密
			String encryptData = encrypt(content);
			System.out.println("加密后" + encryptData);
			//解密
			String decryptData = decrypt(encryptData);
			System.out.println("解密后：" + decryptData);
		}
		/**
		 * 加密方法
		 * @param content
		 * @return
		 */
		public static String encrypt(String content){
			//第一步，将字符串转换成字符数组
			char[] charArray = content.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				//开始加密
				charArray[i] = (char) (charArray[i] + 1);
			}
			//将加密后的字符串返回出去
			return new String(charArray);
		}
		/**
		 * 解密
		 * @param encryptData
		 * @return
		 */
		public static String decrypt(String encryptData){
			//第一步，将字符串转换成字符数组
			char[] charArray = encryptData.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				//开始解密
				charArray[i] = (char) (charArray[i]-1);
			}
			return new String(charArray);
		}
	}
			
##第二个案例，对字节进行简单的加密。
	public class SimpleEncryptionDemo02 {
	
		public static void main(String[] args) {
			String content = "大街上广厦股份撒娇";
			int key = 80;
			//加密
			String encryptData = encrypt(content,key);
			System.out.println("加密后：" + encryptData);
			//解密
			String decryptData = decrypt(encryptData,key);
			System.out.println("解密后：" + decryptData);
		}
		/**
		 * 加密
		 * @param content
		 * @return
		 */
		public static String encrypt(String content,int key){
			//第一步，将字符串转换成字节数组
			byte[] bytes = content.getBytes();
			System.out.println("------------打印原文的字节数组");
			Util.printBytes(bytes);
			for (int i = 0; i < bytes.length; i++) {
				//加密
				bytes[i] = (byte) (bytes[i] + key);
			}
			System.out.println("------------打印加密后的字节数组");
			Util.printBytes(bytes);
	//		return new String(bytes);
			return Base64.getEncoder().encodeToString(bytes);
		}
		/**
		 * 解密
		 * @param encryptData
		 * @return
		 */
		public static String decrypt(String encryptData,int key){
			//第一步，将字符串转换成字节数组
	//		byte[] bytes = encryptData.getBytes();
			byte[] bytes = Base64.getDecoder().decode(encryptData);
			System.out.println("------------打印经过两次转换的字节数组");
			Util.printBytes(bytes);
			for (int i = 0; i < bytes.length; i++) {
				//解密
				bytes[i] = (byte) (bytes[i] - key);
			}
			return new String(bytes);
		}
	}

**字节和字符的区别？**

* UTF-8编码下，一个英文或数字字符就是一个字节
* UTF-8编码下，一个汉字代表三个字节。
* 为什么new String(bytes)的时候回发生乱码，就是因为加密后得到的字节找不到对应的字符。
* Base64的原理
![](Base64yuanli.png)
		
##对称加密与非对称加密
* 对称加密与非对称加密的区别
	* 对称加密。同一把钥匙进行加密和解密
	* 非对称加密，公钥加密，私钥解密。

##对称加密的案例
常见算法：AES、DES、3DES、TDEA、Blowfish、RC2、RC4、RC5、IDEA、SKIPJACK  
AES:高级加密标准（Advanced Encryption Standard)  
DES:数据加密标准(Data Encryption Standard) 
###对称加密第一个案例，代码，。
		public class SymmetricalEncryption {
	
		public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
			String content = "今晚六点半404不见不散！";
			SecretKey secretKey = getSecretKey();
			Cipher cipher = Cipher.getInstance("AES");
			//加密
			String encryptData = encrypt(cipher, content, secretKey);
			System.out.println("加密后的数据：" + encryptData);
			//解密
			String decryptData = decrypt(cipher, encryptData, secretKey);
			System.out.println("解密后的数据：" + decryptData);
		}
		/**
		 *获取秘钥
		 * @return
		 * @throws NoSuchAlgorithmException
		 */
		private static SecretKey getSecretKey() throws NoSuchAlgorithmException {
			//得到keyGenerator对象
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey;
		}
		/**
		 * 加密AES
		 * @param content
		 * @return
		 * @throws NoSuchPaddingException 
		 * @throws NoSuchAlgorithmException 
		 * @throws InvalidKeyException 
		 * @throws BadPaddingException 
		 * @throws IllegalBlockSizeException 
		 */
		public static String encrypt(Cipher cipher,String content,SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
			//content转换成字节数组
			byte[] bytes = content.getBytes();
			//初始化cipher,确定为加密模式
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			//开始加密
			byte[] encryptBytes = cipher.doFinal(bytes);
			return Base64.getEncoder().encodeToString(encryptBytes);
		}
		/**
		 * 解密
		 * @param encryptData
		 * @return
		 * @throws InvalidKeyException 
		 * @throws BadPaddingException 
		 * @throws IllegalBlockSizeException 
		 */
		public static String decrypt(Cipher cipher,String encryptData,SecretKey secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
			//将加密后的内容转换成bytes,使用Base64解码
			byte[] bytes = Base64.getDecoder().decode(encryptData);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			//开始解密
			byte[] decryptBytes = cipher.doFinal(bytes);
			return new String(decryptBytes);
		}
	}
		
###对称加密的第二个案例，将生成的key保存到本地，然后解密时和下一次加密时就只需要到本地读取即可。。

**保存对象到文件**
	
	/**
	 * 保存对象到文件中
	 * @param object
	 * @param fileName
	 * @throws IOException 
	 */
	public static void saveObject2File(Object object,String fileName) throws IOException{
		FileOutputStream out = new FileOutputStream(new File(fileName));
		ObjectOutputStream oos = new ObjectOutputStream(out);
		try {
			oos.writeObject(object);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			out.close();
			oos.close();
		}
	}
	
**从文件中读取对象**

	/**
	 * 从文件中读取对象
	 * @param fileName
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static Object readObjectFromFile(String fileName) throws IOException, ClassNotFoundException{
		FileInputStream in = new FileInputStream(new File(fileName));
		ObjectInputStream ois = new ObjectInputStream(in);
		Object object = null;
		try {
			object = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			in.close();
			ois.close();
		}
		
		return object;
	}
	
###第三个案例——自定义秘钥，代码
		
	/**
	 *自定义秘钥
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static SecretKey getSecretKey(String keyWord) throws NoSuchAlgorithmException {
		//得到keyGenerator对象
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(new SecureRandom(keyWord.getBytes()));
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey;
	}

###应用场景
* 登录，post请求{username=lisi,pw=加密}

##非对称加密的案例
常见算法：RSA、Elgamal、背包算法、Rabin、D-H、ECC（椭圆曲线加密算法）等 
应用场景：银行和电商网站，他就采用非对称加密，将公钥给所有人，你们就用这个公钥加密，私钥我自己留着，谁也不知道，所以除了我，谁也解密不了。
###第一个案例（数据量不大的情况下可用，加密数据小于117个字节，解密数据小于128个字节）

		/**
		 * @author Camille
		 *非对称加密
		 */
		public class AsymmetricEncryption {
		
			public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
				String content = "今晚六点半404不见不散";
				//获取密钥对
				KeyPair keypair = getKeypair();
				PublicKey publicKey = keypair.getPublic();
				//获取cipher
				Cipher cipher = Cipher.getInstance("RSA");
				//加密
				String encryptData = encrypt(cipher, content, publicKey);
				System.out.println(encryptData);
				PrivateKey privateKey = keypair.getPrivate();
				String decryptData = decrypt(cipher, encryptData, privateKey);
				System.out.println(decryptData);
			}
			/**
			 * 获取密钥对
			 * @return
			 * @throws NoSuchAlgorithmException
			 */
			public static KeyPair getKeypair() throws NoSuchAlgorithmException{
				KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
				KeyPair keyPair = keyPairGenerator.generateKeyPair();
				return keyPair;
			}
			/**
			 * 加密AES
			 * @param content
			 * @return
			 * @throws NoSuchPaddingException 
			 * @throws NoSuchAlgorithmException 
			 * @throws InvalidKeyException 
			 * @throws BadPaddingException 
			 * @throws IllegalBlockSizeException 
			 */
			public static String encrypt(Cipher cipher,String content,PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
				//content转换成字节数组
				byte[] bytes = content.getBytes();
				//初始化cipher,确定为加密模式
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				//开始加密
				byte[] encryptBytes = cipher.doFinal(bytes);
				return Base64.getEncoder().encodeToString(encryptBytes);
			}
			/**
			 * 解密
			 * @param encryptData
			 * @return
			 * @throws InvalidKeyException 
			 * @throws BadPaddingException 
			 * @throws IllegalBlockSizeException 
			 */
			public static String decrypt(Cipher cipher,String encryptData,PrivateKey privateKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
				//将加密后的内容转换成bytes,使用Base64解码
				byte[] bytes = Base64.getDecoder().decode(encryptData);
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
				//开始解密
				byte[] decryptBytes = cipher.doFinal(bytes);
				return new String(decryptBytes);
			}
		}
	
###如果数据量大的话，则要对要加密、解密的数据进行分块处理，代码如下：
	
		/**
	 * 分块进行加密或解密
	 * @param cipher
	 * @param bytes
	 * @param max
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 */
	private static ByteArrayOutputStream doFinalWithBlock(Cipher cipher, byte[] bytes,int max)
			throws IllegalBlockSizeException, BadPaddingException, IOException {
		int inputOffset = 0;
		int leng = bytes.length;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while(inputOffset < leng){
			if (leng - inputOffset >= max) {
				byte[] encryptBytes = cipher.doFinal(bytes, 0, max);
				baos.write(encryptBytes);
				inputOffset = inputOffset + max;
			}else {
				byte[] encryptBytes = cipher.doFinal(bytes, 0, leng - inputOffset);
				baos.write(encryptBytes);
				inputOffset = leng;
			}
		}
		return baos;
	}

##消息摘要
消息摘要是一个不可逆的过程。常用来防篡改。

常见算法：MD5、SHA、CRC等 

###byte数组和16进制字符串的互转

	/**
	 * byte数组转换成16进制数
	 * @param bytes
	 * @return
	 */
	public static String bytes2Hex(byte[] bytes){
		StringBuilder sBuilder = new StringBuilder();
		//先遍历出所有的字节
		for (byte b : bytes) {
			//取高位
			int high = (b & 0xf0) >> 4;
			//取低位
			int low = b & 0x0f;
			sBuilder.append(HEX[high]).append(HEX[low]);
		}
		return sBuilder.toString();
	}

	/**
	 * 将16进制转成byte数组
	 * @param hex
	 * @return
	 */
	public static byte[] hex2Bytes(String hex){
		int len = hex.length()/2;
		byte[] byets = new byte[len];
		for (int i = 0; i < len; i++) {
			//取高位字符,偶数个
			String highStr = hex.substring(2*i, 2*i + 1);
			//取低位字符，奇数个
			String lowStr = hex.substring(2*i+1, 2*i+2);
			//将字符串转换成int
			int high = Integer.parseInt(highStr, 16) << 4;
			int low = Integer.parseInt(lowStr, 16);
			byets[i] = (byte) (high + low);
		}
		return byets;
	}

##数字签名

**签名过程**

	public class DigitalSignature {

	public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, ClassNotFoundException, IOException {
		String content = "今晚404不见不散";
		//获取数字签名对象
		Signature signature = Signature.getInstance("MD5withRSA");
		//先使用私钥进行签名
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		PrivateKey privateKey = keyPair.getPrivate();//获取私钥
		PublicKey publicKey = keyPair.getPublic();//获取公钥
		signature.initSign((PrivateKey) SerializableUtil.readObjectFromFile("privatekey.heima"));
		signature.update(content.getBytes());
		byte[] sign = signature.sign();
		System.out.println(sign);
		//认证部分
		signature.initVerify(publicKey);
		String conten2 = "今晚505不见不散";
		signature.update(content.getBytes());
		boolean verify = signature.verify(sign);
		System.out.println(verify);
	}

}		

###数字签名的流程(画图理解)
###Signature**

	
##keytool的使用。
* 生成keyPair  keytool -genkeypair
* 修改别名   keytool  -changealias -alias mykey -destalias heima1
* 导出证书   keytool -exportcert
* 导入证书   keytool -importcert

**获取文件中的证书**

		// 获取证书工厂的实例
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				FileInputStream inStream = new FileInputStream(new File("heima.cer"));
				X509Certificate certificate = (X509Certificate) cf.generateCertificate(inStream);

##SSl
**双向认证的原理**

		