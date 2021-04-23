package m.heim.server.controller;

import m.heim.server.domain.User;
import m.heim.server.service.UserService;
import m.heim.server.service.Web3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.SignatureException;
import java.util.Optional;

@RestController()
@RequestMapping("api/login")
public class LoginController {
    UserService userService;
    Web3Service web3Service;

    @Autowired
    public LoginController(UserService userService, Web3Service web3Service) {
        this.userService = userService;
        this.web3Service = web3Service;
    }

    @GetMapping("getNonce")
    private ResponseEntity<String> getNonce(@RequestParam String publicAddress){
        publicAddress = publicAddress.toLowerCase();
        if(!web3Service.isAddressValid(publicAddress)) return new ResponseEntity<>("Invalid Address", HttpStatus.BAD_REQUEST);
        Optional<User> optionalUser = userService.getUserRepository().findByPublicAddress(publicAddress);
        User user;
        if(optionalUser.isEmpty()) user = new User(publicAddress);
        else user = optionalUser.get();
        if(user.getNonce() == null) {
            user.setNonce(web3Service.createNonce());
            userService.getUserRepository().save(user);
        }
        return new ResponseEntity<>(user.getNonce(), HttpStatus.OK);
    }

    @GetMapping("getToken")
    private ResponseEntity<String> getToken(@RequestParam String signedNonce, @RequestParam String nonce) throws SignatureException {
        String recoveredAddress = web3Service
                .recoverAddressFromSignature(signedNonce, nonce);
        Optional<User> user = userService.getUserRepository().findByPublicAddress(recoveredAddress.toLowerCase());
        if(user.isEmpty() || user.get().getNonce() == null || !user.get().getNonce().equals(nonce)) return new ResponseEntity<>("Invalid signature",HttpStatus.BAD_REQUEST);
        user.get().setNonce(null);
        userService.getUserRepository().save(user.get());
        return new ResponseEntity<>(web3Service.getTokenFromAddress(user.get().getPublicAddress()), HttpStatus.OK);
    }
}
