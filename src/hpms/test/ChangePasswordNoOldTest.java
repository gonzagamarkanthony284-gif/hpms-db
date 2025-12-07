package hpms.test;

import hpms.auth.AuthService;

public class ChangePasswordNoOldTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting changePasswordNoOld quick test...");
        // create patient account
        AuthService.createPatientAccount("pNoOld", "init123");
        String curr = AuthService.getLastPlaintextForUI("pNoOld");
        System.out.println("Initial stored plaintext: " + curr);
        if (curr == null || !curr.equals("init123")) { System.err.println("Initial plaintext not available"); System.exit(2); }
        java.util.List<String> out = AuthService.changePasswordNoOld("pNoOld", "newpass456");
        System.out.println(out);
        String now = AuthService.getLastPlaintextForUI("pNoOld");
        System.out.println("After change plaintext: " + now);
        if (now == null || !now.equals("newpass456")) { System.err.println("Change did not update plaintext store"); System.exit(3); }
        System.out.println("changePasswordNoOld test OK");
    }
}
