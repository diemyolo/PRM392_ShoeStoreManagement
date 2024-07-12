package com.shoestoreproject.UI_Layer.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shoestoreproject.Data_Layer.Repository.AccountRepository;

public class RegisterViewModel extends ViewModel {
    private AccountRepository accountRepository;

    public RegisterViewModel() {
        accountRepository = new AccountRepository();
    }

    public MutableLiveData<String> register(String email, String password, String userName) {
        return accountRepository.register(email, password, userName);
    }
}
