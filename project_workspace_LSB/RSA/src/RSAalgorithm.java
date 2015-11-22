import java.math.*;
import java.security.SecureRandom;

public class RSAalgorithm {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 SecureRandom sr = new SecureRandom(); // 난수 생성
		 System.out.println("평문은 12,22,02");
		 BigInteger text1 = new BigInteger("12");
		 BigInteger text2 = new BigInteger("22");
		 BigInteger text3 = new BigInteger("02");
		 
		 BigInteger p = new BigInteger("7");
		 BigInteger q = new BigInteger("17");
		 
         BigInteger n = p.multiply(q); // n
         System.out.println("n is "+n);
         
         BigInteger one = new BigInteger("1");
         
         BigInteger phi_n
         = p.subtract(one).multiply(q.subtract(one)); // Func(Phi)

         BigInteger e;
         do {
                 e = new BigInteger(p.bitLength(), sr); // 공개키        
         } while(!e.gcd(phi_n).equals(one));
         
         System.out.println("PublicKey is "+e);

         BigInteger d = e.modInverse(phi_n); // 개인키
         System.out.println("PrivateKey is "+d);
        
         BigInteger encText1 = text1.modPow(e, n); // 암호화
         BigInteger encText2 = text2.modPow(e, n);
         BigInteger encText3 = text3.modPow(e, n);
         System.out.println("암호문 : "+encText1 +","+encText2+","+encText3);
         
         BigInteger dec1 = encText1.modPow(d, n); // 복호화
         BigInteger dec2 = encText2.modPow(d, n);
         BigInteger dec3 = encText3.modPow(d, n);   
         System.out.println("평 문 : " + dec1 +","+ dec2 +","+ dec3);
	}
}
