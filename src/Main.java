import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;

public class Main {

    private BigInteger PrivateKey, PublicKey1, PublicKey2;


    private Main() {

        Menu();
    }

    public static void main(String[] args) {
        new Main();
    }

    private void Menu() {
        while (true) {
            switch (IO.readInt("Please choose:\n" + "1. Generate new public key\n" + "2. Print public key\n" + "3. Print private Key\n" + "4. Encrypt message\n" + "5. Decrypt message\n" + "6. Insert private key\n" + "7. Quit Program \n")) {
                case (1):
                    GenKey();
                    break;
                case (2):
                    PrintPubKey();
                    break;
                case (3):
                    PrintPrivKey();
                    break;
                case (4):
                    EncryptMessage();
                    break;
                case (5):
                    DecryptMessage();
                    break;
                case (6):
                    InsertPrivKey();
                    break;
                case (7):
                    return;
            }
        }


    }

    private void GenKey() {
        BigInteger p = new BigInteger(1024, 2147483647, new Random());
        BigInteger q = new BigInteger(1024, 2147483647, new Random());
        PublicKey1 = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        PublicKey2 = getCoprime(phi);
        PrivateKey = Objects.requireNonNull(PublicKey2).modInverse(phi);
    }

    private BigInteger getCoprime(BigInteger a) {
        StringBuilder primA = new StringBuilder(" ");
        for (BigInteger i = new BigInteger("2"); i.compareTo(a) < 0; i = i.add(BigInteger.TWO)) {
            if (a.mod(i).compareTo(BigInteger.ZERO) == 0) {
                if (!primA.toString().contains(i.toString())) {
                    primA.append(i.toString()).append(" ");
                }
                a = a.divide(i);
                continue;
            }


            i = new BigInteger("3");

            while (0 > i.compareTo(a)) {


                if (a.mod(i).compareTo(BigInteger.ZERO) == 0) {
                    if (!primA.toString().contains(i.toString())) {
                        primA.append(i.toString()).append(" ");
                    }
                    a = a.divide(i);
                    break;
                }
                if (i.isProbablePrime(2147483647) && !primA.toString().contains(i.toString())) {
                    return i;
                }

                i = i.add(new BigInteger("2"));
            }
        }



        return null;
    }





    private void PrintPrivKey() {
        System.out.println("The private key is: " + PrivateKey.toString());
    }

    private void InsertPubKey1() {
        PublicKey1 = new BigInteger(IO.readString("Please insert the first part of your public Key: "));
    }

    private void InsertPrivKey() {
        PrivateKey = new BigInteger(IO.readString("Please insert your private Key: "));
    }

    private void DecryptMessage() {
        if (PrivateKey == null) {
            int i = IO.readInt("You have not set a private key. " + "If you want to insert your own private key press 1. " + "If you want to gen a new pair of keys press 2.\nIf you want to return to the menu press 3. ");
            if (1 == i) {
                InsertPrivKey();
            } else if (2 == i) {
                GenKey();
            } else if (3 == i) {
                return;
            } else {
                System.out.println("Please select 1, 2 or 3.");
            }
        }

        if (PrivateKey == null) {
            int j = IO.readInt("The first part of your public key can not be found. It is needed for the Encryption.\n" + "If you want to insert the first part of your public key press 1.\n " + "If you want to gen a new pair of keys press 2.\nIf you want to return to the menu press 3. ");
            if (1 == j) {
                InsertPubKey1();
            } else if (2 == j) {
                GenKey();
            } else if (3 == j) {
                return;
            } else {
                System.out.println("Please select 1, 2 or 3.");
            }
        }


        String EncryptedMessage = IO.readString("Insert your Encrypted message please: \n");
        String[] EncryptedMessageLetters = EncryptedMessage.split(" ");


        BigInteger[] EncryptedMessageLetterBigInteger = new BigInteger[EncryptedMessageLetters.length];

        char[] DecryptedMessageLettersChar = new char[EncryptedMessageLetterBigInteger.length];
        BigInteger[] DecryptedMessageBigInterger = new BigInteger[EncryptedMessageLetterBigInteger.length];

        for (int i = 0; i < EncryptedMessageLetters.length; i++) {
            EncryptedMessageLetterBigInteger[i] = new BigInteger(EncryptedMessageLetters[i]);
            DecryptedMessageBigInterger[i] = EncryptedMessageLetterBigInteger[i].modPow(PrivateKey, PublicKey1);
            DecryptedMessageLettersChar[i] = (char) Integer.parseInt( DecryptedMessageBigInterger[i].toString());
            System.out.print(DecryptedMessageLettersChar[i]);

        }
        System.out.println();

    }


