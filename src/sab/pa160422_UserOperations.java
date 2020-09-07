package sab;

import rs.etf.sab.operations.UserOperations;

import java.util.List;

public class pa160422_UserOperations implements UserOperations {
    @Override
    public boolean insertUser(String s, String s1, String s2, String s3, int i) {
        return false;
    }

    @Override
    public boolean declareAdmin(String s) {
        return false;
    }

    @Override
    public int getSentPackages(String... strings) {
        return 0;
    }

    @Override
    public int deleteUsers(String... strings) {
        return 0;
    }

    @Override
    public List<String> getAllUsers() {
        return null;
    }
}
