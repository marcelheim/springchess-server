package m.heim.server.service;

import m.heim.server.domain.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

@Service
public class Web3Service {
    private final TokenService tokenService;

    @Autowired
    public Web3Service(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public String getTokenFromAddress(String address){
        Instant expirationTime = Instant.now().plus(1, ChronoUnit.HOURS);
        Date expirationDate = Date.from(expirationTime);
        return tokenService.generateToken(new TokenModel(address, expirationDate));
    }

    public String recoverAddressFromSignature(String signedMessage, String nonce) throws SignatureException {
        String message = "\u0019Ethereum Signed Message:\n" + nonce.length() + nonce;
        String r = signedMessage.substring(0, 66);
        String s = "0x"+signedMessage.substring(66, 130);
        String v = "0x"+signedMessage.substring(130, 132);
        Sign.SignatureData signatureData = new Sign.SignatureData(Numeric.hexStringToByteArray(v)[0],
                Numeric.hexStringToByteArray(r),
                Numeric.hexStringToByteArray(s));
        BigInteger publicKey = Sign.signedMessageToKey(message.getBytes(), signatureData);
        return "0x" + Keys.getAddress(publicKey);
    }

    public String createNonce(){
        Random random = new Random();
        int x = random.nextInt(100);
        return Integer.toString(x);
    }

    public boolean isAddressValid(String address){
        return WalletUtils.isValidAddress(address);
    }
}