    private BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        while (exponent.signum() > 0) {
            if (exponent.testBit(0)) result = result.multiply(base);
            base = base.multiply(base);
            exponent = exponent.shiftRight(1);
        }
        return result;
    }

    private void EncryptMessage() {

        BigInteger PubKey1Opponent, PubKey2Opponent;
        switch (IO.readInt("If you want to insert the public keys of your opponent press 1.\nIf you want to use your own public keys for testing press 2.\nIf you want to go back to the menu press 3.\n")) {
            case (1):
                PubKey1Opponent = new BigInteger(IO.readString("Please insert the first part of your opponents public key."));
                PubKey2Opponent = new BigInteger(IO.readString("Please insert the second part of your opponents public key."));
                break;
            case (2):
                PubKey1Opponent = PublicKey1;
                PubKey2Opponent = PublicKey2;
                break;
            case (3):
                return;
            default:
                System.out.println("Wrong insert.");
                DecryptMessage();
                return;
        }
        String CryptedMessage = IO.readString("Please insert the message: ");

        char[] CryptedMessageChar = CryptedMessage.toCharArray();

        int CryptedMessageInt[] = new int[CryptedMessageChar.length];


        for (int i = 0; i < CryptedMessage.length(); i++) {
            CryptedMessageInt[i] = (int) CryptedMessage.charAt(i) ;
        }


        BigInteger CryptedMessageBigInt[] = new BigInteger[CryptedMessageInt.length];
        for (int i = 0; i < CryptedMessage.length(); i++) {
            CryptedMessageBigInt[i] = new BigInteger(Integer.toString(CryptedMessageInt[i]));
            BigInteger Message = pow(CryptedMessageBigInt[i], PubKey2Opponent).mod(PubKey1Opponent);
            System.out.print(Message.toString() + " ");
        }
        System.out.println();
    }

    private void PrintPubKey() {
        System.out.println("First part of the private key: " + PublicKey1.toString() + "\nSecond part of the public key :" + PublicKey2.toString());
    }
}
/*private boolean primzahlgroß(BigInteger a) {
        BigInteger nul = BigInteger.ZERO;
        if (a == new BigInteger("2") || a == new BigInteger("3") || a == new BigInteger("5") || a == new BigInteger("7")) {
            return true;
        }
        if (a.mod(new BigInteger("2")).compareTo(nul) == 0 || a.mod(new BigInteger("3")).compareTo(nul) == 0 || a.mod(new BigInteger("5")).compareTo(nul) == 0 || a.mod(new BigInteger("7")).compareTo(nul) == 0 || a == BigInteger.ONE)
            return false;
        for (BigInteger i = new BigInteger("10"); i.multiply(i).compareTo(a) <= 0; i = i.add(new BigInteger("10 "))) {
            if ((a.mod(i.add(BigInteger.ONE)).compareTo(nul) == 0) || (a.mod(i.add(new BigInteger("3"))).compareTo(nul) == 0) || (a.mod(i.add(new BigInteger("7"))).compareTo(nul) == 0) || (a.mod(i.add(BigInteger.ONE)).compareTo(nul) == 0))
                return false;
        }
        return true;
    }*/


