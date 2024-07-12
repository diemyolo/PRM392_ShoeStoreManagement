package com.shoestoreproject.UI_Layer.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shoestoreproject.Data_Layer.Repository.AccountRepository;

public class LoginViewModel extends ViewModel {
    private AccountRepository accountRepository;

    public LoginViewModel() {
        accountRepository = new AccountRepository();
    }

    public MutableLiveData<String> login(String email, String password) {
        return accountRepository.login(email, password);
    }
}