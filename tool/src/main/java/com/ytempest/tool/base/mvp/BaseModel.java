package com.ytempest.tool.base.mvp;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public class BaseModel implements IModel {

    private IContract mContract;

    @Override
    public <T extends IContract> void setContract(T contract) {
        mContract = contract;
    }

    @Override
    public <T extends IContract> T getContract() {
        return (T) mContract;
    }

    @Override
    public void detach() {
        mContract = null;
    }
}
