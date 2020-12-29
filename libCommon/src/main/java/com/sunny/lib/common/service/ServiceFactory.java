package com.sunny.lib.common.service;

/**
 * Created by zhangxin17 on 2020/12/29
 */
public class ServiceFactory {
    private IAccountService accountService;


    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    public IAccountService getAccountService() {
        if (accountService == null) {
            return new EmptyAccountService();
        }
        return accountService;
    }

    private ServiceFactory() {
    }

    private static class Singleton {
        private static final ServiceFactory INSTANCE = new ServiceFactory();
    }

    public static ServiceFactory getInstance() {
        return Singleton.INSTANCE;
    }
}
