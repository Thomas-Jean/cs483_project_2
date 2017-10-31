import java.math.BigInteger;
import java.security.SecureRandom;
import java.lang.Math;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


class RSAKeyGen{

	class OpPar {

		OpPar(String publicFileName, String secretFileName, int securityParameter){
			this.publicFileName = publicFileName;
			this. secretFileName = secretFileName;
			this.securityParameter = securityParameter;
		}

		String publicFileName;
		String secretFileName;
		int securityParameter;
		
	}

	RSAKeyGen(String[] args){
		OpPar options = getParameters(args);
		if (options.securityParameter == -1){

			System.err.printf("Usage: rsa-keygen -p [public key file] -s [secret key file] -n [number of bits in key]");
			System.exit(1);
		}

		SecureRandom rand = new SecureRandom();

		PrimeAndOrder po;
		BigInteger e;

		do{
			po = calcuateN(options.securityParameter, rand);

			e = picke(rand);

		}while (!(e.gcd(po.order).compareTo(BigInteger.ONE) == 0));

		BigInteger d = e.modInverse(po.order);


		writePublicKey(options.securityParameter, po.N, e, options.publicFileName);

		writeSecretKey(options.securityParameter, po.N, d, options.secretFileName);
			

	}

	OpPar getParameters(String [] args){
		if( args.length == 6){

			String pub = "";
			String sec = "";
			int secPar = -1;
			boolean pubFlag = false;
			boolean secFlag = false;
			boolean secParFlag = false;
			boolean errorFlag = false;

			for(int i=0; i < args.length;i=i+2){

				if(args[i].equals("-p")){
					pub = args[i+1];
					pubFlag = true;
				}else if(args[i].equals("-s")){
					sec = args[i+1];
					secFlag = true;
				}else if(args[i].equals("-n")){
					secPar = Integer.parseInt(args[i+1]);
					secParFlag = true;
				}else{
					errorFlag = true;
				}

			}

			if(pubFlag && secFlag && secParFlag && !errorFlag){
				return new OpPar(pub,sec,secPar);
			}
		}
		
		System.err.printf("Usage: rsa-keygen -p [public key file] -s [secret key file] -n [number of bits in key]");
		System.exit(1);

		return new OpPar("","",-1);

	}

	class PrimeAndOrder {

		PrimeAndOrder(BigInteger N, BigInteger order){
			this.N = N;
			this.order = order;
		}

		BigInteger N;
		BigInteger order;
		
	}

	PrimeAndOrder calcuateN(int nbits, SecureRandom secRandom){
		int p1bits = nbits/2;
		int p2bits = nbits - p1bits;

		BigInteger prime1 = BigInteger.probablePrime(p1bits,secRandom);
		BigInteger prime2 = BigInteger.probablePrime(p2bits,secRandom);

		BigInteger N = prime1.multiply(prime2);

		while(N.bitLength() != nbits){
			prime1 = BigInteger.probablePrime(p1bits+1,secRandom);
			N = prime1.multiply(prime2);
		}

		prime1 = prime1.subtract(BigInteger.ONE);
		prime2 = prime2.subtract(BigInteger.ONE);

		BigInteger order = prime1.multiply(prime2);

		PrimeAndOrder ret = new PrimeAndOrder(N, order);

		return ret;
	}

	BigInteger picke(SecureRandom rand){
		int[] lowOddPrimes = {3, 5, 7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101};

		int p = lowOddPrimes[Math.abs(rand.nextInt())%lowOddPrimes.length];

		BigInteger bigP = BigInteger.valueOf(p);
		return bigP;

	}

	void writePublicKey(int numBits, BigInteger N, BigInteger e, String publicKeyFilename){
		
		try{
			PrintWriter fw = new PrintWriter( new BufferedWriter(new FileWriter(publicKeyFilename)));

			fw.format("%d%n",numBits);
			fw.format("%s%n",N.toString());
			fw.format("%s%n",e.toString());
			fw.close();

		}catch(IOException ioe){
			System.err.printf("%s%n",ioe.getMessage());
			System.exit(1);
		}
		

	}

	void writeSecretKey(int numBits, BigInteger N, BigInteger d, String secretKeyFilename){

		try{
			PrintWriter fw = new PrintWriter( new BufferedWriter(new FileWriter(secretKeyFilename)));

			fw.format("%d%n",numBits);
			fw.format("%s%n",N.toString());
			fw.format("%s%n",d.toString());
			fw.close();

		}catch(IOException ioe){
			System.err.printf("%s%n",ioe.getMessage());
			System.exit(1);
		}
		
		
	}


	public static void main(String[] args) {
		new RSAKeyGen(args);
	}
}
